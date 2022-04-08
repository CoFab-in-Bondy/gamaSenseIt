package ummisco.gamaSenseIt.springServer.data.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ummisco.gamaSenseIt.springServer.data.classes.Node;
import ummisco.gamaSenseIt.springServer.data.model.IView;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.sensor.SensorDTO;
import ummisco.gamaSenseIt.springServer.data.model.user.Access;
import ummisco.gamaSenseIt.springServer.data.model.user.AccessUserPrivilege;
import ummisco.gamaSenseIt.springServer.data.services.access.AccessSearch;

import java.io.IOError;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(IRoute.PRIVATE)
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

    @RequestMapping(value = IRoute.SENSORS, method = RequestMethod.POST)
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

        // check if name is not already taken
        var selectedSensors = sensorsRepo.findByName(sensorDTO.getName());
        if (!selectedSensors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Nom déjà utilisé");
        }

        sensor.setSensorMetadata(sensorMetadata.get());
        sensor.setName(sensorDTO.getName());
        sensor.setDisplayName(sensorDTO.getDisplayName());
        sensor.setSubDisplayName(sensorDTO.getSubDisplayName());
        sensor.setLongitude(sensorDTO.getLongitude());
        sensor.setLatitude(sensorDTO.getLatitude());
        sensor.setHidden(sensorDTO.isHidden());
        sensor.setHiddenMessage(sensorDTO.getHiddenMessage());
        sensor.setDescription(sensorDTO.getDescription());
        sensor.setMaintenanceDescription(sensorDTO.getMaintenanceDescription());
        sensor = sensorsManagement.addSensorForUser(sensor, currentUser().getId());

        return sensor.toNode(true);
    }


    @RequestMapping(value = IRoute.SENSORS + IRoute.ID, method = RequestMethod.POST)
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
            sensor.setName(sensorDTO.getName());
        }

        if (sensorDTO.getDisplayName() != null)
            sensor.setDisplayName(sensorDTO.getDisplayName());

        if (sensorDTO.getLatitude() != null)
            sensor.setSubDisplayName(sensorDTO.getSubDisplayName());
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
        return sensor.toNode(true);
    }



    /*
    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS, method = RequestMethod.PATCH)
    public Sensor updateSensor(
            @RequestParam(value = IParametersRequest.SENSOR_ID) long sensorId,
            @RequestParam(value = IParametersRequest.NAME, required = false) String name,
            @RequestParam(value = IParametersRequest.DISPLAY_NAME, required = false) String displayName,
            @RequestParam(value = IParametersRequest.SUB_DISPLAY_NAME, required = false) String subDisplayName,
            @RequestParam(value = IParametersRequest.LONGITUDE, required = false) Double longitude,
            @RequestParam(value = IParametersRequest.LATITUDE, required = false) Double latitude
    ) {

        Sensor sensor = sensor(sensorId);
        if (sensor == null)
            return null;

        if (name != null)
            sensor.setName(name);

        if (displayName != null)
            sensor.setDisplayName(displayName);

        if (subDisplayName != null)
            sensor.setSubDisplayName(subDisplayName);

        if (longitude != null)
            sensor.setLongitude(longitude);

        if (longitude != null)
            sensor.setLatitude(latitude);

        return sensorsRepo.save(sensor);
    }
    */

    /*------------------------------------*
     | Sensors metadata                   |
     *------------------------------------*/

    /*
    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA, method = RequestMethod.POST)
    public SensorMetadata addSensorMetadata(
            @RequestParam(value = IParametersRequest.NAME, defaultValue = NIL) String name,
            @RequestParam(value = IParametersRequest.VERSION, defaultValue = NIL) String version,
            @RequestParam(value = IParametersRequest.DATA_SEPARATOR, defaultValue = SensorMetadata.DEFAULT_DATA_SEPARATOR) String sep,
            @RequestParam(value = IParametersRequest.DESCRIPTION, defaultValue = NIL) String description
    ) {
        var sensorsMetadata = sensorsMetadataRepo.findByNameAndVersion(name, version);
        if (!sensorsMetadata.isEmpty()) return null;
        return this.sensorsMetadataRepo.save(new SensorMetadata(name, version, sep, description));
    }
    */

    @RequestMapping(value = IRoute.ACCESSES + IRoute.ID + IRoute.SEARCH, method = RequestMethod.GET)
    @JsonView(IView.AccessUser.class)
    public AccessSearch accessByIdSearch(
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

    @RequestMapping(value = IRoute.ACCESSES  + IRoute.ID, method = RequestMethod.GET)
    @JsonView(IView.AccessCount.class)
    public Access getAccessById(@PathVariable(name = "id") long accessId) {
        accessManagement.guardManage(accessId, currentUser().getId());
        return this.accessRepo.getById(accessId);
    }


    @RequestMapping(value = IRoute.ACCESSES  + IRoute.SEARCH, method = RequestMethod.GET)
    @JsonView(IView.AccessCount.class)
    public List<Access> accessSearch(@RequestParam(name = IParametersRequest.QUERY, defaultValue = "") String query) {
        query = "%" + query.replace("%", "%%") + "%";
        return this.accessRepo.searchManageableAccessByName(currentUser().getId(), query);
    }

    public record UserIdRecord(long userId) {}
    public record SensorIdRecord(long sensorId) {}

    @RequestMapping(value = IRoute.ACCESSES + IRoute.ID + IRoute.USERS, method = RequestMethod.POST)
    @JsonView(IView.AccessUser.class)
    public void accessByIdAddUser(@PathVariable(name = "id") long accessId, @RequestBody UserIdRecord userIdRecord) {
        accessManagement.guardManage(accessId, currentUser().getId());
        accessManagement.addAccessUser(accessId, userIdRecord.userId());
    }

    @RequestMapping(value = IRoute.ACCESSES + IRoute.ID + IRoute.USERS + "/{userId}", method = RequestMethod.DELETE)
    @JsonView(IView.AccessUser.class)
    public void accessByIdDelUser(@PathVariable(name = "id") long accessId, @PathVariable(name = "userId") long userId) {
        accessManagement.guardManage(accessId, currentUser().getId());
        accessManagement.delAccessUser(accessId, userId);
    }

    @RequestMapping(value = IRoute.ACCESSES + IRoute.ID + IRoute.USERS + "/{userId}" + IRoute.PROMOTE, method = RequestMethod.POST)
    @JsonView(IView.AccessUser.class)
    public void accessByIdPromoteUser(
            @PathVariable(name = "id") long accessId,
            @PathVariable(name = "userId") long userId
    ) {
        accessManagement.guardManage(accessId, currentUser().getId());
        accessManagement.promoteAccessUser(accessId, userId, AccessUserPrivilege.MANAGE);
    }

    @RequestMapping(value = IRoute.ACCESSES + IRoute.ID + IRoute.USERS + "/{userId}" + IRoute.DISMISE, method = RequestMethod.POST)
    @JsonView(IView.AccessUser.class)
    public void accessByIdDismissUser(
            @PathVariable(name = "id") long accessId,
            @PathVariable(name = "userId") long userId
    ) {
        accessManagement.guardManage(accessId, currentUser().getId());
        accessManagement.promoteAccessUser(accessId, userId, AccessUserPrivilege.VIEW);
    }

    @RequestMapping(value = IRoute.ACCESSES + IRoute.ID + IRoute.SENSORS, method = RequestMethod.POST)
    @JsonView(IView.AccessUser.class)
    public void accessByIdAddSensor(@PathVariable(name = "id") long accessId, @RequestBody SensorIdRecord sensorIdRecord) {
        accessManagement.guardManage(accessId, currentUser().getId());
        accessManagement.addAccessSensor(accessId, sensorIdRecord.sensorId());
    }

    @RequestMapping(value = IRoute.ACCESSES + IRoute.ID + IRoute.SENSORS + "/{sensorId}", method = RequestMethod.DELETE)
    @JsonView(IView.AccessUser.class)
    public void accessByIdDelSensor(@PathVariable(name = "id") long accessId, @PathVariable(name = "sensorId") long sensorId) {
        accessManagement.guardManage(accessId, currentUser().getId());
        accessManagement.delAccessSensor(accessId, sensorId);
    }
}
