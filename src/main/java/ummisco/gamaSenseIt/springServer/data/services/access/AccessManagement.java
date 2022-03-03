package ummisco.gamaSenseIt.springServer.data.services.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ummisco.gamaSenseIt.springServer.data.model.user.*;
import ummisco.gamaSenseIt.springServer.data.repositories.*;

import java.util.HashSet;

@Service
public class AccessManagement {

    @Autowired
    private IAccessRepository accessRepo;

    @Autowired
    private IAccessUserRepository accessUserRepo;

    @Autowired
    private IAccessSensorRepository accessSensorRepo;

    @Autowired
    private ISensorRepository sensorRepo;

    @Autowired
    private IUserRepository userRepo;


    public AccessUser addAccessUser(long accessId, long userId) {
        var pk = new AccessUser.AccessUserPK(accessId, userId);
        if (accessUserRepo.findById(pk).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user already have an access");
        var acu = new AccessUser();
        acu.setUserId(userId);
        acu.setAccessId(accessId);
        acu.setPrivilege(AccessUserPrivilege.VIEW);
        return accessUserRepo.save(acu);
    }

    public void delAccessUser(long accessId, long userId) {
        var pk = new AccessUser.AccessUserPK(accessId, userId);
        var acu = accessUserRepo
                .findById(pk)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't found access"));
        accessUserRepo.delete(acu);
    }

    public AccessUser promoteAccessUser(long accessId, long userId, AccessUserPrivilege accessUserPrivilege) {
        var pk = new AccessUser.AccessUserPK(accessId, userId);
        var acu = accessUserRepo
                .findById(pk)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't have access"));
        acu.setPrivilege(accessUserPrivilege);
        return accessUserRepo.save(acu);
    }

    public AccessSensor addAccessSensor(long accessId, long sensorId) {
        var pk = new AccessSensor.AccessSensorPK(accessId, sensorId);
        if (accessSensorRepo.findById(pk).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "sensor already have an access");
        var acu = new AccessSensor();
        acu.setSensorId(sensorId);
        acu.setAccessId(accessId);
        return accessSensorRepo.save(acu);
    }

    public void delAccessSensor(long accessId, long sensorId) {
        var pk = new AccessSensor.AccessSensorPK(accessId, sensorId);
        var acs = accessSensorRepo
                .findById(pk)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't found access"));
        accessSensorRepo.delete(acs);
    }

    public AccessSearch search(long userId, long accessId, String mixedCaseQuery, boolean sensor, boolean user, boolean in, boolean out) {
        final var query = mixedCaseQuery.toLowerCase();
        System.out.println("SEARCH : " + query);
        var ac = accessRepo
                .findById(accessId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find access"));


        var currentUser = userRepo
                .findById(userId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid User"));

        var res = new AccessSearch();

        if (!in && !out) return res;

        if (sensor) {
            var sensors = ac.getSensors();
            if (in) {
                for (var s: sensors)
                    if ((s.getName().toLowerCase().contains(query) || s.getDisplayName().toLowerCase().contains(query)))
                        res.put(s, true);
            }
            if (out) {
                for (var acu: currentUser.getAccessUsers())
                    if (acu.getAccess().getPrivilege() == AccessPrivilege.MAINTENANCE)
                        for (var s: acu.getAccess().getSensors())
                            if ((s.getName().toLowerCase().contains(query) || s.getDisplayName().toLowerCase().contains(query)) && !sensors.contains(s))
                                res.put(s, false);
            }
        }

        if (user) {
            var users = ac.getUsers();
            if (in) {
                var usersAccesses = ac.getAccessUsers();
                for (var acu : usersAccesses) {
                    var u = acu.getUser();
                    if (u.getFirstname().toLowerCase().contains(query) || u.getLastname().toLowerCase().contains(query))
                        res.put(u, true, acu.getPrivilege());
                }
            }
            if (out) {
                for (var u : userRepo.findAll())
                    if ((u.getFirstname().toLowerCase().contains(query) || u.getLastname().toLowerCase().contains(query)) && !users.contains(u))
                        res.put(u, false, null);
            }
        }

        return res;
    }

    public void guardManage(long accessId, long userId) {
        var access = accessRepo
                .findById(accessId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find access"));
        if (!access.manageableBy(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid permission");
    }


}
