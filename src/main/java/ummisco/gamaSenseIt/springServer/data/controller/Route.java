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
            // Third level
            NAMES = "/names",
            TYPES = "/types",
            DATE = "/date",
            SEPARATOR = "/separator",
            ID = "/{id}";

}
