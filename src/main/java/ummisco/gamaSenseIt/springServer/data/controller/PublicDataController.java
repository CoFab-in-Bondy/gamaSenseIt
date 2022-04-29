package ummisco.gamaSenseIt.springServer.data.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ummisco.gamaSenseIt.springServer.Application;
import ummisco.gamaSenseIt.springServer.data.classes.Node;
import ummisco.gamaSenseIt.springServer.data.model.IView;
import ummisco.gamaSenseIt.springServer.data.model.sensor.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.sensor.SensorMetadata;
import ummisco.gamaSenseIt.springServer.services.export.ExportJSON;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;


@RestController
@RequestMapping(IRoute.PUBLIC)
public class PublicDataController extends DataController {

    private static final Logger logger = LoggerFactory.getLogger(PublicDataController.class);

    @Autowired
    private ExportJSON exportJSON;

    /*------------------------------------*
     | Server                             |
     *------------------------------------*/

    @RequestMapping(value = IRoute.SERVER + IRoute.DATE, method = RequestMethod.GET)
    public long serverDate() {
        return Calendar.getInstance().getTimeInMillis() / 1000;
    }

    @RequestMapping(value = IRoute.SERVER + IRoute.SEPARATOR, method = RequestMethod.GET)
    public String serverSeparator() {
        return SensorMetadata.DEFAULT_DATA_SEPARATOR;
    }

    /*------------------------------------*
     | Parameters                         |
     *------------------------------------*/

    @JsonView(IView.Public.class)
    @RequestMapping(value = IRoute.PARAMETERS, method = RequestMethod.GET)
    public Node parameters(
            @RequestParam(value = IParametersRequest.SENSOR_ID) long sensorId,
            @RequestParam(value = IParametersRequest.START, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date start,
            @RequestParam(value = IParametersRequest.END, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date end,
            @RequestParam(name = IParametersRequest.SORT, required = false) Integer sort,
            @RequestParam(name = IParametersRequest.ASC, required = false) Boolean asc,
            @RequestParam(name = IParametersRequest.PAGE, required = false) Integer page,
            @RequestParam(name = IParametersRequest.COUNT, required = false) Integer count
    ) {
        return exportJSON.toNode(sensorRead(sensorId), null, start, end, sort, asc, page, count, false);
    }

    @JsonView(IView.Public.class)
    @RequestMapping(value = IRoute.PARAMETERS + IRoute.DOWNLOAD, method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadParameters(
            @RequestParam(value = IParametersRequest.SENSOR_ID) long sensorId,
            @RequestParam(value = IParametersRequest.TYPE) String type,
            @RequestParam(value = IParametersRequest.PARAMETER_METADATA_ID, required = false)ParameterMetadata parameterMetadata,
            @RequestParam(value = IParametersRequest.START, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date start,
            @RequestParam(value = IParametersRequest.END, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date end
    ) {
        return export.format(type, sensorRead(sensorId), parameterMetadata, start, end);
    }

    /*------------------------------------*
     | Parameters metadata                |
     *------------------------------------*/

    /* FIXME : Security issue
    @CrossOrigin
    @RequestMapping(value = IRoute.PARAMETERS_METADATA, method = RequestMethod.GET)
    public Iterable<ParameterMetadata> parametersMetadata() {
        return parametersMetadataRepo.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = IRoute.PARAMETERS_METADATA + IRoute.ID, method = RequestMethod.GET)
    public ParameterMetadata parametersMetadataById(@PathVariable long id) {
        return parametersMetadataRepo.findById(id).orElse(null);
    }
    */

    /*------------------------------------*
     | Sensors                            |
     *------------------------------------*/

    /*
    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS, method = RequestMethod.GET)
    public Iterable<Sensor> sensors() {
        return null; //sensorsRepo.findSensorsByUserId(currentUserIdOrNull());
    }
    */

    /*
    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS + IRoute.ID, method = RequestMethod.GET)
    public Sensor sensorById(@PathVariable long id) {
        return sensor(id);
    }
    */

    @RequestMapping(value = IRoute.SENSORS + IRoute.ID, method = RequestMethod.GET)
    public Node sensor(@PathVariable(name = "id") long sensorId) {
        try {
            return sensorManage(sensorId).toNode(true);
        } catch (ResponseStatusException err) {
            return sensorRead(sensorId).toNode(false);
        }
    }

    @RequestMapping(value = IRoute.SENSORS + IRoute.ID + IRoute.IMAGE, method = RequestMethod.GET)
    public ResponseEntity<?> sensorImage(@PathVariable(name = "id") long sensorId) {
        var s = sensorRead(sensorId);
        return img(s.getName(), s.getPhoto());
    }

    @RequestMapping(value = IRoute.SENSORS + IRoute.METADATA + IRoute.ID + IRoute.IMAGE, method = RequestMethod.GET)
    public ResponseEntity<?> sensorMetadataImage(@PathVariable(name = "id") long sensorMetadataId) {
        var s = sensorsMetadataRepo
                .findById(sensorMetadataId)
                .orElseThrow((()->new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return img(s.getName(), s.getIcon());
    }

    /* FIXME : Security issue
    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS + IRoute.NAMES, method = RequestMethod.GET)
    public Iterable<String> sensorsNames() {
        return IterableUtils.toList(sensorsRepo.findAll()).stream().map(Sensor::getName).collect(Collectors.toList());
    }
    */

    /*------------------------------------*
     | Sensors metadata                   |
     *------------------------------------*/

    /* FIXME : Security issue
    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA, method = RequestMethod.GET)
    public Iterable<SensorMetadata> sensorsMetadata() {
        return sensorsMetadataRepo.findAll();
    }
    */
/*

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA + IRoute.ID + IRoute.SENSORS, method = RequestMethod.GET)
    public Iterable<Sensor> getSensorBySensorMetadataId(@PathVariable long id) {
        return null; //sensorsRepo.findSensorsBySensorMetadataId(currentUserIdOrNull(), id);
    }

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA + IRoute.ID, method = RequestMethod.GET)
    public SensorMetadata sensorMetadataById(@PathVariable long id) {
        return sensorsMetadataRepo.findById(id).orElse(null);
    }*/

    /* FIXME : Security issue
    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA + IRoute.NAMES, method = RequestMethod.GET)
    public Iterable<String> sensorMetadataNames() {
        return apply(sensorsMetadataRepo.findAll(), smd -> smd.getName() + " -- " + smd.getVersion());
    }
    */

    /*
    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA + IRoute.ID + IRoute.PARAMETERS_METADATA, method = RequestMethod.GET)
    public Iterable<ParameterMetadata> getParametersMetadataBySensorMetadataId(
            @PathVariable long id) {
        var smd = sensorsMetadataRepo.findById(id);
        return smd.map(SensorMetadata::getParametersMetadata).orElse(Collections.emptyList());
    }
    */

    @RequestMapping(value = IRoute.SENSORS_METADATA, method = RequestMethod.GET)
    @JsonView(IView.SensorMetadataExtended.class)
    public Iterable<SensorMetadata> getAllSensorMetadataExtended(
            @RequestParam(name = IParametersRequest.MANAGEABLE, defaultValue = "false") boolean manageable) {
        var smds = this.sensorsMetadataRepo.findAll();
        var smdsIter = smds.iterator();
        var sensorsId = new HashSet<>();
        if (manageable) {
            sensorsId.addAll(this.sensorsRepo.findManageableSensors(user().getId()));
            if (user() != publicUser())
                sensorsId.addAll(this.sensorsRepo.findManageableSensors(publicUser().getId()));
        } else {
            sensorsId.addAll(this.sensorsRepo.findReadableSensors(user().getId()));
            if (user() != publicUser())
                sensorsId.addAll(this.sensorsRepo.findReadableSensors(publicUser().getId()));
        }
        while (smdsIter.hasNext()) {
            var smd = smdsIter.next();
            smd.setSensors(smd.getSensors()
                    .stream()
                    .filter(s -> sensorsId.contains(s.getId()))
                    .collect(Collectors.toSet()));
            /*
            if (smd.getSensors().isEmpty())
                smdsIter.remove();
            */
        }
        return smds;
    }

    @RequestMapping(value = "geo", method = RequestMethod.GET)
    public Object geo(
            HttpServletRequest request
    ) {
        var city = geoService.geolocate(request);
        if (city.isPresent()) {
            var loc = city.get().getLocation();
            return new Node() {{
                put("lat", loc.getLatitude());
                put("lng", loc.getLongitude());
            }};
        } else {
            // can't find location, use server default
            return new Node() {{
                put("lat", 48.856614);
                put("lng", 2.3522219);
            }};
        }
    }
}
