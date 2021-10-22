package ummisco.gamaSenseIt.springServer.data.controller;

import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ummisco.gamaSenseIt.springServer.data.model.*;

import java.util.*;


@RestController
@RequestMapping(Route.PUBLIC)
public class PublicDataController extends DataController {

    /*-----------------------*
     | Data                  |
     *-----------------------*/

    @CrossOrigin
    @RequestMapping(value = Route.DATA, method = RequestMethod.GET)
    public ResponseEntity<Resource> data(
            @RequestParam(value = Param.SENSOR_ID) long sensorId,
            @RequestParam(value = Param.PARAMETER_ID, required = false) Long parameterId,
            @RequestParam(value = Param.START, required = false) @DateTimeFormat(pattern = Param.DATE_PATTERN) Date start,
            @RequestParam(value = Param.END, required = false) @DateTimeFormat(pattern = Param.DATE_PATTERN)Date end,
            @RequestParam(value = Param.TYPE, defaultValue = "json") String type
    ) {
        return formatter.format(type, display(sensorsData.advancedFindAll(sensorId, parameterId, start, end)));
    }

    /*-----------------------*
     | Sensors               |
     *-----------------------*/

    @CrossOrigin
    @RequestMapping(value = Route.SENSORS, method = RequestMethod.GET)
    public List<DisplayableSensor> sensors() {
        return display(sensors.findAll());
    }

    @CrossOrigin
    @RequestMapping(value = Route.SENSORS + Route.ID, method = RequestMethod.GET)
    public DisplayableSensor sensorById(@PathVariable long id) {
        return display(sensors.findById(id));
    }

    @CrossOrigin
    @RequestMapping(value = Route.SENSORS + Route.NAMES, method = RequestMethod.GET)
    public List<String> sensorsNames() {
        return display(sensors.findAll(), Sensor::getName);
    }

    /*-----------------------*
     | Types                 |
     *-----------------------*/

    @CrossOrigin
    @RequestMapping(value = Route.TYPES, method = RequestMethod.GET)
    public List<DisplayableSensorMetadata> types() {
        return display(sensorsMetadata.findAll());
    }

    @CrossOrigin
    @RequestMapping(value = Route.TYPES + Route.NAMES, method = RequestMethod.GET)
    public List<String> typeById() {
        return display(sensorsMetadata.findAll(),
                s -> s.getName() + " -- " + s.getVersion());
    }

    @CrossOrigin
    @RequestMapping(value = Route.TYPES + Route.ID + Route.PARAMETERS, method = RequestMethod.GET)
    public List<DisplayableParameterMetadata> parameterOfTypesById(
            @PathVariable long id) {
        var mt = sensorsMetadata.findById(id);
        if (mt.isEmpty()) return null;
        var sensor = mt.get();
        return display(sensor.getParameterMetadata());
    }

    /*-----------------------*
     | Parameters            |
     *-----------------------*/

    @CrossOrigin
    @RequestMapping(value = Route.PARAMETERS, method = RequestMethod.GET)
    public List<DisplayableParameterMetadata> parameters() {
        return display(parametersMetadata.findAll());
    }

    @CrossOrigin
    @RequestMapping(value = Route.PARAMETERS + Route.ID, method = RequestMethod.GET)
    public DisplayableParameterMetadata parametersById(@PathVariable long id) {
        return display(parametersMetadata.findById(id));
    }

    /*-----------------------*
     | Server                |
     *-----------------------*/

    @CrossOrigin
    @RequestMapping(value = Route.SERVER + Route.DATE, method = RequestMethod.GET)
    public long serverDate() {
        return Calendar.getInstance().getTimeInMillis() / 1000;
    }

    @CrossOrigin
    @RequestMapping(value = Route.SERVER + Route.SEPARATOR, method = RequestMethod.GET)
    public String serverSeparator() {
        return SensorMetadata.DEFAULT_DATA_SEPARATOR;
    }
}
