package ummisco.gamaSenseIt.springServer.data.model;

import javax.persistence.*;
import java.nio.ByteBuffer;

@Entity
public class ParameterMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String varName;
    private String unit;
    @ManyToOne
    private SensorMetadata sensorMetadata;
    private DataFormat dataFormat;
    private DataParameter parameter;
    private String icon = "";

    public ParameterMetadata(String varName, String unit, DataFormat typeOfData, DataParameter typeOfSensor) {
        super();
        this.varName = varName;
        this.unit = unit;
        this.dataFormat = typeOfData;
        this.parameter = typeOfSensor;
        this.setIconFromParameter();
    }

    public ParameterMetadata() {
        super();
    }

    private void setIconFromParameter() {
        icon = switch (this.parameter) {
            case TEMPERATURE -> "fas fa-thermometer-three-quarters";
            case PM10, PM2_5, PM1 -> "fab fa-cloudversify";
            case HUMIDITY -> "fas fa-tint";
            default -> "";
        };
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
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

    public DataParameter getParameter() {
        return parameter;
    }

    public void setParameter(DataParameter typeOfSensor) {
        this.parameter = typeOfSensor;
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

    // TODO maybe do an interface ?

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
