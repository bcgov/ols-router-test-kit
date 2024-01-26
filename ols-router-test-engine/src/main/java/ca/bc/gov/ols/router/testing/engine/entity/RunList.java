package ca.bc.gov.ols.router.testing.engine.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
 * A view of Runs that also includes a few joined columns and one computed column
 * Used so that we can easily page and sort a GUI view with all this data in one table. 
 */

@Immutable
@Entity
@Table(name = "runlist")
public class RunList{


	//Columns from Run Class type
	@Id
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
	
	
	
	//Extra columns for this view
	String environment;

	@Column(name="test_count")
	Integer testCount;

	String dataset;

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

	public void setParameters(JsonNode parameters) {
		this.parameters = parameters;
	}

	public LocalDateTime getQueuedDateTime() {
		return queuedDateTime;
	}

	public void setQueuedDateTime(LocalDateTime queuedDateTime) {
		this.queuedDateTime = queuedDateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public Integer getTestCount() {
		return testCount;
	}

	public void setTestCount(Integer testCount) {
		this.testCount = testCount;
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	

}