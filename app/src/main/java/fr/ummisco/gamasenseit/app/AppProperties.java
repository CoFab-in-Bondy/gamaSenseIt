package fr.ummisco.gamasenseit.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {

    private static final  String
            gamaSenseItFile = "gmstfile",
            gamaSenseItLauncher = "gmstlauncher",
            gamaSenseItNameWindows = "gamasenseit.bat",
            scriptPath = "/script",
            iconName = "favicon.ico",
            imagesPath = "/img",
            iconPath = imagesPath + "/" + iconName;

    private static String java = "javaw.exe", arduinoVersion = "0.25.0", baseUrl = "https://localhost";

    static {
        try (InputStream input = AppProperties.class.getResourceAsStream("/application.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            java = prop.getProperty("gamaSenseIt.java", java);
            arduinoVersion = prop.getProperty("gamaSenseIt.arduino.version", arduinoVersion);
            baseUrl = prop.getProperty("gamaSenseIt.base-url", baseUrl);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private AppProperties() {
        throw new IllegalStateException("AppProperties is a static class");
    }

    public static String gamaSenseItFile() {
        return gamaSenseItFile;
    }

    public static String gamaSenseItLauncher() {
        return gamaSenseItLauncher;
    }

    public static String gamaSenseItNameWindows() {
        return gamaSenseItNameWindows;
    }

    public static String scriptPath() {
        return scriptPath;
    }

    public static String iconName() {
        return iconName;
    }

    public static String imagesPath() {
        return imagesPath;
    }

    public static String iconPath() {
        return iconPath;
    }

    public static String java() {
        return java;
    }

    public static String arduinoVersion() {
        return arduinoVersion;
    }

    public static String baseUrl() {
        return baseUrl;
    }
}
