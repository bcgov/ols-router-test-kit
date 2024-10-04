package ca.bc.gov.old.router.testing.web.utilities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteResponse {

    private ZonedDateTime roadNetworkTimestamp;

    // Additional fields from the JSON response
    private String routeDescription;
    private int executionTime;
    private String version;
    private String disclaimer;
    private String privacyStatement;
    private String copyrightNotice;
    private String copyrightLicense;
    private int srsCode;
    private String criteria;
    private String enable;
    private String distanceUnit;
    private ZonedDateTime dataProcessingTimestamp;
    private boolean routeFound;
    private double distance;
    private double time;
    private String timeText;
    private List<List<Double>> points;
    private List<List<Double>> route;
    private List<Object> notifications; // Assuming notifications are objects
    private List<Direction> directions; // Assuming directions is a list of Direction objects

    // Getters and Setters
    public ZonedDateTime getRoadNetworkTimestamp() {
        return roadNetworkTimestamp;
    }

    public void setRoadNetworkTimestamp(ZonedDateTime roadNetworkTimestamp) {
        this.roadNetworkTimestamp = roadNetworkTimestamp;
    }

    // Getters and Setters for additional fields
    public String getRouteDescription() {
        return routeDescription;
    }

    public void setRouteDescription(String routeDescription) {
        this.routeDescription = routeDescription;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getPrivacyStatement() {
        return privacyStatement;
    }

    public void setPrivacyStatement(String privacyStatement) {
        this.privacyStatement = privacyStatement;
    }

    public String getCopyrightNotice() {
        return copyrightNotice;
    }

    public void setCopyrightNotice(String copyrightNotice) {
        this.copyrightNotice = copyrightNotice;
    }

    public String getCopyrightLicense() {
        return copyrightLicense;
    }

    public void setCopyrightLicense(String copyrightLicense) {
        this.copyrightLicense = copyrightLicense;
    }

    public int getSrsCode() {
        return srsCode;
    }

    public void setSrsCode(int srsCode) {
        this.srsCode = srsCode;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public ZonedDateTime getDataProcessingTimestamp() {
        return dataProcessingTimestamp;
    }

    public void setDataProcessingTimestamp(ZonedDateTime dataProcessingTimestamp) {
        this.dataProcessingTimestamp = dataProcessingTimestamp;
    }

    public boolean isRouteFound() {
        return routeFound;
    }

    public void setRouteFound(boolean routeFound) {
        this.routeFound = routeFound;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public List<List<Double>> getPoints() {
        return points;
    }

    public void setPoints(List<List<Double>> points) {
        this.points = points;
    }

    public List<List<Double>> getRoute() {
        return route;
    }

    public void setRoute(List<List<Double>> route) {
        this.route = route;
    }

    public List<Object> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Object> notifications) {
        this.notifications = notifications;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    // Inner class for directions
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Direction {
        private String type;
        private String name;
        private double distance;
        private double time;
        private String text;
        private List<Double> point;

        // Getters and Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<Double> getPoint() {
            return point;
        }

        public void setPoint(List<Double> point) {
            this.point = point;
        }
    }
}

