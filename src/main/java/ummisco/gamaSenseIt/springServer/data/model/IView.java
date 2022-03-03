package ummisco.gamaSenseIt.springServer.data.model;

public interface IView {
    interface Public {}
    interface Private extends Public {}
    interface Internal extends Private {}

    // access
    interface AccessCount extends Public {}

    // parameters
    interface SensorOfParameter extends Public {}
    interface ParameterMetadataOfParameter extends Public {}

    // parameter metadata
    interface SensorMetadataOfParameterMetadata extends Public {}
    interface ParametersOfParameterMetadata extends Public {}

    // sensor
    interface SensorMetadataOfSensor extends Public {}
    interface ParametersOfSensor extends Public {}

    // sensor metadata
    interface ParametersMetadataOfSensorMetadata extends Public {}
    interface SensorsOfSensorMetadata extends Public {}

    interface SensorMetadataExtended extends SensorsOfSensorMetadata, ParametersMetadataOfSensorMetadata {}

    interface AccessUser extends Public {

    }
}
