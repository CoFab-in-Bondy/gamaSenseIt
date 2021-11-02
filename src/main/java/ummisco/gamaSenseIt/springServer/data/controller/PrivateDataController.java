package ummisco.gamaSenseIt.springServer.data.controller;

import org.springframework.web.bind.annotation.*;
import ummisco.gamaSenseIt.springServer.data.model.*;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata.DataParameter;

@CrossOrigin
@RestController
@RequestMapping(IRoute.PRIVATE)
public class PrivateDataController extends DataController {

    public Sensor getSensorById(long id) {
        var sensor = sensorsRepo.findById(id);
        return sensor.isEmpty() ? null : sensor.get();
    }

    /*------------------------------------*
     | Server                             |
     *------------------------------------*/

    /*------------------------------------*
     | Parameters                         |
     *------------------------------------*/

    /*------------------------------------*
     | Parameters metadata                |
     *------------------------------------*/

    @CrossOrigin
    @RequestMapping(value = IRoute.PARAMETERS_METADATA, method = RequestMethod.POST)
    public DisplayableParameterMetadata addParameter(
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
        return new DisplayableParameterMetadata(parameterMetadata);
    }

    /*------------------------------------*
     | Sensors                            |
     *------------------------------------*/

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS, method = RequestMethod.POST)
    public DisplayableSensor addSensor(
            @RequestParam(value = IParametersRequest.SENSOR_METADATA_ID) long sensorMetadataId,
            @RequestParam(value = IParametersRequest.NAME, defaultValue = NIL) String name,
            @RequestParam(value = IParametersRequest.DISPLAY_NAME, defaultValue = NIL) String displayName,
            @RequestParam(value = IParametersRequest.SUB_DISPLAY_NAME, defaultValue = NIL) String subDisplayName,
            @RequestParam(value = IParametersRequest.LONGITUDE, defaultValue = "0") double longitude,
            @RequestParam(value = IParametersRequest.LATITUDE, defaultValue = "0") double latitude
    ) {
        var sensorMetadata = sensorsMetadataRepo.findById(sensorMetadataId);

        if (sensorMetadata.isEmpty())
            return null;

        // TODO raise an error this sensor already exist
        var selectedSensors = sensorsRepo.findByName(name);
        if (!selectedSensors.isEmpty())
            return selectedSensors.get(0).convert();

        Sensor sensor = new Sensor(name, displayName, subDisplayName, longitude, latitude, sensorMetadata.get());
        sensorsRepo.save(sensor);
        return sensor.convert();
    }

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS, method = RequestMethod.PATCH)
    public DisplayableSensor updateSensor(
            @RequestParam(value = IParametersRequest.SENSOR_ID) long sensorId,
            @RequestParam(value = IParametersRequest.NAME, required = false) String name,
            @RequestParam(value = IParametersRequest.DISPLAY_NAME, required = false) String displayName,
            @RequestParam(value = IParametersRequest.SUB_DISPLAY_NAME, required = false) String subDisplayName,
            @RequestParam(value = IParametersRequest.LONGITUDE, required = false) Double longitude,
            @RequestParam(value = IParametersRequest.LATITUDE, required = false) Double latitude
    ) {

        Sensor sensor = getSensorById(sensorId);
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

        sensorsRepo.save(sensor);
        return sensor.convert();
    }

    /*------------------------------------*
     | Sensors metadata                   |
     *------------------------------------*/

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA, method = RequestMethod.POST)
    public DisplayableSensorMetadata addSensorMetadata(
            @RequestParam(value = IParametersRequest.NAME, defaultValue = NIL) String name,
            @RequestParam(value = IParametersRequest.VERSION, defaultValue = NIL) String version,
            @RequestParam(value = IParametersRequest.DATA_SEPARATOR, defaultValue = SensorMetadata.DEFAULT_DATA_SEPARATOR) String sep,
            @RequestParam(value = IParametersRequest.MEASURED_DATA_ORDER, defaultValue = NIL) String measuredDataOrder,
            @RequestParam(value = IParametersRequest.DESCRIPTION, defaultValue = NIL) String description
    ) {
        var sensorsMetadata = sensorsMetadataRepo.findByNameAndVersion(name, version);
        if (!sensorsMetadata.isEmpty()) return null;

        var sensorMetadata = new SensorMetadata(name, version, sep, description);
        sensorMetadata.setMeasuredDataOrder(measuredDataOrder);
        sensorMetadata = this.sensorsMetadataRepo.save(sensorMetadata);
        return new DisplayableSensorMetadata(sensorMetadata);
    }
}
