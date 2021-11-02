package ummisco.gamaSenseIt.springServer.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DisplayableSensorMetadata {
    private final List<String> parametersMetadata;
    private Long id;
    private String name;
    private String version;
    private String dataSeparator;
    private String measuredDataOrder;
    private String description;

    public DisplayableSensorMetadata(Long id, String name, String version, String dataSeparator,
                                     String measuredDataOrder, Set<ParameterMetadata> parametersMetadata, String description) {
        super();
        this.id = id;
        this.name = name;
        this.version = version;
        this.dataSeparator = dataSeparator;
        this.measuredDataOrder = measuredDataOrder;
        this.parametersMetadata = new ArrayList<>();
        for (ParameterMetadata pm : parametersMetadata) {
            this.parametersMetadata.add(pm.getName());
        }
        this.description = description;
    }

    public DisplayableSensorMetadata(SensorMetadata smd) {
        this(
                smd.getSensorMetadataId(),
                smd.getName(),
                smd.getVersion(),
                smd.getDataSeparator(),
                smd.getMeasuredDataOrder(),
                smd.getParameterMetadataById(),
                smd.getDescription()
        );
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
