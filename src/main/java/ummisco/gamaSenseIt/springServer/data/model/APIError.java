package ummisco.gamaSenseIt.springServer.data.model;

import org.springframework.http.HttpStatus;

public class APIError {
    private int status;
    private String error;
    private String message;
    private long timestamp;

    public APIError(HttpStatus status, String message){
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.timestamp = System.nanoTime();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
