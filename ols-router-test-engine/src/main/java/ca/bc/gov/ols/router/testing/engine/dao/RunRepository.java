package ca.bc.gov.ols.router.testing.engine.dao;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.data.repository.query.Param;
import ca.bc.gov.ols.router.testing.engine.entity.Run;
import jakarta.annotation.Resource;
import jakarta.persistence.Column;

@Resource
@Component
public interface RunRepository extends JpaRepository<Run, Integer> {

	List<Run> findByStatusOrderByQueuedTimestamp(String string);
	@Query("SELECT cv.versionNum " +
	           "FROM Run r JOIN CodeVersion cv ON r.codeId = cv.codeId " +
	           "WHERE r.runId IN :runIds")
	List<String> findVersionNumsByRunIds(@Param("runIds") List<Integer> runIds);

	
	/*
	 * Query that gets the main run table with sub-queries etc
	 */
	@Query(value ="SELECT r.runId as runId , r.description as description, r.datasetId as datasetId, r.forwardRouteInd as forwardRouteInd, "
			+ "    r.environmentId as environmentId, r.runTimestamp as runTimestamp, r.groupName as groupName, "
			+ "    r.parameters as parameters, r.queuedTimestamp as queuedTimestamp, r.status as status, "
			+ "    concat(e.environment, ' ', e.platform) as environment,"
			+ "    concat(d.roadSource, ' ', d.roadNetworkTimestamp, ' - ', d.description) as dataset,"
			+ "    rc.c as testCount"
			+ "  FROM Run r"
			+ "  LEFT JOIN Dataset d ON r.datasetId = d.datasetId"
			+ "  LEFT JOIN Environment e ON r.environmentId = e.environmentId"
			+ "  LEFT JOIN (SELECT res.runId as resRunId, count(*) as c FROM Result res GROUP BY res.runId) as rc ON r.runId = rc.resRunId")
	List<Map> getRunList(Pageable pageable);
	
	/*
	 * Query that gets the total count of the above query 
	 */
	@Query(value ="SELECT count(*) as count"
			+ "  FROM Run r"
			+ "  LEFT JOIN Dataset d ON r.datasetId = d.datasetId"
			+ "  LEFT JOIN Environment e ON r.environmentId = e.environmentId"
			+ "  LEFT JOIN (SELECT res.runId as resRunId, count(*) as c FROM Result res GROUP BY res.runId) as rc ON r.runId = rc.resRunId")
	int getRunListCount();

}
