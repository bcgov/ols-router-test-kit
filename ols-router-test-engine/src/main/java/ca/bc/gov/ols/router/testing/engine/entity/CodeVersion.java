package ca.bc.gov.ols.router.testing.engine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "code_versions")
public class CodeVersion{

	@Id
	@SequenceGenerator(name="identifier", sequenceName="code_versions_code_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="identifier")
	@Column(name="code_id")
	int codeId;
	
	@Column(name="github_commit_id")
	String githubCommitId;
	@Column(name="version_num")
	String versionNum;
	String description;
	
	
	
	
	
	public CodeVersion(int codeId, String githubCommitId, String versionNum, String description) {
		super();
		this.codeId = codeId;
		this.githubCommitId = githubCommitId;
		this.versionNum = versionNum;
		this.description = description;
	}

	public CodeVersion() {}

	public int getCodeId() {
		return codeId;
	}

	public void setCodeId(int codeId) {
		this.codeId = codeId;
	}

	public String getGithubCommitId() {
		return githubCommitId;
	}

	public void setGithubCommitId(String githubCommitId) {
		this.githubCommitId = githubCommitId;
	}



	public String getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}