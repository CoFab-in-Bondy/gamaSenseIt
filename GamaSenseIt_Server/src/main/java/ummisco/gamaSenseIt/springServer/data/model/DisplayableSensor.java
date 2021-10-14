package ummisco.gamaSenseIt.springServer.data.model;

public class DisplayableSensor {
  private long idSensor;
  private String name;
  private String displayName;
  private String subDisplayName;
  private double longitude;
  private double latitude;
  private long sensorMetadata;
  private String sensorMetadataName;

  public DisplayableSensor(long idSensor, String name, String displayName, String subDisplayName, double longitude,
      double latitude, long sensorMetadata, String sensorMetadataName) {
    super();
    this.idSensor = idSensor;
    this.name = name;
    this.displayName = displayName;
    this.subDisplayName = subDisplayName;
    this.longitude = longitude;
    this.latitude = latitude;
    this.sensorMetadata = sensorMetadata;
    this.sensorMetadataName = sensorMetadataName;
  }

  public DisplayableSensor(Sensor s) {
    this(s.getIdSensor(), s.getName(), s.getDisplayName(), s.getSubDisplayName(), s.getLongitude(), s.getLatitude(),
        s.getMetadata().getIdType(), s.getMetadata().getName() + " -- " + s.getMetadata().getVersion());
  }

  public long getIdSensor() {
    return idSensor;
  }

  public void setIdSensor(long idSensor) {
    this.idSensor = idSensor;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public long getSensorMetadata() {
    return sensorMetadata;
  }

  public void setSensorMetadata(long sensorMetadata) {
    this.sensorMetadata = sensorMetadata;
  }

  public String getSensorMetadataName() {
    return sensorMetadataName;
  }

  public void setSensorMetadataName(String sensorMetadataName) {
    this.sensorMetadataName = sensorMetadataName;
  }

  public String getSubDisplayName() {
    return subDisplayName;
  }

  public void setSubDisplayName(String subDisplayName) {
    this.subDisplayName = subDisplayName;
  }

}
