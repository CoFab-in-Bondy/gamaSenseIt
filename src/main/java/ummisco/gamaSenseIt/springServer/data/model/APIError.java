package ummisco.gamaSenseIt.springServer.data.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class APIError {
    private int status;
    private String error;
    private String message;
    private long timestamp;

    public APIError(){
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.error = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        this.message = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        this.timestamp = System.nanoTime();
    }

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

    public ResponseEntity<APIError> response() {
        return new ResponseEntity<>(this, HttpStatus.valueOf(this.getStatus()));
    }

    public ModelAndView modelAndView() {
        var mav = new ModelAndView(new MappingJackson2JsonView());
        mav.addObject("status", status);
        mav.addObject("error", error);
        mav.addObject("message", message);
        mav.addObject("timestamp", timestamp);
        mav.setStatus(HttpStatus.valueOf(this.getStatus()));
        return mav;
    }

    public String json() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public void intoResponse(@Nullable HttpServletResponse response) throws IOException {
        if (response == null) return;
        response.setStatus(this.status);
        response.getWriter().write(json());

    }
}
