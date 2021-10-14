package ummisco.gamaSenseIt.springServer.data.model;

import java.util.Date;

public class DisplayableData {
  private Object value;
  private Date date;
  private String unit;
  private String sensorName;
  private double latitude;
  private double longitude;
  private String measuredParameter;

  public DisplayableData(Object value, Date date, String unit, String sensorName, double latitude, double longitude,
      String measuredParameter) {
    super();
    this.value = value;
    this.date = date;
    this.unit = unit;
    this.sensorName = sensorName;
    this.latitude = latitude;
    this.longitude = longitude;
    this.measuredParameter = measuredParameter;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getSensorName() {
    return sensorName;
  }

  public void setSensorName(String sensorName) {
    this.sensorName = sensorName;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getMeasuredParameter() {
    return measuredParameter;
  }

  public void setMeasuredParameter(String measuredParameter) {
    this.measuredParameter = measuredParameter;
  }

}
