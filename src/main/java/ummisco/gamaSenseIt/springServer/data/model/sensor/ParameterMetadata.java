package ummisco.gamaSenseIt.springServer.data.model.sensor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ummisco.gamaSenseIt.springServer.data.model.IView;

import javax.persistence.*;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "parameter_metadata",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"sensor_metadata_id", "idx"})})
public class ParameterMetadata implements Comparable<ParameterMetadata> {

    private static final Logger logger = LoggerFactory.getLogger(ParameterMetadata.class);

    // ----- parameter_metadata_id ----- //

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonProperty("id")
    @JsonView(IView.Public.class)
    private Long id;

    // ----- name ----- //

    @Column(name = "name")
    @JsonProperty("name")
    @JsonView(IView.Public.class)
    private String name;

    // ----- unit ----- //

    @Column(name = "unit")
    @JsonProperty("unit")
    @JsonView(IView.Public.class)
    private String unit;

    // ----- sensor_metadata ----- //

    @JoinColumn(name = "sensor_metadata_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private SensorMetadata sensorMetadata;

    @Column(name = "sensor_metadata_id", nullable = false, insertable = false, updatable = false)
    @JsonProperty("sensorMetadataId")
    @JsonView(IView.Public.class)
    private Long sensorMetadataId;

    // ----- type ----- //

    @Column(name = "data_type")
    @JsonProperty("dataType")
    @JsonView(IView.Public.class)
    private DataFormat dataType;

    // ----- data_parameter ----- //

    @Column(name = "depreciated_parameter")
    @JsonProperty("depreciatedParameter")
    @JsonView(IView.Public.class)
    private DataParameter depreciatedParameter;

    // ----- icon ----- //

    @Column(name = "icon")
    @JsonProperty("icon")
    @JsonView(IView.Public.class)
    private String icon = "";

    // ----- idx ----- //

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idx")
    @JsonProperty("idx")
    @JsonView(IView.Public.class)
    private Integer idx;

    // ----- parameters ----- //

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parameterMetadata")
    @JsonIgnore
    private Set<Parameter> parameters = new HashSet<>();

    public ParameterMetadata() {
    } // for JSON compatibility

    public ParameterMetadata(String name, String unit, DataFormat type) {
        this.name = name;
        this.unit = unit;
        this.dataType = type;
        this.setIconFromParameter();
    }

    public ParameterMetadata(String name, String unit, DataFormat type, String icon) {
        this.name = name;
        this.unit = unit;
        this.dataType = type;
        this.icon = icon;
    }

    private void setIconFromParameter() {
        icon = switch (this.depreciatedParameter) {
            case TEMPERATURE -> "fas fa-thermometer-three-quarters";
            case PM10, PM2_5, PM1 -> "fab fa-cloudversify";
            case HUMIDITY -> "fas fa-tint";
            default -> "";
        };
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    @JsonView(IView.SensorMetadataOfParameterMetadata.class)
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

    public DataFormat getDataType() {
        return dataType;
    }

    public void setDataType(DataFormat dataType) {
        this.dataType = dataType;
    }

    public DataParameter getDepreciatedParameter() {
        return depreciatedParameter;
    }

    public void setDepreciatedParameter(DataParameter depreciatedParameter) {
        this.depreciatedParameter = depreciatedParameter;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @JsonView(IView.ParametersOfParameterMetadata.class)
    @JsonProperty("parameters")
    public Set<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Set<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Parameter createParameter(String message, Date captureDate, Sensor s) {
        return getDataType() == null ? null : getDataType().createParameterFromMorsel(message, captureDate, this, s);
    }

    @Override
    public int compareTo(ParameterMetadata o) {
        long idx = getIdx() == null ? Long.MAX_VALUE : getIdx();
        long oIdx = o.getIdx() == null ? Long.MAX_VALUE : o.getIdx();
        int cmp = Long.compare(idx, oIdx);
        if (cmp != 0)
            return Long.compare(getId(), o.getId());
        else
            return cmp;
    }


    public enum DataParameter {
        TEMPERATURE,
        CO2,
        PM10,
        PM2_5,
        PM1,
        PRESSURE,
        HUMIDITY
    }

    public enum DataFormat {
        // abbreviation <=> ordinal()
        INTEGER, DOUBLE, STRING;

        public Object convertToObject(byte[] data) {
            if (data == null) return null;
            ByteBuffer buffer = ByteBuffer.wrap(data);
            Object obj = switch (ordinal()) {
                case 0 -> buffer.getInt();
                case 1 -> buffer.getDouble();
                case 2 -> new String(data);
                default -> null;
            };
            if (buffer.hasRemaining() && ordinal() != 2)
                logger.warn("Cast an object who could not use all these bytes");
            return obj;
        }

        public String convertToString(byte[] data) {
            var obj = convertToObject(data);
            return obj == null ? "" : obj.toString();
        }

        public Parameter createParameterFromMorsel(String morsel, Date captureDate, ParameterMetadata pmd, Sensor s) {
            return switch (ordinal()) {
                case 0 -> new Parameter(Long.parseLong(morsel), captureDate, pmd, s);
                case 1 -> new Parameter("NAN".equalsIgnoreCase(morsel.strip()) ? Double.NaN : Double.parseDouble(morsel), captureDate, pmd, s);
                case 2 -> new Parameter(morsel, captureDate, pmd, s);
                default -> null;
            };
        }
    }
}
