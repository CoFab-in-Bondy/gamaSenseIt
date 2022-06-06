package ummisco.gamaSenseIt.springServer.data.controller;

public class Routes {

    public static final String
            // Arguments
            NAMES = "/names",
            DATE = "/date",
            SEPARATOR = "/separator",
            METADATA = "/metadata",
            DATA = "/data",
            SEARCH = "/search",
            ID = "/{id}",
            SENSOR_ID = "/{sensorId}",
            USERS = "/users",
            PROMOTE = "/promote",
            DISMISE = "/dismiss";

    public static final String
            // First level
            PUBLIC = "/public",
            PRIVATE = "/private",
            ERROR = "/error",
            AUTH = "/auth";

    public static final String
            // Second level
            SERVER = "/server",
            PARAMETERS = "/parameters",
            SENSORS = "/sensors",
            EXTENDED = "/extended",
            BINARY = "/binary",
            DOWNLOAD = "/download",
            LOGIN = "/login",
            REFRESH = "/refresh",
            LOGOUT = "/logout",
            ME = "/me",
            IMAGE = "/image",
            ACCESSES = "/accesses",
            IP = "/ip",
            SENSORS_METADATA = SENSORS + METADATA,
            PARAMETERS_METADATA = PARAMETERS + METADATA;

    public static final String[] ENDPOINTS = {PUBLIC, PRIVATE, AUTH, ERROR};


    public static boolean isEndpoint(String location) {
        for (var endpoint : Routes.ENDPOINTS)
            if (location.startsWith(endpoint))
                return true;
        return false;
    }
}
