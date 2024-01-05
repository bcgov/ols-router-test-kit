package ca.bc.gov.ols.router.testing.engine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "datasets")
public class Dataset{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int datasetId;
	
	@Column(name="is_bc_subset")
	Boolean isBcSubset;
	@Column(name="road_source")
	String roadSource;
	@Column(name="road_vintage")
	String roadVintage;
	String description;
	
	public Dataset() {}
	
	public Dataset(int datasetId, Boolean isBcSubset, String roadSource, String roadVintage, String description) {
		super();
		this.datasetId = datasetId;
		this.isBcSubset = isBcSubset;
		this.roadSource = roadSource;
		this.roadVintage = roadVintage;
		this.description = description;
	}
	public int getDatasetId() {
		return datasetId;
	}
	public void setDatasetId(int datasetId) {
		this.datasetId = datasetId;
	}
	public Boolean getIsBcSubset() {
		return isBcSubset;
	}
	public void setIsBcSubset(Boolean isBcSubset) {
		this.isBcSubset = isBcSubset;
	}
	public String getRoadSource() {
		return roadSource;
	}
	public void setRoadSource(String roadSource) {
		this.roadSource = roadSource;
	}
	public String getRoadVintage() {
		return roadVintage;
	}
	public void setRoadVintage(String roadVintage) {
		this.roadVintage = roadVintage;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}