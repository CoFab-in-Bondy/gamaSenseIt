package fr.ummisco.gamasenseit.arduino.cli;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Properties {

    private static final String REGEX_KEY = "[^a-zA-Z0-9_]";
    private static final String REGEX_STRING = "[^a-zA-Z0-9-+_\\\\.]";

    private final LinkedHashMap<String, String> properties;

    public Properties() {
        this.properties = new LinkedHashMap<>();
    }

    public static Properties build() {
        return new Properties();
    }

    public boolean empty() {
        return this.properties.isEmpty();
    }

    private String keyfy(String key) {
        key = Security.esc(key, REGEX_KEY);
        if (key.length() < 2) throw new IndexOutOfBoundsException("key too small");
        if (key.length() >= 64) throw new IndexOutOfBoundsException("key too long");
        return key;
    }

    public Properties addInt(String key, int value) {
        this.properties.put(keyfy(key), String.valueOf(value));
        return this;
    }

    public Properties addLong(String key, long value) {
        this.properties.put(keyfy(key), String.valueOf(value));
        return this;
    }

    public Properties define(String key, boolean presence) {
        if (presence)
            this.properties.put(keyfy(key), null);
        else
            this.properties.remove(keyfy(key));
        return this;
    }

    public Properties addShortString(String key, String value) {
        value = "\"" + Security.esc(Objects.requireNonNull(value), REGEX_STRING) + "\"";
        if (value.length() >= 255) throw new IndexOutOfBoundsException("Value too long");
        this.properties.put(keyfy(key), value);
        return this;
    }

    public Properties addText(String key, String value) {
        value = "\"" + Security.esc(Objects.requireNonNull(value), REGEX_STRING) + "\"";
        this.properties.put(keyfy(key), value);
        return this;
    }

    public Properties delete(String key) {
        this.properties.remove(keyfy(key));
        return this;
    }

    public String asString() {
        StringBuilder propStr = new StringBuilder("build.extra_flags=");
        for (Map.Entry<String, String> property : properties.entrySet()) {
            if (property.getValue() == null)
                propStr.append("-D").append(property.getKey());
            else
                propStr.append("\"-D").append(property.getKey()).append('=').append(property.getValue()).append("\"");
            propStr.append(' ');
        }
        return propStr.toString();
    }

    public Properties copy() {
        Properties prop = new Properties();
        prop.properties.putAll(properties);
        return prop;
    }
}
