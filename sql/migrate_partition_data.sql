--Migrates data from the old DB format of storing partition data in 3 columns called partition_signature, 
-- partition_indices and partition_lengths into a single jsonb one called partition_info
--Creates the new column first, processing the past data into the correct format an updates the new col
--Then removes the old data and columns. 

--Recommended to run each part manually if possible to be sure you have confirmed that the data has been 
-- successfully migrated to the new partition_info column before removing it during the column drops
ALTER TABLE public.results ADD COLUMN partition_info JSONB;

--setup a basic json object of the correct length in partition_info
UPDATE results
SET partition_info = (
    SELECT jsonb_agg(jsonb_build_object('num', idx))
    FROM generate_series(1, array_length(string_to_array(partition_indices, '|'), 1)) AS idx
);

--update the new column with the entire JSON object we want now
WITH indexed_json AS (
    SELECT 
        result_id,
		partition_indices,
        partition_signature,
        elem,
        row_number() OVER (PARTITION BY result_id ORDER BY elem_index) AS counter  -- Correctly increments within the same result_id
    FROM (
        SELECT 
            result_id,
			partition_indices,
            partition_signature,
            jsonb_array_elements(partition_info) AS elem,
            generate_series(1, jsonb_array_length(partition_info)) AS elem_index  -- Creates an index for each element
        FROM 
            public.results
    ) subquery
),
updated_data AS (
    SELECT 
        result_id,
        jsonb_agg(
            jsonb_build_object(
				'index', 
				CASE 
                    WHEN counter <= length(partition_signature) THEN split_part(partition_indices, '|', counter::int )
				END ,
				'distance', '',  
                'isTruckRoute', 
                CASE 
                    WHEN counter <= length(partition_signature) THEN 
                        substring(partition_signature FROM counter::int FOR 1) = '1'  -- Converts '1' to true and '0' to false
                    ELSE false
                END
            )
        ) AS new_partition_info
    FROM 
        indexed_json
    GROUP BY 
        result_id
)
UPDATE results
SET partition_info = updated_data.new_partition_info
FROM updated_data
WHERE results.result_id = updated_data.result_id;

--drop the old columns once the data migration is complete  
ALTER TABLE public.results DROP COLUMN partition_signature, DROP COLUMN partition_indices, DROP COLUMN partition_lengths;  