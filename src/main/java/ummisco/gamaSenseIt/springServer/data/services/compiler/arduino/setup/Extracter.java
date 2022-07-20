package ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.setup;

import java.io.IOException;

public interface Extracter {
    String extract(String archive, String dest, String name) throws IOException;
}
