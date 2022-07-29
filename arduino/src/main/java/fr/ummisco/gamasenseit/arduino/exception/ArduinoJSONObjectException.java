package fr.ummisco.gamasenseit.arduino.exception;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ArduinoJSONObjectException extends ArduinoException {
    public ArduinoJSONObjectException(String message, String out) {
        super(message, out);
    }

    public JSONObject jsonObject() throws IOException {
        if (output() == null) return null;
        try {
            return new JSONObject(output());
        } catch (JSONException err) {
            throw new IOException("Error stream is not an JSONObject");
        }
    }
}
