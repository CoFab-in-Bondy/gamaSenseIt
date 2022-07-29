package fr.ummisco.gamasenseit.arduino.setup;

import java.io.IOException;

public interface Extracter {
    String extract(String archive, String dest, String name) throws IOException;
}
