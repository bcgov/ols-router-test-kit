package ca.bc.gov.ols.router.testing.engine.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
	@Column(name="forward_route")
	Boolean forwardRoute;
	@Column(name="run_date")
	LocalDate runDate;
	@Column(name="group_name")
	String groupName;
	
	@JdbcTypeCode(SqlTypes.JSON)
	JsonNode parameters;

	@Column(name="queued_datetime")
	LocalDateTime queuedDateTime;
	String status;
	
	
	public Run() {}


	public Run(int runId, String description, Integer datasetId, Integer environmentId, Boolean forwardRoute,
			LocalDate runDate, String groupName, JsonNode parameters) {
		super();
		this.runId = runId;
		this.description = description;
		this.datasetId = datasetId;
		this.environmentId = environmentId;
		this.forwardRoute = forwardRoute;

		this.runDate = runDate;
		this.groupName = groupName;
		this.parameters = parameters;
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


	public Integer getEnvironmentId() {
		return environmentId;
	}


	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}


	public Boolean getForwardRoute() {
		return forwardRoute;
	}


	public void setForwardRoute(Boolean forwardRoute) {
		this.forwardRoute = forwardRoute;
	}


	public LocalDate getRunDate() {
		return runDate;
	}


	public void setRunDate(LocalDate runDate) {
		this.runDate = runDate;
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

	public Map getParameterCopy() {
		ObjectMapper mapper =new ObjectMapper();
		Map<String, Object> map = mapper.convertValue(this.parameters, Map.class);
		return map;
	}

	public void setParameters(JsonNode extraParameters) {
		this.parameters = extraParameters;
	}
	
	public void setParameters(Map extraParameters) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonParameters =  mapper.convertValue(extraParameters, JsonNode.class);
		this.parameters = jsonParameters;
	}
	
	public LocalDateTime getQueuedDateTime() {
		return queuedDateTime;
	}


	public void setQueuedDate(LocalDateTime queuedDateTime) {
		this.queuedDateTime = queuedDateTime;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

}