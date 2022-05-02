package ummisco.gamaSenseIt.springServer.data.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import ummisco.gamaSenseIt.springServer.data.model.APIError;

@ControllerAdvice
public class GlobalExceptionHandler {


    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<APIError> generateNotFoundException(ResponseStatusException ex) {
        var apiError = new APIError(ex.getStatus(), ex.getReason());
        logger.error("[" + ex.getStatus().value() + " " + ex.getStatus().getReasonPhrase() + "] " + ex.getReason());
        return new ResponseEntity<>(apiError, ex.getStatus());
    }

}