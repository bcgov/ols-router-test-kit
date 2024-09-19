package ca.bc.gov.ols.router.testing.engine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "environments")
public class Environment{

	@Id
	@SequenceGenerator(name="envidentifier", sequenceName="environments_env_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="envidentifier")
	@Column(name="env_id")
	int environmentId;
	
	
	String platform;
	String environment;
	@Column(name="base_api_url")
	String baseApiUrl;
	@Column(name="api_key")
	String apiKey;
	@Column(name="usable_as_map_platform")
	Boolean usableAsMapPlatform;
	
	public Environment() {}

	public Environment(int environmentId, String platform, String environment, String baseApiUrl, String apiKey) {
		super();
		this.environmentId = environmentId;
		this.platform = platform;
		this.environment = environment;
		this.baseApiUrl = baseApiUrl;
		this.apiKey = apiKey;
	}

	
	public int getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getBaseApiUrl() {
		return baseApiUrl;
	}

	public void setBaseApiUrl(String baseApiUrl) {
		this.baseApiUrl = baseApiUrl;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	public Boolean getUsableAsMapPlatform() {
		return usableAsMapPlatform;
	}

	public void setUsableAsMapPlatform(Boolean usableAsMapPlatform) {
		this.usableAsMapPlatform = usableAsMapPlatform;
	}

}