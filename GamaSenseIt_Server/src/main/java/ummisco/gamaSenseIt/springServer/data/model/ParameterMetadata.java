package ummisco.gamaSenseIt.springServer.data.model;

import java.nio.ByteBuffer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ParameterMetadata {
  public enum DataFormat {
    INTEGER(0),
    DOUBLE(1),
    STRING(2);

    private int type;

    private DataFormat(int abreviation) {
      this.type = abreviation;
    }

    public Object convertToObject(byte[] data) {
      ByteBuffer buffer = ByteBuffer.wrap(data);
      Object res = null;
      switch (type) {
      case 0: {
        return new Integer(buffer.getInt());
      }
      case 1: {
        return new Double(buffer.getDouble());
      }
      case 2: {
        return new String(data);
      }
      }
      return res;
    }

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
    switch (this.parameter) {
    case TEMPERATURE:
      icon = "fas fa-thermometer-three-quarters";
      break;
    case PM10:
      icon = "fab fa-cloudversify";
      break;
    case PM2_5:
      icon = "fab fa-cloudversify";
      break;
    case PM1:
      icon = "fab fa-cloudversify";
      break;
    case HUMIDITY:
      icon = "fas fa-tint";
      break;
    default:
      icon = "";
    }
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
}
