package ummisco.gamaSenseIt.springServer.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.vividsolutions.jts.geom.Point;
import ummisco.gamaSenseIt.springServer.data.classes.Node;

import javax.persistence.*;
import java.util.*;

@Entity
public class Sensor {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sensor")
    @JsonIgnore
    private final Set<SensoredBulkData> bulkData = new HashSet<>();

    // ----- name ----- //
    // ----- sensor_id ----- //
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonProperty("id")
    @JsonView(IView.Public.class)
    private Long id;


    // ----- displayName ----- //
    @Column(name = "name")
    @JsonProperty("name")
    @JsonView(IView.Public.class)
    private String name;

    // ----- name ----- //
    @Column(name = "display_name")
    @JsonProperty("displayName")
    @JsonView(IView.Public.class)
    private String displayName;

    // ----- longitude ----- //
    @Column(name = "sub_display_name")
    @JsonProperty("subDisplayName")
    @JsonView(IView.Public.class)
    private String subDisplayName;

    // ----- longitude ----- //
    @Column(name = "longitude")
    @JsonProperty("longitude")
    @JsonView(IView.Public.class)
    private double longitude;

    // ----- longitude ----- //
    @Column(name = "latitude")
    @JsonProperty("latitude")
    @JsonView(IView.Public.class)
    private double latitude;
    @Column(name = "is_hidden")
    @JsonProperty("isHidden")
    @JsonView(IView.Public.class)
    private boolean isHidden;

    // ----- sensorMetadata ---- //
    @Column(name = "hidden_message")
    @JsonProperty("hiddenMessage")
    @JsonView(IView.Public.class)
    private String hiddenMessage;
    @JoinColumn(name = "sensor_metadata_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private SensorMetadata sensorMetadata;

    // ----- parameters ----- //
    @Column(name = "sensor_metadata_id", nullable = false, insertable = false, updatable = false)
    @JsonProperty("sensorMetadataId")
    @JsonView(IView.Public.class)
    private Long sensorMetadataId;

    // ----- bulk_data ----- //
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sensor")
    @JsonIgnore
    private Set<Parameter> parameters = new HashSet<>();

    public Sensor() {
    } // for JSON compatibility

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
        this.sensorMetadata = sensorType;
        this.isHidden = false;
    }

    @JsonView(IView.Public.class)
    @JsonProperty("state")
    public State state() {
        var now = new Date().getTime();
        var oneHourAgo = new Date(now - 60 * 60 * 1000);
        var lastRecv = new Date(0);
        for (var p : getParameters()) {
            if (p.getCaptureDate().after(oneHourAgo))
                return State.ACTIVE;
            if (p.getCaptureDate().after(lastRecv))
                lastRecv = p.getCaptureDate();
        }
        var oneDayAgo = new Date(now - 24 * 60 * 60 * 1000);
        return lastRecv.before(oneDayAgo) ? State.DEAD : State.NO_SIGNAL;
    }



    public Node toNode() {
        return new Node(){{
            put("id", getId());
            put("name", getName());
            put("displayName", getDisplayName());
            put("subDisplayName", getSubDisplayName());
            put("latitude", getLatitude());
            put("longitude", getLongitude());
            put("metadata", getSensorMetadata().toNode());
        }};
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public String getHiddenMessage() {
        return hiddenMessage;
    }

    public void setHiddenMessage(String hiddenMessage) {
        this.hiddenMessage = hiddenMessage;
    }


    @JsonView(IView.SensorMetadataOfSensor.class)
    @JsonProperty("sensorMetadata")
    public SensorMetadata getSensorMetadata() {
        return sensorMetadata;
    }

    public void setSensorMetadata(SensorMetadata sensorMetadata) {
        this.sensorMetadata = sensorMetadata;
    }

    public Long getSensorMetadataId() {
        return sensorMetadataId;
    }

    public void setSensorMetadataId(Long sensorMetadataId) {
        this.sensorMetadataId = sensorMetadataId;
    }

    @JsonView(IView.ParametersOfSensor.class)
    @JsonProperty("parameters")
    public Set<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Set<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Set<SensoredBulkData> getBulkData() {
        return bulkData;
    }
}
