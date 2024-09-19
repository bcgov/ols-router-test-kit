CREATE OR REPLACE FUNCTION get_binary_partition_sig(json_data jsonb, key_name TEXT)
RETURNS TEXT AS $$
DECLARE
    result TEXT := '';
    item jsonb;
    prev_value TEXT := '';
BEGIN
    -- Iterate through each element in the JSON array
    FOR item IN SELECT * FROM jsonb_array_elements(json_data)
    LOOP
        -- Determine the current value based on the specified key
        IF item->>key_name = 'true' THEN
            -- Append '1' if the previous value was '0' or if the result is empty
            IF prev_value = '0' OR result = '' THEN
                result := result || '1';
                prev_value := '1';
            END IF;
        ELSIF item->>key_name = 'false' THEN
            -- Append '0' if the previous value was '1' or if the result is empty
            IF prev_value = '1' OR result = '' THEN
                result := result || '0';
                prev_value := '0';
            END IF;
        END IF;
    END LOOP;
    
    IF result <> '' THEN
        -- Remove trailing duplicates to ensure the result alternates
        result := regexp_replace(result, '(.)\1+', '\1', 'g');
    END IF;
    
    RETURN result;
END;
$$ LANGUAGE plpgsql IMMUTABLE ;

