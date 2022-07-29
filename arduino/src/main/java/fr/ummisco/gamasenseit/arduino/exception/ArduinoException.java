package fr.ummisco.gamasenseit.arduino.exception;

public class ArduinoException extends Exception {

    private final String out;

    public ArduinoException(String message, String out) {
        super(message);
        this.out = out;
    }

    public String output() {
        return out;
    }
}
