package ummisco.gamaSenseIt.springServer.data.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ummisco.gamaSenseIt.springServer.data.classes.Node;
import ummisco.gamaSenseIt.springServer.data.services.record.RecordList;
import ummisco.gamaSenseIt.springServer.data.model.*;
import ummisco.gamaSenseIt.springServer.services.formatter.ExportJSON;

import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;


@RestController
@RequestMapping(IRoute.PUBLIC)
public class PublicDataController extends DataController {

    @Autowired
    private ExportJSON exportJSON;

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
    @JsonView(IView.Public.class)
    @RequestMapping(value = IRoute.PARAMETERS, method = RequestMethod.GET)
    public RecordList parameters(
            @RequestParam(value = IParametersRequest.SENSOR_ID) Sensor sensor,
            @RequestParam(value = IParametersRequest.PARAMETER_METADATA_ID, required = false) ParameterMetadata parameterMetadata,
            @RequestParam(value = IParametersRequest.START, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date start,
            @RequestParam(value = IParametersRequest.END, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date end,
            @RequestParam(value = IParametersRequest.SORT, defaultValue = "0") Integer index

    ) {
        return recordManager.getRecords(sensor, parameterMetadata, start, end).sortBy(index);
    }

    @CrossOrigin
    @JsonView(IView.Public.class)
    @RequestMapping(value = IRoute.PARAMETERS + IRoute.DOWNLOAD, method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadParameters(
            @RequestParam(value = IParametersRequest.SENSOR_ID) Sensor sensor,
            @RequestParam(value = IParametersRequest.TYPE) String type,
            @RequestParam(value = IParametersRequest.PARAMETER_METADATA_ID, required = false) ParameterMetadata parameterMetadata,
            @RequestParam(value = IParametersRequest.START, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date start,
            @RequestParam(value = IParametersRequest.END, required = false)
            @DateTimeFormat(pattern = IParametersRequest.DATE_PATTERN) Date end
    ) {
        return export.format(type, sensor, parameterMetadata, start, end);
    }

    /*------------------------------------*
     | Parameters metadata                |
     *------------------------------------*/

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

    /*------------------------------------*
     | Sensors                            |
     *------------------------------------*/

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS, method = RequestMethod.GET)
    public Iterable<Sensor> sensors() {
        return sensorsRepo.findAll();
    }


    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS + IRoute.ID, method = RequestMethod.GET)
    public Sensor sensorById(@PathVariable long id) {
        return sensorsRepo.findById(id).orElse(null);
    }

    @RequestMapping(value = IRoute.SENSORS + IRoute.ID + IRoute.EXTENDED, method = RequestMethod.GET)
    @JsonView(IView.Public.class)
    public Node sensorByIdExtended(
            @PathVariable long id
    ) {
        return sensorsRepo.findById(id).map(s-> exportJSON.toNode(s, null, null, null)).orElse(null);
    }

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS + IRoute.NAMES, method = RequestMethod.GET)
    public Iterable<String> sensorsNames() {
        return IterableUtils.toList(sensorsRepo.findAll()).stream().map(Sensor::getName).collect(Collectors.toList());
    }

    /*------------------------------------*
     | Sensors metadata                   |
     *------------------------------------*/

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA, method = RequestMethod.GET)
    public Iterable<SensorMetadata> sensorsMetadata() {
        return sensorsMetadataRepo.findAll();
    }


    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA + IRoute.ID + IRoute.SENSORS, method = RequestMethod.GET)
    public Iterable<Sensor> getSensorBySensorMetadataId(@PathVariable long id) {
        var smd = sensorsMetadataRepo.findById(id);
        return smd.isEmpty()? null : smd.get().getSensors();
    }

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA + IRoute.ID, method = RequestMethod.GET)
    public SensorMetadata sensorMetadataById(@PathVariable long id) {
        return sensorsMetadataRepo.findById(id).orElse(null);
    }

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA + IRoute.NAMES, method = RequestMethod.GET)
    public Iterable<String> sensorMetadataNames() {
        return apply(sensorsMetadataRepo.findAll(), smd -> smd.getName() + " -- " + smd.getVersion());
    }

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA + IRoute.ID + IRoute.PARAMETERS_METADATA, method = RequestMethod.GET)
    public Iterable<ParameterMetadata> getParametersMetadataBySensorMetadataId(
            @PathVariable long id) {
        var smd = sensorsMetadataRepo.findById(id);
        if (smd.isEmpty()) return null;
        var sensor = smd.get();
        return sensor.getParametersMetadata();
    }

    @CrossOrigin
    @RequestMapping(value = IRoute.SENSORS_METADATA + IRoute.EXTENDED, method = RequestMethod.GET)
    @JsonView(IView.SensorMetadataExtended.class)
    public Iterable<SensorMetadata> getAllSensorMetadataExtended() {
        return this.sensorsMetadataRepo.findAll();
    }
}
