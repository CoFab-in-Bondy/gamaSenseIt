package ummisco.gamaSenseIt.springServer.data.model;

import com.vividsolutions.jts.geom.Point;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class Sensor implements IConvertible<DisplayableSensor> {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sensor")
    private final Set<SensoredBulkData> bulkData = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long sensorId;
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

    public Sensor(String sensorName, String displayName, Point location, SensorMetadata sensorMetadata) {
        this(sensorName, displayName, location.getX(), location.getY(), sensorMetadata);
    }

    public Sensor(String sensorName, String displayName, String subDisplayName, Point location, SensorMetadata sensorMetadata) {
        this(sensorName, displayName, subDisplayName, location.getX(), location.getY(), sensorMetadata);
    }

    public Sensor(String sensorName, String displayName, double locationX, double locationY, SensorMetadata sensorMetadata) {
        this(sensorName, displayName, null, locationX, locationY, sensorMetadata);
    }

    public Sensor(String sensorName, String displayName, String subDisplayName, double locationX, double locationY,
                  SensorMetadata sensorType) {
        this.name = sensorName;
        this.displayName = displayName;
        this.subDisplayName = subDisplayName;
        this.longitude = locationX;
        this.latitude = locationY;
        this.sensorType = sensorType;
        this.isHidden = false;
    }

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
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
                Optional.of(this.sensorType.getParameterMetadataById());
    }

    public Optional<ParameterMetadata> getParameterMetadata(long id) {
        if (this.sensorType == null)
            return Optional.empty();
        return this.sensorType.getParameterMetadataById(id);
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

    @Override
    public DisplayableSensor convert() {
        return new DisplayableSensor(this);
    }
}
