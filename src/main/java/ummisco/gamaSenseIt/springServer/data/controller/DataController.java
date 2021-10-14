package ummisco.gamaSenseIt.springServer.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ummisco.gamaSenseIt.springServer.data.model.*;
import ummisco.gamaSenseIt.springServer.data.repositories.IParameterMetadataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorDataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorMetadataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorRepository;
import ummisco.gamaSenseIt.springServer.data.services.ISensorManagment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/public/")
public class DataController {
    final static String NIL_VALUE = "nil";

    @Autowired
    ISensorRepository sensors;

    @Autowired
    ISensorMetadataRepository sensorMetadata;

    @Autowired
    ISensorManagment sensorManagmentService;

    @Autowired
    ISensorMetadataRepository sensorTypeRepo;

    @Autowired
    ISensorDataRepository sensorData;

    @Autowired
    IParameterMetadataRepository metadataRepo;

    public DataController() {

    }

    public static <MT, D> List<D> createByApplying(
            Iterable<MT> findResult, Converter<MT, D> caster) {
        var result = new ArrayList<D>();
        for (var data : findResult)
            result.add(caster.cast(data));
        return result;
    }

    @CrossOrigin
    @RequestMapping(IDataController.SENSORS)
    public List<DisplayableSensor> getSensors() {
        return createByApplying(sensors.findAll(), DisplayableSensor::new);
    }

    @CrossOrigin
    @RequestMapping(IDataController.SENSOR)
    public DisplayableSensor getSensorById(
            @RequestParam(value = IDataController.SENSOR_ID) long id) {
        var sensor = sensors.findById(id);
        return sensor.isEmpty() ? null : new DisplayableSensor(sensor.get());
    }

    @CrossOrigin
    @RequestMapping(IDataController.SENSORS_NAMES)
    public List<String> getSensorsNames() {
        return createByApplying(sensors.findAll(), Sensor::getName);
    }

    @CrossOrigin
    @RequestMapping(IDataController.SENSOR_META_DATA_FULLNAMES)
    public List<String> getSensorMetadataName() {
        return createByApplying(sensorMetadata.findAll(),
                s -> s.getName() + " -- " + s.getVersion());
    }

    @CrossOrigin
    @RequestMapping(IDataController.META_DATA)
    public List<DisplayableParameterMetadata> getMetadata() {
        return createByApplying(metadataRepo.findAll(), DisplayableParameterMetadata::new);
    }

    @CrossOrigin
    @RequestMapping(IDataController.META_DATA_ID)
    public DisplayableParameterMetadata getMetadataByParameterId(
            @RequestParam(value = IDataController.PARAMETER_ID) long id) {
        var mt = metadataRepo.findById(id);
        return mt.isEmpty() ? null : new DisplayableParameterMetadata(mt.get());
    }

    @CrossOrigin
    @RequestMapping(IDataController.SENSOR_META_DATA)
    public List<DisplayableSensorMetadata> getSensorMetadata() {
        return createByApplying(sensorMetadata.findAll(), DisplayableSensorMetadata::new);
    }

    @CrossOrigin
    @RequestMapping(IDataController.META_DATA_SENSOR_META_DATA_ID)
    public List<DisplayableParameterMetadata> getSensorMetadata(
            @RequestParam(value = IDataController.METADATA_ID) long id) {
        var mt = sensorMetadata.findById(id);
        if (mt.isEmpty()) return null;
        var sensor = mt.get();
        return createByApplying(sensor.getParameterMetadata(),
                DisplayableParameterMetadata::new);
    }

    @CrossOrigin
    @RequestMapping(
            value = IDataController.SENSOR_DATA_SINCE_DATE,
            method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DisplayableData> getDataAfter(
            @RequestParam(value = IDataController.SENSOR_ID) long id,
            @RequestParam(value = IDataController.PARAMETER_ID) long idParam,
            @RequestParam(value = IDataController.BEGIN_DATE)
            @DateTimeFormat(pattern = IDataController.DATE_PATTERN) Date start
    ) {
        Calendar dte = Calendar.getInstance();
        dte.add(Calendar.DAY_OF_MONTH, 1);
        return getDataBetween(id, idParam, start, dte.getTime());
    }

    @CrossOrigin
    @RequestMapping(value = IDataController.SENSOR_DATA_BETWEEN_DATES)
    public List<DisplayableData> getDataBetween(
            @RequestParam(value = IDataController.SENSOR_ID) long id,
            @RequestParam(value = IDataController.PARAMETER_ID) long idParam,
            @RequestParam(value = IDataController.BEGIN_DATE)
            @DateTimeFormat(pattern = IDataController.DATE_PATTERN) Date start,
            @RequestParam(value = IDataController.END_DATE)
            @DateTimeFormat(pattern = IDataController.DATE_PATTERN) Date endDate) {
        List<SensorData> dts = this.sensorData.findAllByDate(id, idParam, start, endDate);
        return buildList(dts);
    }

    @CrossOrigin
    @RequestMapping(value = IDataController.SENSOR_DATA)
    public List<DisplayableData> getDataBetween() {
        Iterable<SensorData> dts = this.sensorData.findAll();
        return buildList(dts);
    }

    @CrossOrigin
    @RequestMapping(value = IDataController.SERVER_DATE)
    public long getServerDate() {
        return Calendar.getInstance().getTimeInMillis() / 1000;
    }

    private List<DisplayableData> buildList(Iterable<SensorData> dts) {
        return createByApplying(dts, dt -> new DisplayableData(
                dt.getDataObject(),
                dt.getCaptureDate(),
                dt.getParameter().getUnit(),
                dt.getSensor().getName(),
                dt.getSensor().getLatitude(),
                dt.getSensor().getLongitude(),
                dt.getParameter().getParameter().toString()
        ));
    }

    @CrossOrigin
    @RequestMapping(value = IDataController.DEFAULT_DATA_SEPARATOR)
    public String getDefaultDataSeparator() {
        return SensorMetadata.DEFAULT_DATA_SEPARATOR;
    }

    interface Converter<MT, D> {
        D cast(MT mt);
    }
}
