package ca.bc.gov.old.router.testing.web.utilities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RouterStatusResponse {

    private String version;

    @JsonProperty("gitCommitId")
    private String gitCommitId;

    @JsonProperty("dataProcessingTimestamp")
    private ZonedDateTime dataProcessingTimestamp;

    @JsonProperty("roadNetworkTimestamp")
    private ZonedDateTime roadNetworkTimestamp;

    @JsonProperty("startTimestamp")
    private ZonedDateTime startTimestamp;

    @JsonProperty("rdmLastSuccessfulUpdate")
    private ZonedDateTime rdmLastSuccessfulUpdate;

    @JsonProperty("rdmLastFailedUpdate")
    private String rdmLastFailedUpdate; // Assuming it can be empty or null

    @JsonProperty("rdmSuccessfulUpdateCount")
    private int rdmSuccessfulUpdateCount;

    @JsonProperty("rdmFailedUpdateCount")
    private int rdmFailedUpdateCount;

    @JsonProperty("rdmLastRecordCount")
    private int rdmLastRecordCount;

    // Getters and setters
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGitCommitId() {
        return gitCommitId;
    }

    public void setGitCommitId(String gitCommitId) {
        this.gitCommitId = gitCommitId;
    }

    public ZonedDateTime getDataProcessingTimestamp() {
        return dataProcessingTimestamp;
    }

    public void setDataProcessingTimestamp(ZonedDateTime dataProcessingTimestamp) {
        this.dataProcessingTimestamp = dataProcessingTimestamp;
    }

    public ZonedDateTime getRoadNetworkTimestamp() {
        return roadNetworkTimestamp;
    }

    public void setRoadNetworkTimestamp(ZonedDateTime roadNetworkTimestamp) {
        this.roadNetworkTimestamp = roadNetworkTimestamp;
    }

    public ZonedDateTime getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(ZonedDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public ZonedDateTime getRdmLastSuccessfulUpdate() {
        return rdmLastSuccessfulUpdate;
    }

    public void setRdmLastSuccessfulUpdate(ZonedDateTime rdmLastSuccessfulUpdate) {
        this.rdmLastSuccessfulUpdate = rdmLastSuccessfulUpdate;
    }

    public String getRdmLastFailedUpdate() {
        return rdmLastFailedUpdate;
    }

    public void setRdmLastFailedUpdate(String rdmLastFailedUpdate) {
        this.rdmLastFailedUpdate = rdmLastFailedUpdate;
    }

    public int getRdmSuccessfulUpdateCount() {
        return rdmSuccessfulUpdateCount;
    }

    public void setRdmSuccessfulUpdateCount(int rdmSuccessfulUpdateCount) {
        this.rdmSuccessfulUpdateCount = rdmSuccessfulUpdateCount;
    }

    public int getRdmFailedUpdateCount() {
        return rdmFailedUpdateCount;
    }

    public void setRdmFailedUpdateCount(int rdmFailedUpdateCount) {
        this.rdmFailedUpdateCount = rdmFailedUpdateCount;
    }

    public int getRdmLastRecordCount() {
        return rdmLastRecordCount;
    }

    public void setRdmLastRecordCount(int rdmLastRecordCount) {
        this.rdmLastRecordCount = rdmLastRecordCount;
    }
}
