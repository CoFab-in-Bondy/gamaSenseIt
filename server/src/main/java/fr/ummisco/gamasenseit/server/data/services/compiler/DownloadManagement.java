package fr.ummisco.gamasenseit.server.data.services.compiler;

import fr.ummisco.gamasenseit.arduino.exception.ArduinoException;
import fr.ummisco.gamasenseit.server.data.model.user.DownloadToken;
import fr.ummisco.gamasenseit.server.data.repositories.IDownloadTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import fr.ummisco.gamasenseit.server.data.model.sensor.Sensor;
import fr.ummisco.gamasenseit.server.data.model.user.User;
import fr.ummisco.gamasenseit.arduino.cli.Properties;
import fr.ummisco.gamasenseit.server.security.SecurityUtils;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class DownloadManagement {

    @Value("${gamaSenseIt.download.validity}")
    private long downloadTokenValidity;

    @Autowired
    private IDownloadTokenRepository downloadTokenRepo;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private Compiler compiler;

    public String generateDownloadToken(User user, Sensor sensor, Properties prop) throws IOException, ArduinoException {
        String token;
        do {
            token = UUID.randomUUID().toString();
        } while (downloadTokenRepo.findDownloadTokenByTokenEquals(token).isPresent());
        var downloadToken = new DownloadToken();
        downloadToken.setToken(token);
        downloadToken.setIssuedAt(new Date());
        downloadToken.setExpiration(new Date(System.currentTimeMillis() - downloadTokenValidity - 2000000));
        downloadToken.setUserId(user.getId());
        downloadToken.setSensorId(sensor.getId());
        var program = compiler.getBinary(sensor, prop);
        downloadToken.setProgram(program);
        downloadTokenRepo.save(downloadToken);
        return token;
    }

    public ResponseEntity<ByteArrayResource> consumeDownloadToken(String token) {
        var downloadToken = downloadTokenRepo
                .findDownloadTokenByTokenEquals(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid or expired token"));
        downloadTokenRepo.delete(downloadToken);
        if (downloadToken.isExpired()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Expired token");
        }

        var binary = downloadToken.getProgram();
        var name = downloadToken.getSensor().getName();
        var header = new HttpHeaders();
        header.setContentType(new MediaType("application", "octet-stream"));
        header.setContentDisposition(ContentDisposition.attachment().filename(securityUtils.sanitizeFilename(name) + ".gmst").build());
        return new ResponseEntity<>(new ByteArrayResource(binary), header, HttpStatus.OK);
    }
}

