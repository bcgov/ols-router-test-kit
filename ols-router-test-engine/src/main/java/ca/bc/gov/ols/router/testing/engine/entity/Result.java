package ca.bc.gov.ols.router.testing.engine.entity;

import org.locationtech.jts.geom.Geometry;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.n52.jackson.datatype.jts.GeometrySerializer;
import org.n52.jackson.datatype.jts.GeometryDeserializer;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Entity
@Table(name = "results")
public class Result{

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "result_id", unique = true, nullable = false)
	 @SequenceGenerator(name="identifier", sequenceName="results_result_id_seq", allocationSize=1)
	 @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="identifier")
	 @Nonnull
	 @JsonView(View.Default.class)
	int resultId;
	
	@Column(name="run_id")
	@JsonView(View.Default.class)
	Integer runId;
	
	@Column(name="test_id")
	@JsonView(View.Default.class)
	Integer testId;
	
	@Column(name="calc_time")
	@JsonView(View.Default.class)
	Double calcTime;
	
	@JsonView(View.Default.class)
	Double distance;
	
	@JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
	@Column(name="route_geometry")
	Geometry routeGeometry;
	
	@JsonView(View.Default.class)
	Double duration;
	
	@Column(name="partition_signature")
	@JsonView(View.Default.class)
	String partitionSignature;
	
	@Column(name="partition_indices")
	@JsonView(View.Default.class)
	String partitionIndices;

	
	public Result() {}

	public Result(Integer runId, Integer testId, Double calcTime, Double distance, Geometry routeGeometry,
			Double duration, String partitionSignature, String partitionIndices) {
		super();
		this.runId = runId;
		this.testId = testId;
		this.calcTime = calcTime;
		this.distance = distance;
		this.routeGeometry = routeGeometry;
		this.duration = duration;
		this.partitionSignature = partitionSignature;
		this.partitionIndices = partitionIndices;
	}

	public int getResultId() {
		return resultId;
	}

	public void setResultId(int resultId) {
		this.resultId = resultId;
	}

	public Integer getRunId() {
		return runId;
	}

	public void setRunId(Integer runId) {
		this.runId = runId;
	}

	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	public Double getCalcTime() {
		return calcTime;
	}

	public void setCalcTime(Double calcTime) {
		this.calcTime = calcTime;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}


	public Geometry getRouteGeometry() {
		return routeGeometry;
	}

	public void setRouteGeometry(Geometry routeGeometry) {
		this.routeGeometry = routeGeometry;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public String getPartitionSignature() {
		return partitionSignature;
	}

	public void setPartitionSignature(String partitionSignature) {
		this.partitionSignature = partitionSignature;
	}

	public String getPartitionIndices() {
		return partitionIndices;
	}

	public void setPartitionIndices(String partitionIndices) {
		this.partitionIndices = partitionIndices;
	}
	
}