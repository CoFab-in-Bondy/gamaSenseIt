package ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.struct;

public class Systems {

    public static String tmpdir() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String userHome() {
        return System.getProperty("user.home");
    }
}
