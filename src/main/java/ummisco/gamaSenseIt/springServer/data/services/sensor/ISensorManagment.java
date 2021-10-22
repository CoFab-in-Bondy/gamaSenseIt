package ummisco.gamaSenseIt.springServer.data.services.sensor;

import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.SensorMetadata;

import java.util.Date;

public interface ISensorManagment {
    String DEFAULT_DESCRIPTION = "UNKNOWN_DESCRIPTION";
    String DEFAULT_SENSOR_DISPLAY_NAME = "UNKNOWN_DISPLAY_NAME";
    String DEFAULT_SENSOR_NAME = "UNKNOWN_SENSOR";
    String DEFAULT_SENSOR_PLACE = "UNKNOWN_PLACE";
    String DEFAULT_SENSOR_TYPE_NAME = "UNKNOWN_SENSOR_TYPE";
    String DEFAULT_SENSOR_VERSION = "UNKNOWN_SENSOR_VERSION";

    Sensor updateSensorInformation(Sensor s);

    ParameterMetadata addParameterToSensorMetadata(SensorMetadata s, ParameterMetadata md);

    SensorMetadata addSensorMetadata(SensorMetadata s);

    void saveDefaultSensorInit();

    void saveData(String message, Date date);
}
