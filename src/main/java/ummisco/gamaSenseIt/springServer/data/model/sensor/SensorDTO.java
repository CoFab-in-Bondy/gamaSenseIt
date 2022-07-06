package ummisco.gamaSenseIt.springServer.data.model.sensor;

public class SensorDTO {
    private long sensorMetadataId;
    private String name;
    private String indications;
    private Double longitude;
    private Double latitude;
    private String hiddenMessage;
    private Boolean isHidden;
    private String description;
    private String maintenanceDescription;

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

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
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

    public boolean isHidden() {
        return isHidden != null && isHidden; // prevent default value as null
    }

    public void setHidden(Boolean hidden) {
        isHidden = hidden;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaintenanceDescription() {
        return maintenanceDescription;
    }

    public void setMaintenanceDescription(String maintenanceDescription) {
        this.maintenanceDescription = maintenanceDescription;
    }
}
