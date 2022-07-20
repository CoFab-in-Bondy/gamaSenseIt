package ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.cli;

import java.text.Normalizer;

public class Security {


    public static String esc(String value, String regex) {
        // null is converted to "null"
        if (value == null)
            value = "null";

        // decompose accents and chars
        value = Normalizer.normalize(value, Normalizer.Form.NFD);

        // remove chars
        value = value.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        // remove all others chars
        value = value.replaceAll(regex, "_");
        return value;
    }
}
