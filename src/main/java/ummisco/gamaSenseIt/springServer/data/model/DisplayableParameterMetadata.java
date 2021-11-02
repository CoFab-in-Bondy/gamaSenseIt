package ummisco.gamaSenseIt.springServer.data.model;

import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata.DataParameter;

public class DisplayableParameterMetadata {
    private long id;
    private String name;
    private String unit;
    private ParameterMetadata.DataFormat dataFormat;
    private DataParameter parameter;
    private long sensorMetadata;
    private String icon;

    public DisplayableParameterMetadata(long id, String name, String unit, ParameterMetadata.DataFormat dataFormat,
                                        DataParameter parameter, long sensorMetadata, String icon) {
        super();
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.dataFormat = dataFormat;
        this.parameter = parameter;
        this.sensorMetadata = sensorMetadata;
        this.icon = icon;
    }

    public DisplayableParameterMetadata(ParameterMetadata pmd) {
        this(pmd.getParameterMetadataId(), pmd.getName(), pmd.getUnit(), pmd.getDataFormat(), pmd.getDataParameter(),
                pmd.getSensorMetadata().getSensorMetadataId(), pmd.getIcon());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public ParameterMetadata.DataFormat getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(ParameterMetadata.DataFormat dataFormat) {
        this.dataFormat = dataFormat;
    }

    public DataParameter getParameter() {
        return parameter;
    }

    public void setParameter(DataParameter parameter) {
        this.parameter = parameter;
    }

    public long getSensorMetadata() {
        return sensorMetadata;
    }

    public void setSensorMetadata(long sensorMetadata) {
        this.sensorMetadata = sensorMetadata;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
