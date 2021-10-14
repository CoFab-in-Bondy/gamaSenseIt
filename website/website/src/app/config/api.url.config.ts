// tslint:disable-next-line: one-variable-per-declaration
const PRIVATE = 'http://vmpams.ird.fr:8080/private/';
// const PRIVATE = 'http://localhost:8080/private/';
const PUBLIC = 'http://vmpams.ird.fr:8080/public/';

// const PUBLIC = 'http://localhost:8080/public/';


export const API_URLS = {
  TEST: '',

  ADD_SENSOR: PRIVATE + 'addSensor', // name,lat,long,metadata
  ADD_SENSOR_METADATA: PRIVATE + 'addSensorMetadata', // name,version,separator
  ADD_PARAMETER_META_DATA: PRIVATE + 'addParameterMetadata', // idMeta,name,unit,dataformat,measuredparam
  UPDATE_SENSOR: PRIVATE + 'updateSensor', // sensorid,nom, lat,long
  UPDATE_SENSOR_METADATA: PRIVATE + 'updateSensorMetadata',

  DEFAULT_DATA_SEPARATOR: PUBLIC + 'getDefaultDataSeparator',
  META_DATA: PUBLIC + 'getMetadata',
  META_DATA_ID: PUBLIC + 'getMetadataByParameterId',
  META_DATA_SENSOR_META_DATA_ID: PUBLIC + 'getMetadataBySensorMetadataId',
  SENSOR: PUBLIC + 'getSensorById',
  SENSOR_DATA: PUBLIC + 'getData', // GET- JSON
  SENSOR_DATA_BETWEEN_DATES: PUBLIC + 'getDataOfSensorBetweenDate', // sensorid,paramid,begindate,EndDate POST-GET
  SENSOR_DATA_SINCE_DATE: PUBLIC + 'getDataOfSensorSinceDate', // sensorid,paramid,begindate POST-GET
  SENSOR_META_DATA: PUBLIC + 'getSensorMetadata',
  SENSOR_META_DATA_FULLNAMES: PUBLIC + 'getSensorTypeNames',
  SENSORS: PUBLIC + 'getSensors',
  SENSORS_NAMES: PUBLIC + 'getSensorsNames',
};
