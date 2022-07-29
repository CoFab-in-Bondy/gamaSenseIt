package fr.ummisco.gamasenseit.server.data.controller;

import com.fasterxml.jackson.annotation.JsonView;
import fr.ummisco.gamasenseit.arduino.exception.ArduinoException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import fr.ummisco.gamasenseit.server.data.classes.Node;
import fr.ummisco.gamasenseit.server.data.model.IView;
import fr.ummisco.gamasenseit.server.data.model.sensor.Sensor;
import fr.ummisco.gamasenseit.server.data.model.sensor.SensorDTO;
import fr.ummisco.gamasenseit.server.data.model.user.Access;
import fr.ummisco.gamasenseit.server.data.model.user.AccessDTO;
import fr.ummisco.gamasenseit.server.data.model.user.AccessUserPrivilege;
import fr.ummisco.gamasenseit.arduino.cli.Properties;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(Routes.PRIVATE)
public class PrivateDataController extends DataController {

    /*------------------------------------*
     | Server                             |
     *------------------------------------*/

    /*------------------------------------*
     | Parameters                         |
     *------------------------------------*/

    /*------------------------------------*
     | Parameters metadata                |
     *------------------------------------*/

    /*
    @CrossOrigin
    @RequestMapping(value = IRoute.PARAMETERS_METADATA, method = RequestMethod.POST)
    public ParameterMetadata addParameter(
            @RequestParam(value = IParametersRequest.SENSOR_METADATA_ID, defaultValue = NIL) long sensorMetadataId,
            @RequestParam(value = IParametersRequest.NAME, defaultValue = NIL) String name,
            @RequestParam(value = IParametersRequest.UNIT, defaultValue = NIL) String unit,
            @RequestParam(value = IParametersRequest.DATA_FORMAT, defaultValue = NIL) String format,
            @RequestParam(value = IParametersRequest.MEASURED_PARAMETER, defaultValue = NIL) String measuredParameter
    ) {
        var sensorMetadata = sensorsMetadataRepo.findById(sensorMetadataId);
        if (sensorMetadata.isEmpty())
            return null;

        ParameterMetadata.DataFormat dataFormat;
        DataParameter dataParameter;
        try {
            dataFormat = ParameterMetadata.DataFormat.valueOf(format);
            dataParameter = DataParameter.valueOf(measuredParameter);
        } catch (IllegalArgumentException e) {
            return null;
        }
        var parameterMetadata = new ParameterMetadata(name, unit, dataFormat, dataParameter);
        parameterMetadata = sensorsManagementRepo.addParameterToSensorMetadata(sensorMetadata.get(), parameterMetadata);
        return parameterMetadata;
    }
    */
    /*------------------------------------*
     | Sensors                            |
     *------------------------------------*/

    @RequestMapping(value = Routes.SENSORS, method = RequestMethod.POST)
    @JsonView(IView.Public.class)
    public Node addSensor(
            @RequestPart(value = "sensor") SensorDTO sensorDTO,
            @RequestParam(value = "photo", required = false) MultipartFile image
    ) {
        var sensor = new Sensor();

        if (image != null) {
            sensor.setPhoto(image);
        }

        // check if sensorMetadata exist
        var sensorMetadata = sensorsMetadataRepo.findById(sensorDTO.getSensorMetadataId());
        if (sensorMetadata.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meta-Capteur Invalide");
        }

        Optional<Sensor> sensorsOwnToken;
        String token;
        do {
            token = securityUtils.randomToken();
            sensorsOwnToken = sensorsRepo.findByToken(token);
        } while (sensorsOwnToken.isPresent());

        sensor.setSensorMetadata(sensorMetadata.get());
        sensor.setToken(token);
        sensor.setName(sensorDTO.getName());
        sensor.setIndications(sensorDTO.getIndications());
        sensor.setLongitude(sensorDTO.getLongitude());
        sensor.setLatitude(sensorDTO.getLatitude());
        sensor.setHidden(sensorDTO.isHidden());
        sensor.setHiddenMessage(sensorDTO.getHiddenMessage());
        sensor.setDescription(sensorDTO.getDescription());
        sensor.setMaintenanceDescription(sensorDTO.getMaintenanceDescription());
        sensor = sensorsManagement.addSensorForUser(sensor, currentUser().getId());

        interactionManagement.touchSensorWithUser(currentUser().getId(), sensor.getId());

        return sensor.toNode(true);
    }


    @RequestMapping(value = Routes.SENSORS + Routes.ID, method = RequestMethod.POST)
    @JsonView(IView.Public.class)
    public Node patchSensor(
            @PathVariable(name = "id") long sensorId,
            @RequestPart(value = "sensor") SensorDTO sensorDTO,
            @RequestPart(value = "photo", required = false) MultipartFile image
    ) {
        var sensor = sensorManage(sensorId);

        if (image != null) {
            sensor.setPhoto(image);
        }

        // check if sensorMetadata exist (can't change it)
        /*
        var sensorMetadata = sensorsMetadataRepo.findById(sensorDTO.getSensorMetadataId());
        if (sensorMetadata.isEmpty()) {
            System.err.println("Can't find sensor Metadata");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find sensor Metadata");
        }
        */

        // check if name is not already taken but ignore itself
        if (sensorDTO.getName() != null) {
            var selectedSensors = sensorsRepo.findByName(sensorDTO.getName());
            if (!selectedSensors.isEmpty() && (selectedSensors.size() == 1 && selectedSensors.get(0).getId() != sensorId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Nom déjà utilisé");
            }
            sensor.setToken(sensorDTO.getName());
        }

        if (sensorDTO.getName() != null)
            sensor.setName(sensorDTO.getName());

        if (sensorDTO.getLatitude() != null)
            sensor.setIndications(sensorDTO.getIndications());
        if (sensorDTO.getLongitude() != null)
            sensor.setLongitude(sensorDTO.getLongitude());

        if (sensorDTO.isHidden())
            sensor.setHidden(sensorDTO.isHidden());

        if (sensorDTO.getHiddenMessage() != null)
            sensor.setHiddenMessage(sensorDTO.getHiddenMessage());

        if (sensorDTO.getDescription() != null)
            sensor.setDescription(sensorDTO.getDescription());

        if (sensorDTO.getMaintenanceDescription() != null)
            sensor.setMaintenanceDescription(sensorDTO.getMaintenanceDescription());

        sensor = sensorsManagement.patchSensor(sensor);

        interactionManagement.touchSensorWithUser(currentUser().getId(), sensor.getId());

        return sensor.toNode(true);
    }

    @RequestMapping(value = Routes.ACCESSES, method = RequestMethod.POST)
    @JsonView(IView.AccessUser.class)
    public Access accessCreate(
            @RequestPart(value = "access") AccessDTO accessDTO
    ) {
        var access = accessManagement.createAccess(user().getId(), accessDTO.getName().strip(), accessDTO.getPrivilege());
        interactionManagement.touchAccessWithUser(user().getId(), access.getId());
        return access;
    }

    @RequestMapping(value = Routes.ACCESSES + Routes.ID + Routes.SEARCH, method = RequestMethod.GET)
    @JsonView(IView.AccessUser.class)
    public List<Node> accessByIdSearch(
            @PathVariable(name = "id") long accessId,
            @RequestParam(name = IParametersRequest.QUERY, defaultValue = "") String query,
            @RequestParam(name = IParametersRequest.SENSOR, defaultValue = "1") boolean sensor,
            @RequestParam(name = IParametersRequest.USER, defaultValue = "1") boolean user,
            @RequestParam(name = IParametersRequest.IN, defaultValue = "1") boolean in,
            @RequestParam(name = IParametersRequest.OUT, defaultValue = "1") boolean out
    ) {
        accessManagement.guardManage(accessId, currentUser().getId());
        return accessManagement.search(currentUser().getId(), accessId, query, sensor, user, in, out);
    }

    @RequestMapping(value = Routes.ACCESSES + Routes.ID, method = RequestMethod.GET)
    @JsonView(IView.AccessCount.class)
    public Access getAccessById(@PathVariable(name = "id") long accessId) {
        accessManagement.guardManage(accessId, currentUser().getId());
        return this.accessRepo.findById(accessId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't found this access"));
    }

    @RequestMapping(value = Routes.ACCESSES + Routes.SEARCH, method = RequestMethod.GET)
    @JsonView(IView.AccessCount.class)
    public List<Access> accessSearch(@RequestParam(name = IParametersRequest.QUERY, defaultValue = "") String query) {
        return this.accessManagement.search(currentUser(), query);
    }

    @RequestMapping(value = Routes.ACCESSES + Routes.ID + Routes.USERS, method = RequestMethod.POST)
    @JsonView(IView.AccessUser.class)
    public void accessByIdAddUser(@PathVariable(name = "id") long accessId, @RequestBody UserIdRecord userIdRecord) {
        accessManagement.guardManage(accessId, currentUser().getId());
        interactionManagement.touchUserWithUser(currentUser().getId(), userIdRecord.userId());
        interactionManagement.touchAccessWithUser(currentUser().getId(), accessId);
        accessManagement.addAccessUser(accessId, userIdRecord.userId());
    }

    @RequestMapping(value = Routes.ACCESSES + Routes.ID + Routes.USERS + "/{userId}", method = RequestMethod.DELETE)
    @JsonView(IView.AccessUser.class)
    public void accessByIdDelUser(@PathVariable(name = "id") long accessId, @PathVariable(name = "userId") long userId) {
        accessManagement.guardManage(accessId, currentUser().getId());
        interactionManagement.touchUserWithUser(currentUser().getId(), userId);
        interactionManagement.touchAccessWithUser(currentUser().getId(), accessId);
        accessManagement.delAccessUser(accessId, userId);
    }

    @RequestMapping(value = Routes.ACCESSES + Routes.ID + Routes.USERS + "/{userId}" + Routes.PROMOTE, method = RequestMethod.POST)
    @JsonView(IView.AccessUser.class)
    public void accessByIdPromoteUser(
            @PathVariable(name = "id") long accessId,
            @PathVariable(name = "userId") long userId
    ) {
        accessManagement.guardManage(accessId, currentUser().getId());
        interactionManagement.touchUserWithUser(currentUser().getId(), userId);
        interactionManagement.touchAccessWithUser(currentUser().getId(), accessId);
        accessManagement.promoteAccessUser(accessId, userId, AccessUserPrivilege.MANAGE);
    }

    @RequestMapping(value = Routes.ACCESSES + Routes.ID + Routes.USERS + "/{userId}" + Routes.DISMISE, method = RequestMethod.POST)
    @JsonView(IView.AccessUser.class)
    public void accessByIdDismissUser(
            @PathVariable(name = "id") long accessId,
            @PathVariable(name = "userId") long userId
    ) {
        accessManagement.guardManage(accessId, currentUser().getId());
        interactionManagement.touchUserWithUser(currentUser().getId(), userId);
        interactionManagement.touchAccessWithUser(currentUser().getId(), accessId);
        accessManagement.promoteAccessUser(accessId, userId, AccessUserPrivilege.VIEW);
    }

    @RequestMapping(value = Routes.ACCESSES + Routes.ID + Routes.SENSORS, method = RequestMethod.POST)
    @JsonView(IView.AccessUser.class)
    public void accessByIdAddSensor(@PathVariable(name = "id") long accessId, @RequestBody SensorIdRecord sensorIdRecord) {
        accessManagement.guardManage(accessId, currentUser().getId());
        interactionManagement.touchSensorWithUser(currentUser().getId(), sensorIdRecord.sensorId());
        interactionManagement.touchAccessWithUser(currentUser().getId(), accessId);
        accessManagement.addAccessSensor(accessId, sensorIdRecord.sensorId());
    }

    @RequestMapping(value = Routes.ACCESSES + Routes.ID + Routes.SENSORS + "/{sensorId}", method = RequestMethod.DELETE)
    @JsonView(IView.AccessUser.class)
    public void accessByIdDelSensor(@PathVariable(name = "id") long accessId, @PathVariable(name = "sensorId") long sensorId) {
        accessManagement.guardManage(accessId, currentUser().getId());
        interactionManagement.touchSensorWithUser(currentUser().getId(), sensorId);
        interactionManagement.touchAccessWithUser(currentUser().getId(), accessId);
        accessManagement.delAccessSensor(accessId, sensorId);
    }

    @RequestMapping(value = Routes.BINARY + Routes.TOKEN + Routes.ID, method = RequestMethod.POST)
    public Map<String, String> getBinaryToken(@PathVariable(name = "id") long sensorId, @RequestBody Map<String, Object> propMap) {
        var sensor = sensorManage(sensorId);
        var user = user();
        var prop = new Properties();
        var tempHumSensor = propMap.get("TEMP_HUM_SENSOR");
        var pmSensor = propMap.get("PM_SENSOR");

        if ("DHT_SENSOR".equals(tempHumSensor))
            prop.define("DHT_SENSOR", true);
        else if ("SHT_SENSOR".equals(tempHumSensor))
            prop.define("SHT_SENSOR", true);
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid TEMP_HUM_SENSOR");

        if ("NextPM_SENSOR".equals(pmSensor))
            prop.define("NextPM_SENSOR", true);
        else if ("PMS7000_SENSOR".equals(pmSensor))
            prop.define("PMS7000_SENSOR", true);
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid PM_SENSOR");

        try {
            String token = downloadManagement.generateDownloadToken(user, sensor, prop);
            return Collections.singletonMap("token", token);
        } catch (IOException | ArduinoException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't compile Sensor");
        }
    }

    public record UserIdRecord(long userId) {
    }

    public record SensorIdRecord(long sensorId) {
    }
}
