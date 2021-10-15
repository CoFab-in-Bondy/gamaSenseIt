package ummisco.gamaSenseIt.springServer.data.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class SensorMetadata {
    public final static String MEASURE_ORDER_SEPARATOR = ":";
    public final static String DEFAULT_DATA_SEPARATOR = ":";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idType;
    private String version;
    private String name;
    private String measuredDataOrder;

    private String dataSeparator;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "sensorMetadata")
    private Set<ParameterMetadata> parameterMetadata = new HashSet<>();

    private String description;

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

    public Long getIdType() {
        return idType;
    }

    public void setIdType(Long idType) {
        this.idType = idType;
    }

    public Set<ParameterMetadata> getParameterMetadata() {
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

    public void addMeasuredData(ParameterMetadata md) {
        md.setSensorMetadata(this);
        this.measuredDataOrder = this.measuredDataOrder + md.getId() + MEASURE_ORDER_SEPARATOR;
        this.parameterMetadata.add(md);
    }

    public Optional<ParameterMetadata> getParameterMetadata(long id) {
        ParameterMetadata res = null;
        for (ParameterMetadata md : this.parameterMetadata) {
            if (md.getId() == id) {
                res = md;
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
}
