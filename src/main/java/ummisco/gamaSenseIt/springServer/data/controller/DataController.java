package ummisco.gamaSenseIt.springServer.data.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.user.User;
import ummisco.gamaSenseIt.springServer.data.repositories.*;
import ummisco.gamaSenseIt.springServer.data.services.access.AccessManagement;
import ummisco.gamaSenseIt.springServer.data.services.access.InteractionManagement;
import ummisco.gamaSenseIt.springServer.data.services.compiler.DownloadManagement;
import ummisco.gamaSenseIt.springServer.data.services.geo.GeoService;
import ummisco.gamaSenseIt.springServer.data.services.record.RecordManager;
import ummisco.gamaSenseIt.springServer.data.services.sensor.ISensorManagement;
import ummisco.gamaSenseIt.springServer.security.SecurityUtils;
import ummisco.gamaSenseIt.springServer.data.services.compiler.Compiler;
import ummisco.gamaSenseIt.springServer.security.jwt.JwtUtils;
import ummisco.gamaSenseIt.springServer.services.export.ExportResolver;

import java.util.ArrayList;
import java.util.function.Function;


public abstract class DataController {

    private static final Logger logger = LoggerFactory.getLogger(DataController.class);

    @Autowired
    protected SecurityUtils securityUtils;

    @Autowired
    protected DownloadManagement downloadManagement;

    @Autowired
    protected JwtUtils jwtUtils;

    @Autowired
    protected IAccessRepository accessRepo;

    @Autowired
    protected IUserRepository userRepo;

    @Autowired
    protected ISensorRepository sensorsRepo;

    @Autowired
    protected ISensorMetadataRepository sensorsMetadataRepo;

    @Autowired
    protected ISensorManagement sensorsManagement;

    @Autowired
    protected IParameterRepository parametersRepo;

    @Autowired
    protected IParameterMetadataRepository parametersMetadataRepo;

    @Autowired
    protected IDownloadTokenRepository downloadTokenRepository;

    @Autowired
    protected RecordManager recordManager;

    @Autowired
    protected ExportResolver export;

    @Autowired
    protected GeoService geoService;

    @Autowired
    protected AccessManagement accessManagement;

    @Autowired
    protected InteractionManagement interactionManagement;

    private User publicUser;

    public static <M, D> ArrayList<D> apply(Iterable<M> list, Function<M, D> caster) {
        var result = new ArrayList<D>();
        for (var obj : list)
            result.add(caster.apply(obj));
        return result;
    }

    public @Nullable User publicUser() {
        if (publicUser == null)
            publicUser = userRepo.findByMail("public").orElse(null);
        return publicUser;
    }

    public @Nullable User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth instanceof AnonymousAuthenticationToken ? null: userRepo.findByMail(auth.getName()).orElse(null);
    }

    public User user() {
        var current = currentUser();
        return current != null? current: publicUser();
    }

    public Sensor sensorRead(long sensorId) throws ResponseStatusException {
        var user = user();
        var publicUser = publicUser();
        var s = sensorsRepo.findReadableSensor(user.getId(), sensorId);
        if (s == null && publicUser != null && user != publicUser)
            s = sensorsRepo.findReadableSensor(publicUser.getId(), sensorId);
        if (s == null) {
            logger.warn("Can't find sensors readable with id " + sensorId + " with " + user.getMail());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Capteur inexistant ou inaccessible");
        }
        return s;
    }

    public Sensor sensorManage(long sensorId) throws ResponseStatusException {
        var user = user();
        var publicUser = publicUser();
        if (user == publicUser) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous devez vous connecter");
        }
        var s = sensorsRepo.findManageableSensor(user.getId(), sensorId);
        if (s == null) {
            logger.warn("Can't find sensors manageable with id " + sensorId + " with " + user.getMail());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Capteur inexistant ou mauvaises permissions");
        }
        return s;
    }

    public ResponseEntity<byte[]> img(String filename, byte[] image) {
        filename = securityUtils.sanitizeFilename(filename);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(image);
    }
}
