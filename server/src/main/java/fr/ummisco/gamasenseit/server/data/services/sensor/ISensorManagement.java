package fr.ummisco.gamasenseit.server.data.services.sensor;

import fr.ummisco.gamasenseit.server.data.model.sensor.ParameterMetadata;
import fr.ummisco.gamasenseit.server.data.model.sensor.Sensor;
import fr.ummisco.gamasenseit.server.data.model.sensor.SensorMetadata;

import java.util.Collection;


public interface ISensorManagement {
    String DEFAULT_DESCRIPTION = "UNKNOWN_DESCRIPTION";
    String DEFAULT_SENSOR_DISPLAY_NAME = "UNKNOWN_DISPLAY_NAME";
    String DEFAULT_SENSOR_NAME = "UNKNOWN_SENSOR";
    String DEFAULT_SENSOR_PLACE = "UNKNOWN_PLACE";
    String DEFAULT_SENSOR_TYPE_NAME = "UNKNOWN_SENSOR_TYPE";
    String DEFAULT_SENSOR_VERSION = "UNKNOWN_SENSOR_VERSION";

    void saveData(String message);

    Sensor updateSensorInformation(Sensor s);

    ParameterMetadata addParameterToSensorMetadata(SensorMetadata smd, ParameterMetadata pmd);

    SensorMetadata addSensorMetadata(SensorMetadata smd);

    Sensor addSensorForUser(Sensor sensor, long userId);

    Sensor patchSensor(Sensor sensor);

    SensorMetadata addSensorMetadata(SensorMetadata smd, Collection<ParameterMetadata> pmds);
}
