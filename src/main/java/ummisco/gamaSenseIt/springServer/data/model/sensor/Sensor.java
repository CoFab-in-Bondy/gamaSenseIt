package ummisco.gamaSenseIt.springServer.data.model.sensor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.vividsolutions.jts.geom.Point;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ummisco.gamaSenseIt.springServer.data.classes.Node;
import ummisco.gamaSenseIt.springServer.data.model.IView;
import ummisco.gamaSenseIt.springServer.data.model.ImageConstantes;
import ummisco.gamaSenseIt.springServer.data.model.preference.InteractBase;
import ummisco.gamaSenseIt.springServer.data.model.preference.InteractSensor;
import ummisco.gamaSenseIt.springServer.data.model.preference.Interactible;
import ummisco.gamaSenseIt.springServer.data.model.user.AccessSensor;

import javax.persistence.*;
import java.io.IOException;
import java.util.*;

@Entity
public class Sensor extends Interactible {

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

    // ----- isHidden ----- //
    @Column(name = "is_hidden")
    @JsonProperty("isHidden")
    @JsonView(IView.Public.class)
    private boolean isHidden;

    // ----- isHidden ----- //
    @Column(name = "notified")
    @JsonProperty("notified")
    @JsonView(IView.Public.class)
    private boolean notified;

    // ----- hiddenMessage ---- //
    @Column(name = "hidden_message")
    @JsonProperty("hiddenMessage")
    @JsonView(IView.Public.class)
    private String hiddenMessage;

    // ----- sensorMetadata ----- //
    @JoinColumn(name = "sensor_metadata_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private SensorMetadata sensorMetadata;

    @Column(name = "sensor_metadata_id", nullable = false, insertable = false, updatable = false)
    @JsonProperty("sensorMetadataId")
    @JsonView(IView.Public.class)
    private Long sensorMetadataId;

    // ----- lastCaptureDate ----- //
    @Column(name = "last_capture_date")
    @JsonProperty("lastCaptureDate")
    @JsonView(IView.Public.class)
    private Date lastCaptureDate;

    // ----- photo ----- //
    @Column(name = "photo")
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private byte[] photo;

    // ----- description ----- //
    @Column(name = "description", nullable = false)
    @JsonProperty("description")
    @JsonView(IView.Public.class)
    private String description;

    // ----- description ----- //
    @Column(name = "maintenance_description", nullable = false)
    @JsonProperty("maintenanceDescription")
    @JsonIgnore
    private String maintenanceDescription;

    // ----- parameters ----- //
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sensor")
    @JsonIgnore
    private Set<Parameter> parameters = new HashSet<>();

    // ----- interactSensor ----- //
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sensor")
    @JsonIgnore
    private final Set<InteractSensor> interacts = new HashSet<>();

    // ----- accessSensors ----- //
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sensor")
    @JsonIgnore
    private final Set<AccessSensor> accessSensors = new HashSet<>();

    public Sensor() {
    } // for JSON compatibility

    public void setLocation(Point location) {
        setLongitude(location.getX());
        setLatitude(location.getY());
    }

    public void setSensorMetadataId(Long sensorMetadataId) {
        this.sensorMetadataId = sensorMetadataId;
    }

    public Set<AccessSensor> getAccessSensors() {
        return accessSensors;
    }

    /*
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
    */

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public byte[] getPhoto() {
        return photo == null || photo.length == 0? ImageConstantes.NO_IMG: photo;


    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void setPhoto(MultipartFile photo) {
        try {
            this.photo = photo.getBytes();
        } catch (IOException err) {
            System.err.println("Invalid bytes");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid bytes");
        }
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

    public Date getLastCaptureDate() {
        return lastCaptureDate;
    }

    public void setLastCaptureDate(Date lastCaptureDate) {
        this.lastCaptureDate = lastCaptureDate;
    }

    public Node toNode(boolean manager) {
        return new Node() {{
            put("id", getId());
            put("name", getName());
            put("displayName", getDisplayName());
            put("subDisplayName", getSubDisplayName());
            put("latitude", getLatitude());
            put("longitude", getLongitude());
            put("metadata", getSensorMetadata().toNode());
            put("description", getDescription());
            put("isHidden", isHidden());
            put("hiddenMessage", getHiddenMessage());
            put("manageable", manager);
            if (manager)
                put("maintenanceDescription", getMaintenanceDescription());
        }};
    }

    public Node toNode() {
        return toNode(false);
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
        this.sensorMetadataId = this.sensorMetadata == null? null: this.sensorMetadata.getId();
    }

    public Long getSensorMetadataId() {
        return sensorMetadataId;
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

    @Override
    public String toString() {
        return String.format(
                "Sensor(%d, %s, (%.5f, %.5f), %s, %s)",
                id, name, longitude, latitude, notified? "N": "M", lastCaptureDate
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sensor sensor)) return false;
        return getId().equals(sensor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public Set<? extends InteractBase> getInteracts() {
        return interacts;
    }
}
