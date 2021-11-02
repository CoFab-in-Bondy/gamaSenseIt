package ummisco.gamaSenseIt.springServer.data.model;

import javax.persistence.*;
import java.nio.ByteBuffer;

@Entity
public class ParameterMetadata implements IConvertible<DisplayableParameterMetadata> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long parameterMetadataId;
    private String name;
    private String unit;
    @ManyToOne
    private SensorMetadata sensorMetadata;
    private DataFormat dataFormat;
    private DataParameter dataParameter;
    private String icon = "";

    public ParameterMetadata(String name, String unit, DataFormat dataFormat, DataParameter dataParameter) {
        this.name = name;
        this.unit = unit;
        this.dataFormat = dataFormat;
        this.dataParameter = dataParameter;
        this.setIconFromParameter();
    }

    public ParameterMetadata() {}

    private void setIconFromParameter() {
        icon = switch (this.dataParameter) {
            case TEMPERATURE -> "fas fa-thermometer-three-quarters";
            case PM10, PM2_5, PM1 -> "fab fa-cloudversify";
            case HUMIDITY -> "fas fa-tint";
            default -> "";
        };
    }

    public Long getParameterMetadataId() {
        return parameterMetadataId;
    }

    public void setParameterMetadataId(Long parameterMetadataId) {
        this.parameterMetadataId = parameterMetadataId;
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

    public DataFormat getDataFormat() {
        return dataFormat;
    }

    public void setTypeOfData(DataFormat typeOfData) {
        this.dataFormat = typeOfData;
    }

    public DataParameter getDataParameter() {
        return dataParameter;
    }

    public void setDataParameter(DataParameter typeOfSensor) {
        this.dataParameter = typeOfSensor;
    }

    public SensorMetadata getSensorMetadata() {
        return sensorMetadata;
    }

    public void setSensorMetadata(SensorMetadata sensorMetadata) {
        this.sensorMetadata = sensorMetadata;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public DisplayableParameterMetadata convert() {
        return new DisplayableParameterMetadata(this);
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
        INTEGER(0),
        DOUBLE(1),
        STRING(2);

        private final int type;

        DataFormat(int abbreviation) {
            this.type = abbreviation;
        }

        public Object convertToObject(byte[] data) {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            return switch (type) {
                case 0 -> buffer.getInt();
                case 1 -> buffer.getDouble();
                case 2 -> new String(data);
                default -> null;
            };
        }
    }
}
