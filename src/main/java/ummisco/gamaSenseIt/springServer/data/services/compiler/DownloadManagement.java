package ummisco.gamaSenseIt.springServer.data.services.compiler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.user.DownloadToken;
import ummisco.gamaSenseIt.springServer.data.model.user.User;
import ummisco.gamaSenseIt.springServer.data.repositories.IDownloadTokenRepository;
import ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.cli.ArduinoException;
import ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.cli.Properties;
import ummisco.gamaSenseIt.springServer.security.SecurityUtils;

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

    public String generateDownloadToken(User user, Sensor sensor, Properties prop) throws ArduinoException, IOException {
        String token;
        do {
            token = UUID.randomUUID().toString();
        } while (downloadTokenRepo.findByToken(token).isPresent());
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
                .findByToken(token)
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

