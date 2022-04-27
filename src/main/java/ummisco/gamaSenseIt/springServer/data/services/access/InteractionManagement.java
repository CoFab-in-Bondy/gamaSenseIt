package ummisco.gamaSenseIt.springServer.data.services.access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.preference.InteractAccess;
import ummisco.gamaSenseIt.springServer.data.model.preference.InteractSensor;
import ummisco.gamaSenseIt.springServer.data.model.preference.InteractUser;
import ummisco.gamaSenseIt.springServer.data.repositories.IInteractAccessRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.IInteractSensorRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.IInteractUserRepository;

@Service
public class InteractionManagement {

    private static final Logger logger = LoggerFactory.getLogger(InteractionManagement.class);

    @Autowired
    private IInteractUserRepository interactUserRepo;

    @Autowired
    private IInteractSensorRepository interactSensorRepo;

    @Autowired
    private IInteractAccessRepository interactAccessRepo;

    public void touchSensorWithUser(long userId, long sensorId) {
        var pk = new InteractSensor.InteractSensorPK(userId, sensorId);
        interactSensorRepo.findById(pk).ifPresent(interactSensorRepo::delete);
        var interact = new InteractSensor();
        interact.setSensorId(sensorId);
        interact.setUserId(userId);
        interactSensorRepo.save(interact);
        logger.info("Sensor interaction of user(" + userId + ") on sensor (" + sensorId + ")");
    }

    public void touchAccessWithUser(long userId, long accessId) {
        var pk = new InteractAccess.InteractAccessPK(userId, accessId);
        interactAccessRepo.findById(pk).ifPresent(interactAccessRepo::delete);
        var interact = new InteractAccess();
        interact.setAccessId(accessId);
        interact.setUserId(userId);
        interactAccessRepo.save(interact);
        logger.info("Access interaction of user(" + userId + ") on access (" + accessId + ")");
    }

    public void touchUserWithUser(long userId, long targetId) {
        var pk = new InteractUser.InteractUserPK(userId, targetId);
        interactUserRepo.findById(pk).ifPresent(interactUserRepo::delete);
        var interact = new InteractUser();
        interact.setTargetId(targetId);
        interact.setUserId(userId);
        interactUserRepo.save(interact);
        logger.info("User interaction of user(" + userId + ") on user (" + targetId + ")");
    }
}
