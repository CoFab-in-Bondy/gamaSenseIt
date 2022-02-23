package ummisco.gamaSenseIt.springServer.security;

import org.springframework.stereotype.Service;

@Service
public class SecurityUtils {

    public String sanitizeFilename(String inputName) {
        return inputName.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
}
