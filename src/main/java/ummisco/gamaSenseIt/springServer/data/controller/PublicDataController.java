package ummisco.gamaSenseIt.springServer.data.controller;

import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ummisco.gamaSenseIt.springServer.data.model.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(IRoute.PUBLIC)
public class PublicDataController extends DataController {

    /*------------------------------------*
     | Server                             |
     *------------------------------------*/

    @CrossOrigin
    @RequestMapping(value = IRoute.SERVER + IRoute.DATE, method = RequestMethod.GET)
    public long serverDate() {
        return Calendar.getInstance().getTimeInMillis() / 1000;
    }

    @CrossOrigin
    @RequestMapping(value = IRoute.SERVER + IRoute.SEPARATOR, method = RequestMethod.GET)
    public String serverSeparator() {
        return SensorMetadata.DEFAULT_DATA_SEPARATOR;
    }

    /*------------------------------------*
     | Parameters                         |
     *------------------------------------*/

    @CrossOrigin
    @RequestMapping(value = IRoute.PARAMETERS, method = RequestMethod.GET)
    public ResponseEntity<Resource> parametersMetadata(
            @RequestParam(value = IParametersRequest.SENSOR_ID) long sensorId,
            @RequestParam(value = IParametersRequest.PARAMETER_METADATA_ID, required = false) Long parameterMetadataId,
            @RequestParam(value = IParametersRequest.START, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date start,
            @RequestParam(value = IParametersRequest.END, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date end,
            @RequestParam(value = IParametersRequest.TYPE, defaultValue = "json") String type
    ) {
        var parameters = parametersRepo.advancedFindAll(sensorId, parameterMetadataId, start, end);
        return formattedResponseFactory.format(type, display(parameters));
    }

    /*------------------------------------*
     | Parameters metadata                |
     *------------------------------------*/

    @CrossOrigin
    @RequestMapping(value = IRoute.PARAMETERS_METADATA, method = RequestMethod.GET)
    public List<DisplayableParameterMetadata> parametersMetadata() {
        return display(parametersMetadataRepo.findAll());
    }

    @CrossOrigin
    @RequestMapping(value = IRoute.PARAMETERS_METADATA + IRoute.ID, method = RequestMethod.GET)
    public DisplayableParameterMetadata parametersMetadataById(@PathVariable long id) {
        return display(parametersMetadataRepo.findById(id));
    }

    /*------------------------------------*
     | Sensors                            |
     *------------------------------------*/

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS, method = RequestMethod.GET)
    public List<DisplayableSensor> sensors() {
        return display(sensorsRepo.findAll());
    }

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS + IRoute.ID, method = RequestMethod.GET)
    public DisplayableSensor sensorById(@PathVariable long id) {
        return display(sensorsRepo.findById(id));
    }

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS + IRoute.NAMES, method = RequestMethod.GET)
    public List<String> sensorsNames() {
        return display(sensorsRepo.findAll(), Sensor::getName);
    }

    /*------------------------------------*
     | Sensors metadata                   |
     *------------------------------------*/

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA, method = RequestMethod.GET)
    public List<DisplayableSensorMetadata> sensorsMetadata() {
        return display(sensorsMetadataRepo.findAll());
    }

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA + IRoute.NAMES, method = RequestMethod.GET)
    public List<String> sensorMetadataById() {
        return display(sensorsMetadataRepo.findAll(),
                smd -> smd.getName() + " -- " + smd.getVersion());
    }

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA + IRoute.ID + IRoute.PARAMETERS_METADATA, method = RequestMethod.GET)
    public List<DisplayableParameterMetadata> parameterMetadataOfSensorMetatdataById(
            @PathVariable long id) {
        var smd = sensorsMetadataRepo.findById(id);
        if (smd.isEmpty()) return null;
        var sensor = smd.get();
        return display(sensor.getParameterMetadataById());
    }
}
