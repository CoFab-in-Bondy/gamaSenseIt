package ummisco.gamaSenseIt.springServer.data.services.sensor;

import ummisco.gamaSenseIt.springServer.data.model.sensor.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.sensor.SensorMetadata;

import java.util.Collection;
import java.util.Date;


public interface ISensorManagment {
    String DEFAULT_DESCRIPTION = "UNKNOWN_DESCRIPTION";
    String DEFAULT_SENSOR_DISPLAY_NAME = "UNKNOWN_DISPLAY_NAME";
    String DEFAULT_SENSOR_NAME = "UNKNOWN_SENSOR";
    String DEFAULT_SENSOR_PLACE = "UNKNOWN_PLACE";
    String DEFAULT_SENSOR_TYPE_NAME = "UNKNOWN_SENSOR_TYPE";
    String DEFAULT_SENSOR_VERSION = "UNKNOWN_SENSOR_VERSION";

    Sensor updateSensorInformation(Sensor s);

    ParameterMetadata addParameterToSensorMetadata(SensorMetadata smd, ParameterMetadata pmd);

    SensorMetadata addSensorMetadata(SensorMetadata smd);

    void saveDefaultSensorInit();

    void saveData(String message, Date date);

    Sensor addSensorForUser(Sensor sensor, long userId);
    SensorMetadata addSensorMetadata(SensorMetadata smd, Collection<ParameterMetadata> pmds);
}
