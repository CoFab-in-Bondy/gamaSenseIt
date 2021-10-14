package ummisco.gamaSenseIt.springServer.data.model;

import com.vividsolutions.jts.geom.Point;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class Sensor {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sensor")
    private final Set<SensoredBulkData> bulkData = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idSensor;
    private String name;
    private String displayName;
    private String subDisplayName;
    private double longitude;
    private double latitude;
    private boolean isHidden;
    private String hiddenMessage;
    @ManyToOne
    private SensorMetadata sensorType;

    public Sensor() {
        name = "";
    }

    public Sensor(String sensorName, String displayName, Point location, SensorMetadata sensorType) {
        this(sensorName, displayName, location.getX(), location.getY(), sensorType);
    }

    public Sensor(String sensorName, String displayName, String subDisplayName, Point location, SensorMetadata sensorType) {
        this(sensorName, displayName, subDisplayName, location.getX(), location.getY(), sensorType);
    }

    public Sensor(String sensorName, String displayName, double locationX, double locationY, SensorMetadata sensorType) {
        this();
        this.longitude = locationX;
        this.latitude = locationY;
        this.name = sensorName;
        this.displayName = displayName;
        this.sensorType = sensorType;
        this.isHidden = false;
    }

    public Sensor(String sensorName, String displayName, String subDisplayName, double locationX, double locationY,
                  SensorMetadata sensorType) {
        this();
        this.name = sensorName;
        this.displayName = displayName;
        this.subDisplayName = subDisplayName;
        this.longitude = locationX;
        this.latitude = locationY;
        this.sensorType = sensorType;
        this.isHidden = false;
    }

    // TODO description not used
    public Sensor(String sensorName, String displayName, String subDisplayName, double locationX, double locationY,
                  SensorMetadata sensorType, String description) {
        this();
        this.name = sensorName;
        this.displayName = displayName;
        this.subDisplayName = subDisplayName;
        this.longitude = locationX;
        this.latitude = locationY;
        this.sensorType = sensorType;
        this.isHidden = false;
    }

    public Long getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(Long idSensor) {
        this.idSensor = idSensor;
    }

    public String getName() {
        return name;
    }

    public void setName(String sensorName) {
        this.name = sensorName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public SensorMetadata getMetadata() {
        return sensorType;
    }

    public void setMetadata(SensorMetadata sensorType) {
        this.sensorType = sensorType;
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

    public Optional<Set<ParameterMetadata>> getParameters() {
        return sensorType == null ?
                Optional.empty() :
                Optional.of(this.sensorType.getParameterMetadata());
    }

    public Optional<ParameterMetadata> getParameterMetadata(long id) {
        if (this.sensorType == null)
            return Optional.empty();
        return this.sensorType.getParameterMetadata(id);
    }

    public String getSubDisplayName() {
        return subDisplayName;
    }

    public void setSubDisplayName(String subDisplayName) {
        this.subDisplayName = subDisplayName;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public String getHiddenMessage() {
        return hiddenMessage;
    }

    public void setHiddenMessage(String hiddenMessage) {
        this.hiddenMessage = hiddenMessage;
    }
}
