package fr.ummisco.gamasenseit.arduino.struct;

import java.util.Objects;

public class Arduino {
    public static final Arduino
            ARDUINO_AVG_MEGA = new Arduino("arduino:avr:mega"),
            ARDUINO_AVG_UNO = new Arduino("arduino:avr:uno");

    private final String fqbn;

    public Arduino(String fqbn) {
        this.fqbn = Objects.requireNonNull(fqbn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Arduino)) return false;
        Arduino arduino = (Arduino) o;
        return fqbn.equals(arduino.fqbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fqbn);
    }

    public String getFQBN() {
        return this.fqbn;
    }

    public String getCore() {
        return this.fqbn.substring(0, fqbn.lastIndexOf(":")) + "@1.8.1";
    }
}
