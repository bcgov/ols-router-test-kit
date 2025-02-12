package ca.bc.gov.ols.router.testing.engine.entity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;



@Entity
@Table(name = "runs")

public class Run{

	@Id
	@SequenceGenerator(name="identifier", sequenceName="runs_run_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="identifier")
	@Nonnull
	int runId;
	
	
	String description;
	@Column(name="dataset_id")
	Integer datasetId;
	@Column(name="env_id")
	Integer environmentId;
	@Column(name="forward_route_ind")
	Boolean forwardRouteInd;
	@Column(name="run_timestamp")
	ZonedDateTime runTimestamp;
	@Column(name="group_name")
	String groupName;
	@Column(name="code_id")
	Integer codeId;
	

	@JdbcTypeCode(SqlTypes.JSON)
	JsonNode parameters;

	@Column(name="queued_timestamp")
	ZonedDateTime queuedTimestamp;
	String status;
	
	
	public Run() {}




	public Run(int runId, String description, Integer datasetId, Integer environmentId, Boolean forwardRouteInd,
			ZonedDateTime runTimestamp, String groupName, JsonNode parameters, ZonedDateTime queuedTimestamp,
			String status) {
		super();
		this.runId = runId;
		this.description = description;
		this.datasetId = datasetId;
		this.environmentId = environmentId;
		this.forwardRouteInd = forwardRouteInd;
		this.runTimestamp = runTimestamp;
		this.groupName = groupName;
		this.parameters = parameters;
		this.queuedTimestamp = queuedTimestamp;
		this.status = status;
	}


	public int getRunId() {
		return runId;
	}


	public void setRunId(int runId) {
		this.runId = runId;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Integer getDatasetId() {
		return datasetId;
	}


	public void setDatasetId(Integer datasetId) {
		this.datasetId = datasetId;
	}

	public Integer getCodeId() {
		return codeId;
	}

	public void setCodeId(Integer codeId) {
		this.codeId = codeId;
	}

	public Integer getEnvironmentId() {
		return environmentId;
	}


	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}

	public Boolean getForwardRouteInd() {
		return forwardRouteInd;
	}


	public void setForwardRouteInd(Boolean forwardRouteInd) {
		this.forwardRouteInd = forwardRouteInd;
	}


	public String getGroupName() {
		return groupName;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	
	public JsonNode getParameters() {
		return parameters;
	}

	@JsonGetter("parameters")
	public Map getParameterCopy() {
		ObjectMapper mapper =new ObjectMapper();
		Map<String, Object> map = mapper.convertValue(this.parameters, Map.class);
		return map;
	}

	
	public void setParameters(JsonNode extraParameters) {
		this.parameters = extraParameters;
	}
	
	@JsonSetter("parameters")
	public void setParameters(Map parameters) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonParameters =  mapper.convertValue(parameters, JsonNode.class);
		this.parameters = jsonParameters;
	}
	

	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public ZonedDateTime getRunTimestamp() {
		return runTimestamp;
	}


	public void setRunTimestamp(ZonedDateTime runTimestamp) {
		this.runTimestamp = runTimestamp;
	}


	public ZonedDateTime getQueuedTimestamp() {
		return queuedTimestamp;
	}


	public void setQueuedTimestamp(ZonedDateTime queuedTimestamp) {
		this.queuedTimestamp = queuedTimestamp;
	}
	
	

}