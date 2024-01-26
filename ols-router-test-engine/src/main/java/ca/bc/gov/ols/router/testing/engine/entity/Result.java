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
	Geometry geom;
	
	@JsonView(View.Default.class)
	Double duration;
	
	@Column(name="partition_signature")
	@JsonView(View.Default.class)
	String partitionSignature;
	
	@Column(name="partition_indices")
	@JsonView(View.Default.class)
	String partitionIndices;

	
	public Result() {}
	
	public Result(int runId, int testId, double calcTime, double distance, Geometry geometry, double duration, String partitionSignature, String partitionIndices) {
	    this.runId = runId; 
	    this.testId = testId;
	    this.calcTime = calcTime;
	    this.distance = distance;
	    this.geom = geometry;
	    this.duration = duration;
	    this.partitionSignature = partitionSignature;
	    this.partitionIndices = partitionIndices;
	  }
	/*
	public Result(Object object1, Object object2, Object object3, Object object4, Object object5, Object object6,
			Object object7, Object object8) {
		this(Integer.parseInt((String) object1), Double.parseDouble((String) object2), Double.parseDouble((String) object3), object4, Double.parseDouble((String) object5), object6, object7, object8);
	}*/

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

	public Geometry getGeom() {
		return geom;
	}

	public void setGeom(Geometry geom) {
		this.geom = geom;
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