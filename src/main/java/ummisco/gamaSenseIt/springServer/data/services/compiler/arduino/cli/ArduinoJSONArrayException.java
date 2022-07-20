package ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.cli;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class ArduinoJSONArrayException extends ArduinoException {
    public ArduinoJSONArrayException(String message, String out) {
        super(message, out);
    }

    public JSONArray jsonArray() throws IOException {
        if (output() == null) return null;
        try {
            return new JSONArray(output());
        } catch (JSONException err) {
            throw new IOException("Error stream is not an JSONObject");
        }
    }
}
