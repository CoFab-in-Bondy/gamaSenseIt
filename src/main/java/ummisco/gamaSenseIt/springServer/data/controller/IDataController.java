package ummisco.gamaSenseIt.springServer.data.controller;

public interface IDataController {
  String ADD_PARAMETER_META_DATA = "addParameterMetadata";
  String ADD_SENSOR = "addSensor";
  String ADD_SENSOR_METADATA = "addSensorMetadata";
  String SENSOR = "getSensorById";
  String SENSOR_DATA = "getData";
  String SENSOR_DATA_BETWEEN_DATES = "getDataOfSensorBetweenDate";
  String SENSOR_DATA_SINCE_DATE = "getDataOfSensorSinceDate";
  String SENSOR_META_DATA = "getSensorMetadata";
  String SENSOR_META_DATA_FULLNAMES = "getSensorTypeNames";
  String SENSORS = "getSensors";
  String SENSORS_NAMES = "getSensorsNames";
  String SERVER_DATE = "serverDateTime";
  String META_DATA = "getMetadata";
  String META_DATA_ID = "getMetadataByParameterId";
  String META_DATA_SENSOR_META_DATA_ID = "getMetadataBySensorMetadataId";
  String UPDATE_PARAMETER_META_DATA = "updateParameterMetadata";
  String UPDATE_SENSOR = "updateSensor";
  String UPDATE_SENSOR_METADATA = "updateSensorMetadata";

  String METADATA_ID = "metadataId";
  String PARAMETER_ID = "parameterId";
  String SENSOR_ID = "sensorId";

  String BEGIN_DATE = "beginDate";
  String DATA_FORMAT = "dataFormat";
  String DATA_SEPARATOR = "dataSeparator";
  String DEFAULT_DATA_SEPARATOR = "getDefaultDataSeparator";
  String DESCRIPTION = "description";
  String DISPLAY_NAME = "displayName";
  String END_DATE = "endDate";
  String LATITUDE = "latitude";
  String LONGITUDE = "longitude";
  String MEASURED_PARAMETER = "measuredParameter";
  String MEASURED_DATA_ORDER = "measuredDataOrder";
  String NAME = "name";
  String SENSOR_METADATA = "sensorMetadata";
  String SUB_DISPLAY_NAME = "subDisplayName";
  String UNIT = "unit";
  String VERSION = "version";

  String DATE_PATTERN = "MMddyyyy";

}
