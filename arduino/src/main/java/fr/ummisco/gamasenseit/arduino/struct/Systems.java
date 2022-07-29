package fr.ummisco.gamasenseit.arduino.struct;

import java.io.File;

public class Systems {

    public static String tmpdir() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String userHome() {
        return System.getProperty("user.home");
    }

    public static String gamasenseitHome() {
        return Systems.userHome() + File.separator + ".gamasenseit";
    }
}
