package ummisco.gamaSenseIt.springServer.data.model;

public class SensorDTO {
    private long sensorMetadataId;
    private String name;
    private String displayName;
    private String subDisplayName;
    private double longitude;
    private double latitude;
    private String hiddenMessage;
    private Boolean isHidden;

    public long getSensorMetadataId() {
        return sensorMetadataId;
    }

    public void setSensorMetadataId(long sensorMetadataId) {
        this.sensorMetadataId = sensorMetadataId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSubDisplayName() {
        return subDisplayName;
    }

    public void setSubDisplayName(String subDisplayName) {
        this.subDisplayName = subDisplayName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getHiddenMessage() {
        return hiddenMessage;
    }

    public void setHiddenMessage(String hiddenMessage) {
        this.hiddenMessage = hiddenMessage;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean hidden) {
        isHidden = hidden;
    }
}
