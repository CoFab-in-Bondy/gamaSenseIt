package ummisco.gamaSenseIt.springServer.data.controller;

public interface IRoute {

    String
            // Arguments
            NAMES = "/names",
            DATE = "/date",
            SEPARATOR = "/separator",
            METADATA = "/metadata",
            SEARCH = "/search",
            ID = "/{id}",
    USERS = "/users",
    PROMOTE = "/promote",
    DISMISE = "/dismiss",

    // First level
    PUBLIC = "/public",
            PRIVATE = "/private",
            AUTH = "/auth",

    // Second level
    SERVER = "/server",
            PARAMETERS = "/parameters",
            SENSORS = "/sensors",
            EXTENDED = "/extended",
            DOWNLOAD = "/download",
            ME = "/me",
            IMAGE = "/image",
            ACCESSES = "/accesses",
            SENSORS_METADATA = SENSORS + METADATA,
            PARAMETERS_METADATA = PARAMETERS + METADATA,
            IP = "ip";

}
