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
	 * Query that compares two runs to each other, calculating the differences in calctie, duration, distance etc. Still sortable and pagable with the standard Pageable type
	 */
	@Query(value ="SELECT a.resultId as a_result_id, a.testId as a_test_id, a.calcTime as a_calc_time, a.partitionSignature as a_partition_signature, a.distance as a_distance, a.duration as a_duration,"
			+ "b.resultId as b_result_id, b.calcTime as b_calc_time, b.distance as b_distance, b.duration as b_duration,"
			+ "b.partitionSignature as b_partition_signature, (b.distance - a.distance) as distance_diff, "
			+ "(100*(b.distance - a.distance)/greatest(a.distance,1)) as distance_perc,"
		    + "(b.duration - a.duration) as duration_diff,"
		    + "(100*(b.duration - a.duration)/greatest(a.duration,1)) as duration_perc,"
		    + "(b.calcTime - a.calcTime) as calc_time_diff,"
		    + "(100*(b.calcTime - a.calcTime)/greatest(a.calcTime, 1)) as calc_time_perc,"
		    + "CASE WHEN b.partitionSignature IS NOT DISTINCT FROM a.partitionSignature THEN 0 ELSE GREATEST(coalesce(length(b.partitionSignature),0) - coalesce(length(a.partitionSignature),0), 1) END as partition_diff,"
		    + "t.description as description, t.notes as notes"
		  + " FROM Result a JOIN Result b on a.testId = b.testId"
		  + "  JOIN Test t on b.testId = t.testId"
		  + " WHERE a.runId = :runIdA AND b.runId = :runIdB")
	List<Map> compareResultsOfRunIds(@Param("runIdA") Integer runIdA, @Param("runIdB") Integer runIdB, Pageable pageable);
	
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
		    + "  concat(e.platform, ' - ', e.environment) as environment, "
		    + "  concat(d.roadSource, ' ', d.roadNetworkTimestamp, ' - ', d.description) as dataset "
		    + " FROM Result re "
		    + " JOIN Run r on re.runId = r.runId "
		    + " LEFT JOIN Dataset d ON r.datasetId = d.datasetId "
		    + " LEFT JOIN Environment e ON r.environmentId = e.environmentId "
		  	+ " WHERE re.testId = :testId ")
	List<Map> getResultListForTest(@Param("testId") int testId, Pageable pageable);
	
	
	@Query(value = "SELECT resultId as result_id, testId as test_id, runId as run_id, distance, duration, calcTime as calc_time, "
			+ "    partitionSignature as partition_signature, partitionIndices as partition_indices, "
			+ "    st_transform(routeGeometry,4326) as geometry "
			+ "  FROM Result "
			+ "  WHERE resultId IN ( :resultIds )")
	List<Map<String,Object>> getGeoJsonByIds(@Param("resultIds") List<Integer> resultIds);
	
	
}
