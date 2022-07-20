package ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.struct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Board {

    private final String address;
    private final Arduino arduino;

    public Board(String address, Arduino arduino) {
        this.address = address;
        this.arduino = arduino;

    }

    public static Board fromJSON(JSONObject json) throws JSONException {
        JSONArray boards = json.getJSONArray("matching_boards");
        if (boards.length() != 1) throw new JSONException("Must have one board connected");
        JSONObject board = boards.getJSONObject(0);
        if (board == null) throw new JSONException("Not a board");
        String fqbn = board.getString("fqbn");
        if (fqbn == null) throw new JSONException("No fqbn");
        Arduino arduino = new Arduino(fqbn);
        JSONObject port = json.getJSONObject("port");
        if (port == null) throw new JSONException("No port");
        String address = port.getString("address");
        if (address == null) throw new JSONException("No address");
        return new Board(address, arduino);
    }

    public String getAddress() {
        return address;
    }

    public Arduino getArduino() {
        return arduino;
    }
}
