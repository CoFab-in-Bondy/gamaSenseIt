package ummisco.gamaSenseIt.springServer.data.controller;

public interface Route {

    String
            // First level
            PUBLIC = "/public",
            PRIVATE = "/private",

            // Second level
            DATA = "/data",
            SENSORS = "/sensors",
            PARAMETERS = "/parameters",
            SERVER = "/server",

            ADD_PARAMETER_METADATA = "/addParameterMetadata",
            ADD_SENSOR = "/addSensor",
            ADD_SENSOR_METADATA = "/addSensorMetadata",
            DEFAULT_DATA_SEPARATOR = "/getDefaultDataSeparator",
            UPDATE_SENSOR = "/updateSensor",
            UPDATE_SENSOR_METADATA = "/updateSensorMetadata",

            // Third level
            NAMES = "/names",
            TYPES = "/types",
            DATE = "/date",
            SEPARATOR = "/separator",
            ID = "/{id}";

}
