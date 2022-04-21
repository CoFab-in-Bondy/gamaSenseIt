package ummisco.gamaSenseIt.springServer.data.model.sensor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import ummisco.gamaSenseIt.springServer.data.classes.Node;
import ummisco.gamaSenseIt.springServer.data.model.IView;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class SensorMetadata {

    public final static String DEFAULT_DATA_SEPARATOR = ":";

    // ----- sensor_metadata_id ----- //

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonProperty("id")
    @JsonView(IView.Public.class)
    private Long id;

    // ----- version ----- //

    @Column(name = "version")
    @JsonProperty("version")
    @JsonView(IView.Public.class)
    private String version;

    // ----- name ----- //

    @Column(name = "name")
    @JsonProperty("name")
    @JsonView(IView.Public.class)
    private String name;

    // ----- data_separator ----- //

    @Column(name = "data_separator")
    @JsonProperty("dataSeparator")
    @JsonView(IView.Public.class)
    private String dataSeparator;

    // ----- description ----- //

    @Column(name = "description")
    @JsonProperty("description")
    @JsonView(IView.Public.class)
    private String description;

    // ----- parameterMetadata ----- //

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sensorMetadata")
    @JsonIgnore
    private Set<ParameterMetadata> parametersMetadata = new HashSet<>();


    // ----- sensor ----- //
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sensorMetadata")
    @JsonIgnore
    private Set<Sensor> sensors = new HashSet<>();

    // ----- sensor ----- //
    @Column(name = "icon")
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private byte[] icon;

    public SensorMetadata() {
    } // for JSON compatibility

    public SensorMetadata(String name, String version) {
        this(name, version, DEFAULT_DATA_SEPARATOR);
    }

    public SensorMetadata(String name, String version, String sep) {
        this(name, version, sep, "");
    }

    public SensorMetadata(String name, String version, String sep, String description) {
        this.version = version;
        this.name = name;
        this.dataSeparator = sep;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataSeparator() {
        return dataSeparator;
    }

    public void setDataSeparator(String dataSeparator) {
        this.dataSeparator = dataSeparator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonView(IView.ParametersMetadataOfSensorMetadata.class)
    @JsonProperty("parametersMetadata")
    public List<ParameterMetadata> getParametersMetadata() {
        List<ParameterMetadata> pmds = new ArrayList<>(parametersMetadata);
        pmds.sort(ParameterMetadata::compareTo);
        return pmds;
    }

    public Node toNode() {
        return new Node() {{
            put("id", getId());
            put("version", getVersion());
            put("name", getName());
            put("dataSeparator", getDataSeparator());
            put("description", getDescription());
        }};
    }

    public void setParametersMetadata(Set<ParameterMetadata> parameterMetadata) {
        this.parametersMetadata = parameterMetadata;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    @JsonView(IView.SensorsOfSensorMetadata.class)
    @JsonProperty("sensors")
    public Set<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }
}
