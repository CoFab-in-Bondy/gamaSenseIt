package ummisco.gamaSenseIt.springServer.data.controller;

import org.springframework.http.MediaType;

public interface IDataController {
    String SERVER_DATETIME = "serverDateTime";
    
    String ADD_PARAMETER_METADATA = "addParameterMetadata";
    String ADD_SENSOR = "addSensor";
    String ADD_SENSOR_METADATA = "addSensorMetadata";

    String GET_SENSOR_BY_ID = "getSensorById";
    String GET_DATA = "getData";
    String GET_DATA_BY_SENSOR_ID = "getDataBySensorId";
    String GET_DATA_OF_SENSOR_BETWEEN_DATE = "getDataOfSensorBetweenDate";
    String GET_DATA_OF_SENSOR_SINCE_DATE = "getDataOfSensorSinceDate";
    String GET_SENSOR_METADATA = "getSensorMetadata";
    String GET_SENSOR_TYPE_NAMES = "getSensorTypeNames";
    String GET_SENSORS = "getSensors";
    String GET_SENSORS_NAMES = "getSensorsNames";
    String GET_META_DATA = "getMetadata";
    String GET_METADATA_BY_PAREMETER_ID = "getMetadataByParameterId";
    String GET_METADATA_BY_SENSOR_METADATA_ID = "getMetadataBySensorMetadataId";

    String CSV_DATA_BY_SENSOR_ID = "csvDataBySensorId";
    
    String UPDATE_PARAMETER_METADATA = "updateParameterMetadata";
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
