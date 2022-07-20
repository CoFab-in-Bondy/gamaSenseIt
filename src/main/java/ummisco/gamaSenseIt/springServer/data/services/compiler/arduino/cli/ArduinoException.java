package ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.cli;

public class ArduinoException extends Exception {

    private String out;

    public ArduinoException(String message, String out) {
        super(message);
        this.out = out;
    }

    public String output() {
        if (out == null) return null;
        return out;
    }
}
