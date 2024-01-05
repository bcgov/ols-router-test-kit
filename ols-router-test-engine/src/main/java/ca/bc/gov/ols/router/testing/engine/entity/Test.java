package ca.bc.gov.ols.router.testing.engine.entity;

import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tests")
public class Test{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int testId;
	
	String description;
	@Column(name="group_name")
	String groupName;
	@Column(name="fwd_ref_result_id")
	Integer forwardResultId;
	@Column(name="rev_ref_result_id")
	Integer reverseResultId;
	String notes;
	@Column(name="good_demo_case")
	Boolean rungoodDemoCase;
	String points;
	@JdbcTypeCode(SqlTypes.JSON)
	JsonNode parameters; 
	
	
	public Test() {}


	public Test(int testId, String description, Integer datasetId, String groupName, Integer forwardResultId,
			Integer reverseResultId, String notes, Boolean rungoodDemoCase, String points, JsonNode parameters) {
		super();
		this.testId = testId;
		this.description = description;
		this.groupName = groupName;
		this.forwardResultId = forwardResultId;
		this.reverseResultId = reverseResultId;
		this.notes = notes;
		this.rungoodDemoCase = rungoodDemoCase;
		this.points = points;
		this.parameters = parameters;
	}


	public int getTestId() {
		return testId;
	}


	public void setTestId(int testId) {
		this.testId = testId;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getGroupName() {
		return groupName;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	public Integer getForwardResultId() {
		return forwardResultId;
	}


	public void setForwardResultId(Integer forwardResultId) {
		this.forwardResultId = forwardResultId;
	}


	public Integer getReverseResultId() {
		return reverseResultId;
	}


	public void setReverseResultId(Integer reverseResultId) {
		this.reverseResultId = reverseResultId;
	}


	public String getNotes() {
		return notes;
	}


	public void setNotes(String notes) {
		this.notes = notes;
	}


	public Boolean getRungoodDemoCase() {
		return rungoodDemoCase;
	}


	public void setRungoodDemoCase(Boolean rungoodDemoCase) {
		this.rungoodDemoCase = rungoodDemoCase;
	}


	public String getPoints() {
		return points;
	}
	
	public String getPointsReversed() {
		String[] elements = this.points.split(" ");
		List<String> fixedLenghtList = Arrays.asList(elements);
		ArrayList<String> list = new ArrayList<String>(fixedLenghtList);
		Collections.reverse(list);
		String revList = "";
		for (String e : list) {
			if ("".equals(revList)){ //don't add a space if this is the first one
				revList = e;
			}else {
				revList = revList + " " + e;
			}
		}
		return revList;
	}


	public void setPoints(String points) {
		this.points = points;
	}


	public JsonNode getParameters() {
		return parameters;
	}

	public Map getParameterCopy() {
		ObjectMapper mapper =new ObjectMapper();
		Map<String, Object> map = mapper.convertValue(this.parameters, Map.class);
		return map;
	}
	
	public void setParameters(Map parameters) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonParameters =  mapper.convertValue(parameters, JsonNode.class);
		this.parameters = jsonParameters;
	}
	public void setParameters(JsonNode parameters) {
		this.parameters = parameters;
	}


}