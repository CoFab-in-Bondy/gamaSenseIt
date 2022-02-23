package ummisco.gamaSenseIt.springServer.data.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ummisco.gamaSenseIt.springServer.data.classes.Node;
import ummisco.gamaSenseIt.springServer.data.model.IView;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.SensorMetadata;
import ummisco.gamaSenseIt.springServer.data.services.record.RecordList;
import ummisco.gamaSenseIt.springServer.services.formatter.ExportJSON;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;


@RestController
@RequestMapping(IRoute.PUBLIC)
@CrossOrigin
public class PublicDataController extends DataController {

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
    public RecordList parameters(
            @RequestParam(value = IParametersRequest.SENSOR_ID) long sensorId,
            @RequestParam(value = IParametersRequest.PARAMETER_METADATA_ID, required = false) ParameterMetadata parameterMetadata,
            @RequestParam(value = IParametersRequest.START, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date start,
            @RequestParam(value = IParametersRequest.END, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date end,
            @RequestParam(value = IParametersRequest.SORT, defaultValue = "0") Integer index
    ) {
        var records = recordManager.getRecords(sensor(sensorId), parameterMetadata, start, end);
        records.sortBy(index, true);
        return records;
    }

    @JsonView(IView.Public.class)
    @RequestMapping(value = IRoute.PARAMETERS + IRoute.DOWNLOAD, method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadParameters(
            @RequestParam(value = IParametersRequest.SENSOR_ID) long sensorId,
            @RequestParam(value = IParametersRequest.TYPE) String type,
            @RequestParam(value = IParametersRequest.PARAMETER_METADATA_ID, required = false) ParameterMetadata parameterMetadata,
            @RequestParam(value = IParametersRequest.START, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date start,
            @RequestParam(value = IParametersRequest.END, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date end
    ) {
        return export.format(type, sensor(sensorId), parameterMetadata, start, end);
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
    public Node sensorByIdExtended(
            @PathVariable(name = "id") long sensorId,
            @RequestParam(name = IParametersRequest.START, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date start,
            @RequestParam(name = IParametersRequest.END, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date end,
            @RequestParam(name = IParametersRequest.SORT, required = false) Integer sort,
            @RequestParam(name = IParametersRequest.ASC, required = false) Boolean asc,
            @RequestParam(name = IParametersRequest.PAGE, required = false) Integer page,
            @RequestParam(name = IParametersRequest.COUNT, required = false) Integer count
    ) {
        System.out.println("MMMMH...");
        System.out.println("SENSORS : " + sensor(sensorId));
        return exportJSON.toNode(sensor(sensorId), null, start, end, sort, asc, page, count);
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
    public Iterable<SensorMetadata> getAllSensorMetadataExtended() {
        var smds = this.sensorsMetadataRepo.findAll();
        var smdsIter = smds.iterator();
        var sensors = this.sensorsRepo.findReadableSensors(user().getId());
        var checker = sensors.stream().map(Sensor::getId).collect(Collectors.toSet());
        while (smdsIter.hasNext()) {
            var smd = smdsIter.next();
            smd.setSensors(smd.getSensors()
                    .stream()
                    .filter(s -> checker.contains(s.getId()))
                    .collect(Collectors.toSet()));
            /*
            if (smd.getSensors().isEmpty())
                smdsIter.remove();
            */
        }
        return smds;
    }

    @RequestMapping(value = "geo", method = RequestMethod.GET)
    public Object processData(
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
            return new Node() {{
                put("lat", 48.856614);
                put("lng", 2.3522219);
            }};
        }
    }
}
