export class Sensor{
  idSensor: number;
  name: string;
  displayName: string;
  subDisplayName: string;
  longitude: number;
  latitude: number;
  sensorMetadata: number;
  sensorMetadataName: string;

  constructor(
    idSensor: number,
    name: string,
    displayName: string,
    subDisplayName: string,
    longitude: number,
    latitude: number,
    sensorMetadata: number,
    sensorMetadataName: string)
  {
    this.idSensor = idSensor;
    this.name= name;
    this.displayName = displayName;
    this.subDisplayName = subDisplayName;
    this.longitude = longitude;
    this.latitude = latitude;
    this.sensorMetadata = sensorMetadata;
    this.sensorMetadataName = sensorMetadataName;
  }
}
