package ca.bc.gov.ols.router.testing.engine.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import ca.bc.gov.ols.router.testing.engine.entity.Result;
import jakarta.annotation.Resource;

@Resource
@Component
public interface ResultRepository extends JpaRepository <Result, Integer> {
	long count();
	Page<Result> findByRunIdIs(Integer runId, Pageable pageable);
	Page<Result> findByTestIdIs(Integer testId, Pageable pageable);
	long countByTestIdIs(Integer integer);
	long countByRunIdIs(Integer integer);
	
	/*
	 * Query that compares two runs to each other, calculating the differences in calctime, duration, distance etc. Still sortable and pagable with the standard Pageable type
	 */

	@Query(value ="SELECT a.resultId as a_result_id, a.testId as a_test_id, a.calcTime as a_calc_time, a.distance as a_distance, a.duration as a_duration,"
			+ "b.resultId as b_result_id, b.calcTime as b_calc_time, b.distance as b_distance, b.duration as b_duration, "
			+ "COALESCE((b.distance - a.distance), -1) as distance_diff, "
			+ "COALESCE((100*(b.distance - a.distance)/greatest(a.distance,1)), -1) as distance_perc,"
		    + "COALESCE((b.duration - a.duration), -1) as duration_diff,"
		    + "COALESCE((100*(b.duration - a.duration)/greatest(a.duration,1)), -1) as duration_perc,"
		    + "COALESCE((b.calcTime - a.calcTime), -1) as calc_time_diff,"
		    + "COALESCE((100*(b.calcTime - a.calcTime)/greatest(a.calcTime, 1)), -1) as calc_time_perc,"
		    + "COALESCE(ST_HausdorffDistance(ST_Simplify(a.routeGeometry, 0.0002), ST_Simplify(b.routeGeometry, 0.0002)) * 91000, -1) as hausdorff_distance," //*91000 is a VERY rough conversion of degrees to meters in BC, but it's fast and should be close enough for our needs here which is mostly just sorting what is "close" vs "far"
		    + "function('get_binary_partition_sig', a.partitionInfo ,'isTruckRoute') as a_partition_signature, "
		    + "function('get_binary_partition_sig', b.partitionInfo ,'isTruckRoute') as b_partition_signature, "
	        + "COALESCE((100 * (LENGTH(CAST(function('get_binary_partition_sig', b.partitionInfo ,'isTruckRoute') AS string)) - LENGTH(CAST(function('get_binary_partition_sig', a.partitionInfo ,'isTruckRoute') AS string))) / GREATEST(LENGTH(CAST(function('get_binary_partition_sig', a.partitionInfo ,'isTruckRoute') AS string)), 1)), 1) AS partition_diff, "
		    + "t.description as description, t.notes as notes"
		  + " FROM Result a JOIN Result b on a.testId = b.testId"
		  + "  JOIN Test t on b.testId = t.testId"
		  + " WHERE a.runId = :runIdA AND b.runId = :runIdB")
	List<Map> compareResultsOfRunIds(@Param("runIdA") Integer runIdA, @Param("runIdB") Integer runIdB, Pageable pageable);
	
	
	/*
	 * Query that gets all results for given run ID
	 */
	@Query(value ="SELECT r.runId as runId, r.forwardRouteInd as forwardRouteInd, "
		    + "  re.resultId as resultId, re.calcTime as calcTime, re.distance as distance, re.duration as duration, "
		    + " concat(e.platform, ' - ', e.environment) as environment, "
		    + " concat(d.roadSource, ' ', d.roadNetworkTimestamp, ' - ', d.description) as dataset, "
		    + " function('get_binary_partition_sig', re.partitionInfo ,'isTruckRoute') as partitionSignature"
		    + " FROM Result re "
		    + " JOIN Run r on re.runId = r.runId "
		    + " LEFT JOIN Dataset d ON r.datasetId = d.datasetId "
		    + " LEFT JOIN Environment e ON r.environmentId = e.environmentId "
		  	+ " WHERE r.runId = :runId ")
	List<Map> findByRunIdIsCustom(@Param("runId") Integer runId, Pageable pageable);
	
	/*
	 * Query that gets all results for given test ID
	 */
	@Query(value ="SELECT r.runId as runId, r.forwardRouteInd as forwardRouteInd, "
		    + "  re.resultId as resultId, re.calcTime as calcTime, re.distance as distance, re.duration as duration, "
		    + " concat(e.platform, ' - ', e.environment) as environment, "
		    + " concat(d.roadSource, ' ', d.roadNetworkTimestamp, ' - ', d.description) as dataset, "
		    + " function('get_binary_partition_sig', re.partitionInfo ,'isTruckRoute') as partitionSignature"
		    + " FROM Result re "
		    + " JOIN Run r on re.runId = r.runId "
		    + " LEFT JOIN Dataset d ON r.datasetId = d.datasetId "
		    + " LEFT JOIN Environment e ON r.environmentId = e.environmentId "
		  	+ " WHERE re.testId = :testId ")
	List<Map> findByTestIdIsCustom(@Param("testId") Integer testId, Pageable pageable);


	/*
	 * Query that gets the simple count of the above query 
	 */
	@Query(value ="SELECT count(*) "
		  + " FROM Result a JOIN Result b on a.testId = b.testId"
		  + "  JOIN Test t on b.testId = t.testId"
		  + " WHERE a.runId = :runIdA AND b.runId = :runIdB")
	Integer compareResultsOfRunIdsCount(@Param("runIdA") Integer runIdA, @Param("runIdB") Integer runIdB);


	/*
	 * Query that compares a runs to reference tests, calculating the differences in calctime, duration, distance etc. Still sortable and pageable with the standard Pageable type
	 */
	@Query(value ="SELECT a.resultId as a_result_id, b.testId as test_id, a.calcTime as a_calc_time, a.distance as a_distance, a.duration as a_duration,"
			+ "    b.resultId as b_result_id,"
			+ "    b.calcTime as b_calc_time,"
			+ "    b.distance as b_distance,"
			+ "    b.duration as b_duration,"
			+ "    (b.calcTime - a.calcTime) as calc_time_diff,"
			+ "    (b.distance - a.distance) as distance_diff,"
			+ "    (b.duration - a.duration) as duration_diff,"
			+ "    t.description as description,"
			+ "    t.notes as notes"
			+ "  FROM Result b JOIN Test t on b.testId = t.testId"
			+ "    LEFT JOIN Run r ON b.runId = r.runId"
			+ "    LEFT JOIN Result a ON (r.forwardRouteInd AND t.forwardResultId = a.resultId)"
			+ "      OR (NOT r.forwardRouteInd AND t.reverseResultId = a.resultId)"
			+ "  WHERE b.runId = :runId AND t.groupName = 'Custom'")
	List<Map> compareRunVsRef(@Param("runId") Integer runId, Pageable pageable);

	/*
	 * Query that gets the simple count of the above query 
	 */
	@Query(value ="SELECT count(*) "
			+ "  FROM Result b JOIN Test t on b.testId = t.testId"
			+ "    LEFT JOIN Run r ON b.runId = r.runId"
			+ "    LEFT JOIN Result a ON (r.forwardRouteInd AND t.forwardResultId = a.resultId)"
			+ "      OR (NOT r.forwardRouteInd AND t.reverseResultId = a.resultId)"
			+ "  WHERE b.runId = :runId AND t.groupName = 'Custom'")
	Integer compareRunVsRefCount(@Param("runId") Integer runId);
	
	
	/*
	 * Query that gets the main run table with sub-queries etc
	 */
	@Query(value ="SELECT r.runId as runId, r.forwardRouteInd as forwardRouteInd, "
		    + "  re.resultId as resultId, re.calcTime as calcTime, re.distance as distance, re.duration as duration, "
		    + " concat(e.platform, ' - ', e.environment) as environment, "
		    + " concat(d.roadSource, ' ', d.roadNetworkTimestamp, ' - ', d.description) as dataset, "
		    + " function('get_binary_partition_sig', re.partitionInfo ,'isTruckRoute') as partitionSignature"
		    + " FROM Result re "
		    + " JOIN Run r on re.runId = r.runId "
		    + " LEFT JOIN Dataset d ON r.datasetId = d.datasetId "
		    + " LEFT JOIN Environment e ON r.environmentId = e.environmentId "
		  	+ " WHERE re.testId = :testId ")
	List<Map> getResultListForTest(@Param("testId") int testId, Pageable pageable);
	
	
	//old partition parts of this: 			+ "    r.partitionSignature as partition_signature, r.partitionIndices as partition_indices, "
	@Query(value = "SELECT r.resultId as result_id, r.testId as test_id, r.runId as run_id, r.partitionInfo as partition_info, "
			+ "    distance as distance, duration as duration, r.calcTime as calc_time, "
			+ "    st_transform(r.routeGeometry,4326) as geometry, "
			+ "    t.points as points "
			+ "  FROM Result r "
			+ "  JOIN Test t ON r.testId = t.testId "
			+ "  WHERE resultId IN ( :resultIds )")
	List<Map<String,Object>> getGeoJsonByIds(@Param("resultIds") List<Integer> resultIds);
	
	
	@Query(value = "SELECT r.runId as runId, r.forwardRouteInd as forwardRouteInd, "
            + " re.resultId as resultId, re.calcTime as calcTime, re.distance as distance, re.duration as duration, "
            + " concat(e.platform, ' - ', e.environment) as environment, "
            + " concat(d.roadSource, ' ', d.roadNetworkTimestamp, ' - ', d.description) as dataset, "
            + " function('get_binary_partition_sig', re.partitionInfo, 'isTruckRoute') as partitionSignature, "
            + " re.testId as testId "
            + " FROM Result re "
            + " JOIN Run r on re.runId = r.runId "
            + " LEFT JOIN Dataset d ON r.datasetId = d.datasetId "
            + " LEFT JOIN Environment e ON r.environmentId = e.environmentId ")
	List<Map> findAllCustom(Pageable pageable);

	
}
