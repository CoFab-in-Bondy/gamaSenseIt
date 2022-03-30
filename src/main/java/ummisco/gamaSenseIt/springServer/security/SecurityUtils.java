package ummisco.gamaSenseIt.springServer.security;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.Normalizer;

@Service
public class SecurityUtils {


    public String sanitizeFilename(String name) {
        // null is converted to "null"
        if (name == null)
            name = "null";

        // decompose accents and chars
        name = Normalizer.normalize(name, Normalizer.Form.NFD);

        // remove chars
        name = name.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        // remove all others chars
        name = name.replaceAll("[^a-zA-Z0-9-_]", "_");

        // strip too long filename
        var len = name.length();
        if (len > 200)
            name = name.substring(0, 99) + "__" + name.substring(len - 99);

        if (len == 0)
            name = "_";

        return name;
    }
}
