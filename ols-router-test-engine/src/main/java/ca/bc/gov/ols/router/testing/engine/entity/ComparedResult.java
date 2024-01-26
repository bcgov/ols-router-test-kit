package ca.bc.gov.ols.router.testing.engine.entity;


public class ComparedResult{

	Integer resultIdA;
	Integer testIdA;
	Double calcTimeA;
	Double distanceA;
	Double durationA;
	String partitionSignatureA;
	
	Integer resultIdB;
	Integer testIdB;
	Double calcTimeB;
	Double distanceB;
	Double durationB;
	String partitionSignatureB;
	
	Integer distanceDifference;
	Integer distancePercent;
	Integer durationDifference;
	Integer durationPercent;
	Integer calcTimeDifference;
	Integer calcTimePercent;
	Integer partitionDifference;
	
	String description;
	String notes;

	
	public ComparedResult() {}

	
	
	public ComparedResult(Integer resultIdA, Integer testIdA, Double calcTimeA, Double distanceA, Double durationA,
			String partitionSignatureA, Integer resultIdB, Integer testIdB, Double calcTimeB, Double distanceB,
			Double durationB, String partitionSignatureB, Integer distanceDifference, Integer distancePercent,
			Integer durationDifference, Integer durationPercent, Integer calcTimeDifference, Integer calcTimePercent,
			Integer partitionDifference, String description, String notes) {
		super();
		this.resultIdA = resultIdA;
		this.testIdA = testIdA;
		this.calcTimeA = calcTimeA;
		this.distanceA = distanceA;
		this.durationA = durationA;
		this.partitionSignatureA = partitionSignatureA;
		this.resultIdB = resultIdB;
		this.testIdB = testIdB;
		this.calcTimeB = calcTimeB;
		this.distanceB = distanceB;
		this.durationB = durationB;
		this.partitionSignatureB = partitionSignatureB;
		this.distanceDifference = distanceDifference;
		this.distancePercent = distancePercent;
		this.durationDifference = durationDifference;
		this.durationPercent = durationPercent;
		this.calcTimeDifference = calcTimeDifference;
		this.calcTimePercent = calcTimePercent;
		this.partitionDifference = partitionDifference;
		this.description = description;
		this.notes = notes;
	}



	public Integer getResultIdA() {
		return resultIdA;
	}


	public void setResultIdA(Integer resultIdA) {
		this.resultIdA = resultIdA;
	}


	public Integer getTestIdA() {
		return testIdA;
	}


	public void setTestIdA(Integer testIdA) {
		this.testIdA = testIdA;
	}


	public Double getCalcTimeA() {
		return calcTimeA;
	}


	public void setCalcTimeA(Double calcTimeA) {
		this.calcTimeA = calcTimeA;
	}


	public Double getDistanceA() {
		return distanceA;
	}


	public void setDistanceA(Double distanceA) {
		this.distanceA = distanceA;
	}


	public Double getDurationA() {
		return durationA;
	}


	public void setDurationA(Double durationA) {
		this.durationA = durationA;
	}


	public String getPartitionSignatureA() {
		return partitionSignatureA;
	}


	public void setPartitionSignatureA(String partitionSignatureA) {
		this.partitionSignatureA = partitionSignatureA;
	}


	public Integer getResultIdB() {
		return resultIdB;
	}


	public void setResultIdB(Integer resultIdB) {
		this.resultIdB = resultIdB;
	}


	public Integer getTestIdB() {
		return testIdB;
	}


	public void setTestIdB(Integer testIdB) {
		this.testIdB = testIdB;
	}


	public Double getCalcTimeB() {
		return calcTimeB;
	}


	public void setCalcTimeB(Double calcTimeB) {
		this.calcTimeB = calcTimeB;
	}


	public Double getDistanceB() {
		return distanceB;
	}


	public void setDistanceB(Double distanceB) {
		this.distanceB = distanceB;
	}


	public Double getDurationB() {
		return durationB;
	}


	public void setDurationB(Double durationB) {
		this.durationB = durationB;
	}


	public String getPartitionSignatureB() {
		return partitionSignatureB;
	}


	public void setPartitionSignatureB(String partitionSignatureB) {
		this.partitionSignatureB = partitionSignatureB;
	}


	public Integer getDistanceDifference() {
		return distanceDifference;
	}


	public void setDistanceDifference(Integer distanceDifference) {
		this.distanceDifference = distanceDifference;
	}


	public Integer getDistancePercent() {
		return distancePercent;
	}


	public void setDistancePercent(Integer distancePercent) {
		this.distancePercent = distancePercent;
	}


	public Integer getDurationDifference() {
		return durationDifference;
	}


	public void setDurationDifference(Integer durationDifference) {
		this.durationDifference = durationDifference;
	}


	public Integer getDurationPercent() {
		return durationPercent;
	}


	public void setDurationPercent(Integer durationPercent) {
		this.durationPercent = durationPercent;
	}


	public Integer getCalcTimeDifference() {
		return calcTimeDifference;
	}


	public void setCalcTimeDifference(Integer calcTimeDifference) {
		this.calcTimeDifference = calcTimeDifference;
	}


	public Integer getCalcTimePercent() {
		return calcTimePercent;
	}


	public void setCalcTimePercent(Integer calcTimePercent) {
		this.calcTimePercent = calcTimePercent;
	}


	public Integer getPartitionDifference() {
		return partitionDifference;
	}


	public void setPartitionDifference(Integer partitionDifference) {
		this.partitionDifference = partitionDifference;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getNotes() {
		return notes;
	}


	public void setNotes(String notes) {
		this.notes = notes;
	}


	
}