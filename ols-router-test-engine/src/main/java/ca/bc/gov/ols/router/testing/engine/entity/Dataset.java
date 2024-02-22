package ca.bc.gov.ols.router.testing.engine.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "datasets")
public class Dataset{

	@Id
	@SequenceGenerator(name="datasetidentifier", sequenceName="datasets_dataset_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="datasetidentifier")
	int datasetId;
	
	@Column(name="bc_subset_ind")
	Boolean isBcSubsetInd;
	@Column(name="road_source")
	String roadSource;
	@Column(name="road_network_timestamp")
	ZonedDateTime roadNetworkTimestamp;
	String description;
	
	public Dataset() {}
	
	public Dataset(int datasetId, Boolean isBcSubsetInd, String roadSource, ZonedDateTime roadNetworkTimestamp, String description) {
		super();
		this.datasetId = datasetId;
		this.isBcSubsetInd = isBcSubsetInd;
		this.roadSource = roadSource;
		this.roadNetworkTimestamp = roadNetworkTimestamp;
		this.description = description;
	}
	public int getDatasetId() {
		return datasetId;
	}
	public void setDatasetId(int datasetId) {
		this.datasetId = datasetId;
	}
	public Boolean getIsBcSubsetInd() {
		return isBcSubsetInd;
	}
	public void setIsBcSubsetInd(Boolean isBcSubsetInd) {
		this.isBcSubsetInd = isBcSubsetInd;
	}
	public String getRoadSource() {
		return roadSource;
	}
	public void setRoadSource(String roadSource) {
		this.roadSource = roadSource;
	}
	
	public ZonedDateTime getRoadNetworkTimestamp() {
		return roadNetworkTimestamp;
	}

	public void setRoadNetworkTimestamp(ZonedDateTime roadNetworkTimestamp) {
		this.roadNetworkTimestamp = roadNetworkTimestamp;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}