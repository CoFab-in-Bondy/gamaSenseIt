package ummisco.gamaSenseIt.springServer.data.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class SensorMetadata implements IConvertible<DisplayableSensorMetadata> {

    public final static String MEASURE_ORDER_SEPARATOR = ":";
    public final static String DEFAULT_DATA_SEPARATOR = ":";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sensorMetadataId;

    private String version;
    private String name;
    private String measuredDataOrder;
    private String dataSeparator;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "sensorMetadata")
    private Set<ParameterMetadata> parameterMetadata = new HashSet<>();

    public SensorMetadata() {
        super();
        measuredDataOrder = "";
        this.dataSeparator = DEFAULT_DATA_SEPARATOR;

    }

    public SensorMetadata(String name, String version) {
        this();
        this.version = version;
        this.name = name;
    }

    public SensorMetadata(String name, String version, String sep) {
        this();
        this.version = version;
        this.name = name;
        this.dataSeparator = sep;
    }

    public SensorMetadata(String name, String version, String sep, String description) {
        this();
        this.version = version;
        this.name = name;
        this.dataSeparator = sep;
        this.description = description;
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

    public void setName(String typeName) {
        this.name = typeName;
    }

    public Long getSensorMetadataId() {
        return sensorMetadataId;
    }

    public void setSensorMetadataId(Long sensorMetadataId) {
        this.sensorMetadataId = sensorMetadataId;
    }

    public Set<ParameterMetadata> getParameterMetadataById() {
        return parameterMetadata;
    }

    public void setParameterMetadata(Set<ParameterMetadata> parameterMetadata) {
        this.parameterMetadata = parameterMetadata;
    }

    public String getMeasuredDataOrder() {
        return measuredDataOrder;
    }

    public void setMeasuredDataOrder(String measuredDataOrder) {
        this.measuredDataOrder = measuredDataOrder;
    }

    public String getDataSeparator() {
        return dataSeparator;
    }

    public void setDataSeparator(String dataSeparator) {
        this.dataSeparator = dataSeparator;
    }

    public void addMeasuredData(ParameterMetadata pmd) {
        pmd.setSensorMetadata(this);
        this.measuredDataOrder = this.measuredDataOrder + pmd.getParameterMetadataId() + MEASURE_ORDER_SEPARATOR;
        this.parameterMetadata.add(pmd);
    }

    public Optional<ParameterMetadata> getParameterMetadataById(long id) {
        ParameterMetadata res = null;
        for (ParameterMetadata pmd : this.parameterMetadata) {
            if (pmd.getParameterMetadataId() == id) {
                res = pmd;
                break;
            }
        }
        return Optional.ofNullable(res);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public DisplayableSensorMetadata convert() {
        return new DisplayableSensorMetadata(this);
    }
}
