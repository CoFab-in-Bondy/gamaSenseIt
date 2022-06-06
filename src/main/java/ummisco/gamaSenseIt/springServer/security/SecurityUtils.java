package ummisco.gamaSenseIt.springServer.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.text.Normalizer;

@Service
public class SecurityUtils {

    @Value("${gamaSenseIt.cors-url:}")
    private String corsUrl;
    @Value("${server.port}")
    private int portServer;

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

    public String getFrontUrl() {
        String url;
        try {
            url = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        } catch (NullPointerException | IllegalStateException err) {
            if (corsUrl != null && !corsUrl.isEmpty()) {
                url = corsUrl;
            } else {
                url = "https://localhost:"+portServer;
            }
        }
        url = url.replaceAll("//+$", "");
        return url;
    }
}
