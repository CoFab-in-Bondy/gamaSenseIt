package ummisco.gamaSenseIt.springServer.data.services.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import ummisco.gamaSenseIt.springServer.data.classes.Node;
import ummisco.gamaSenseIt.springServer.data.controller.IParametersRequest;
import ummisco.gamaSenseIt.springServer.data.model.user.*;
import ummisco.gamaSenseIt.springServer.data.repositories.*;

import java.util.*;

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

    /**
     * Create group and creator as manager.
     */
    public Access createAccess(long userId, String name, AccessPrivilege privilege) {
        var access = new Access();
        access.setPrivilege(privilege);
        access.setName(name);
        access = accessRepo.save(access);
        var acu = new AccessUser();
        acu.setPrivilege(AccessUserPrivilege.MANAGE);
        acu.setUserId(userId);
        acu.setAccessId(access.getId());
        accessUserRepo.save(acu);
        return access;
    }

    /**
     * Add view access to a group.
     */
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


    /**
     * Delete a user from a group.
     */
    public void delAccessUser(long accessId, long userId) {
        var pk = new AccessUser.AccessUserPK(accessId, userId);
        var acu = accessUserRepo
                .findById(pk)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't found access"));
        accessUserRepo.delete(acu);
    }

    /**
     * Change privilege of user in group.
     */
    public AccessUser promoteAccessUser(long accessId, long userId, AccessUserPrivilege accessUserPrivilege) {
        var pk = new AccessUser.AccessUserPK(accessId, userId);
        var acu = accessUserRepo
                .findById(pk)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't have access"));
        acu.setPrivilege(accessUserPrivilege);
        return accessUserRepo.save(acu);
    }


    /**
     * Add sensor to group.
     */
    public AccessSensor addAccessSensor(long accessId, long sensorId) {
        var pk = new AccessSensor.AccessSensorPK(accessId, sensorId);
        if (accessSensorRepo.findById(pk).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "sensor already have an access");
        var acu = new AccessSensor();
        acu.setSensorId(sensorId);
        acu.setAccessId(accessId);
        return accessSensorRepo.save(acu);
    }

    /**
     * Delete a sensor from a group.
     */
    public void delAccessSensor(long accessId, long sensorId) {
        var pk = new AccessSensor.AccessSensorPK(accessId, sensorId);
        var acs = accessSensorRepo
                .findById(pk)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't found access"));
        accessSensorRepo.delete(acs);
    }

    /**
     * Search a group.
     */
    public List<Access> search(User user, String query) {
        query = query.toLowerCase();
        var accesses = new ArrayList<Access>();
        for (var accessOfUser : user.getAccessUsers()) {
            var access = accessOfUser.getAccess();
            if (accessOfUser.getPrivilege() == AccessUserPrivilege.MANAGE
                    && access.getName().toLowerCase().contains(query)){
                accesses.add(access);
            }
        }

        Comparator<Access> cmp = Comparator.comparing(access -> access.getDateInteractWithUser(user.getId()));
        accesses.sort(cmp.reversed().thenComparing(Access::getName));
        return accesses;
    }


    /**
     * Search a sensor or user in a group.
     */
    public List<Node> search(long userId, long accessId, String mixedCaseQuery, boolean sensor, boolean user, boolean in, boolean out) {
        final var query = mixedCaseQuery.toLowerCase();
        var access = accessRepo
                .findById(accessId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find access"));

        var currentUser = userRepo
                .findById(userId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid User"));

        if (!in && !out) return Collections.emptyList();

        var res = new AccessSearch();

        if (sensor) {
            var sensors = access.getSensors();
            if (in) {
                for (var s: sensors)
                    if ((s.getName().toLowerCase().contains(query)
                            || s.getDisplayName().toLowerCase().contains(query)))
                        res.put(s, true);
            }
            if (out) {
                // avoid duplicate
                var addedSensorsId = new HashSet<Long>();

                for (var accessUser: currentUser.getAccessUsers()) {

                    var otherAccess = accessUser.getAccess();
                    if (otherAccess.getPrivilege() != AccessPrivilege.MAINTENANCE)
                        continue;

                    for (var s: otherAccess.getSensors()) {
                        if ((s.getName().toLowerCase().contains(query)
                                || s.getDisplayName().toLowerCase().contains(query))
                                && !sensors.contains(s)
                                && !addedSensorsId.contains(s.getId())) {
                            addedSensorsId.add(s.getId());
                            res.put(s, false);
                        }
                    }
                }
            }
        }

        if (user) {
            var users = access.getUsers();
            if (in) {
                var usersAccesses = access.getAccessUsers();
                for (var acu : usersAccesses) {
                    var u = acu.getUser();
                    if (u.getFirstname().toLowerCase().contains(query)
                            || u.getLastname().toLowerCase().contains(query))
                        res.put(u, true, acu.getPrivilege());
                }
            }
            if (out) {
                for (var u : userRepo.findAll())
                    if ((u.getFirstname().toLowerCase().contains(query)
                            || u.getLastname().toLowerCase().contains(query))
                            && !users.contains(u))
                        res.put(u, false, null);
            }
        }

        return res.toNodeForUser(currentUser);
    }

    /**
     * Check if a user can manage a given group.
     */
    public void guardManage(long accessId, long userId) {
        var access = accessRepo
                .findById(accessId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find access"));
        if (!access.manageableBy(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid permission");
    }
}
