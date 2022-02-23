package ummisco.gamaSenseIt.springServer.data.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ummisco.gamaSenseIt.springServer.data.model.IView;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.SensorDTO;

@RestController
@RequestMapping(IRoute.PRIVATE)
@CrossOrigin
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

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS, method = RequestMethod.POST)
    @JsonView(IView.Public.class)
    public Sensor addSensor(
            @RequestBody SensorDTO sensorDTO
    ) {
        var sensorMetadata = sensorsMetadataRepo.findById(sensorDTO.getSensorMetadataId());
        if (sensorMetadata.isEmpty()) {
            System.err.println("Can't find sensor Metadata");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find sensor Metadata");
        }

        var selectedSensors = sensorsRepo.findByName(sensorDTO.getName());
        if (!selectedSensors.isEmpty()) {
            System.err.println("Name already exist");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Name already exist");
        }

        Sensor sensor = new Sensor(
                sensorDTO.getName(),
                sensorDTO.getDisplayName(),
                sensorDTO.getSubDisplayName(),
                sensorDTO.getLongitude(),
                sensorDTO.getLatitude(),
                sensorMetadata.get()
        );
        sensor.setHiddenMessage(sensorDTO.getHiddenMessage());
        sensor.setHidden(sensorDTO.getIsHidden());
        sensor = sensorsManagement.addSensorForUser(sensor, user().getId());
        System.err.println("Returning sensor");
        return sensor;
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
}
