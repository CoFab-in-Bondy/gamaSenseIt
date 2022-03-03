package ummisco.gamaSenseIt.springServer.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.user.User;
import ummisco.gamaSenseIt.springServer.data.repositories.*;
import ummisco.gamaSenseIt.springServer.data.services.access.AccessManagement;
import ummisco.gamaSenseIt.springServer.data.services.geo.GeoService;
import ummisco.gamaSenseIt.springServer.data.services.record.RecordManager;
import ummisco.gamaSenseIt.springServer.data.services.sensor.ISensorManagment;
import ummisco.gamaSenseIt.springServer.services.formatter.ExportResolver;

import java.util.ArrayList;
import java.util.function.Function;


public abstract class DataController {
    final static String NIL = "nil";

    @Autowired
    protected IAccessRepository accessRepo;

    @Autowired
    protected IUserRepository userRepo;

    @Autowired
    protected ISensorRepository sensorsRepo;

    @Autowired
    protected ISensorMetadataRepository sensorsMetadataRepo;

    @Autowired
    protected ISensorManagment sensorsManagement;

    @Autowired
    protected IParameterRepository parametersRepo;

    @Autowired
    protected IParameterMetadataRepository parametersMetadataRepo;

    @Autowired
    protected RecordManager recordManager;

    @Autowired
    protected ExportResolver export;

    @Autowired
    protected GeoService geoService;

    @Autowired
    protected AccessManagement accessManagement;

    private User publicUser;

    public static <M, D> ArrayList<D> apply(Iterable<M> list, Function<M, D> caster) {
        var result = new ArrayList<D>();
        for (var obj : list)
            result.add(caster.apply(obj));
        return result;
    }

    public User publicUser() {
        if (publicUser == null)
            publicUser = userRepo.findByMail("public");
        return publicUser;
    }

    public User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth instanceof AnonymousAuthenticationToken ? null: userRepo.findByMail(auth.getName());

    }

    public User user() {
        var current = currentUser();
        return current != null? current: publicUser();
    }

    public Sensor sensorRead(long sensorId) throws ResponseStatusException {
        var s = sensorsRepo.findReadableSensor(user().getId(), sensorId);
        if (s == null)
            s = sensorsRepo.findReadableSensor(publicUser().getId(), sensorId);
        if (s == null) {
            System.err.println("Can't find sensors with id " + sensorId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "can't find sensor");
        }
        return s;
    }
}
