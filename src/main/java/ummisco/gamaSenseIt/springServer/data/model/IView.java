package ummisco.gamaSenseIt.springServer.data.model;

public interface IView {
    interface Public {}
    interface Private extends Public {}
    interface Internal extends Private {}

    interface SensorOfParameter extends Public {}
    interface ParameterMetadataOfParameter extends Public {}

    interface SensorMetadataOfParameterMetadata extends Public {}
    interface ParametersOfParameterMetadata extends Public {}

    interface SensorMetadataOfSensor extends Public {}
    interface ParametersOfSensor extends Public {}

    interface ParametersMetadataOfSensorMetadata extends Public {}
    interface SensorsOfSensorMetadata extends Public {}

    interface SensorMetadataExtended extends SensorsOfSensorMetadata, ParametersMetadataOfSensorMetadata {}
}
