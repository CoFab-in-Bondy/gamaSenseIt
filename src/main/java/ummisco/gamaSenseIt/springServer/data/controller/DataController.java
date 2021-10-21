package ummisco.gamaSenseIt.springServer.data.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
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
import ummisco.gamaSenseIt.springServer.services.csvFormatter.IFormatter;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// TODO function should have the same name as their route
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
    
    @Autowired
    IFormatter formatter;

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
    @RequestMapping(IDataController.GET_SENSORS)
    public List<DisplayableSensor> getSensors() {
        return createByApplying(sensors.findAll(), DisplayableSensor::new);
    }

    @CrossOrigin
    @RequestMapping(IDataController.GET_SENSOR_BY_ID)
    public DisplayableSensor getSensorById(
            @RequestParam(value = IDataController.SENSOR_ID) long id) {
        var sensor = sensors.findById(id);
        return sensor.isEmpty() ? null : new DisplayableSensor(sensor.get());
    }

    @CrossOrigin
    @RequestMapping(IDataController.GET_SENSORS_NAMES)
    public List<String> getSensorsNames() {
        return createByApplying(sensors.findAll(), Sensor::getName);
    }

    @CrossOrigin
    @RequestMapping(IDataController.GET_SENSOR_TYPE_NAMES)
    public List<String> getSensorTypeNames() {
        return createByApplying(sensorMetadata.findAll(),
                s -> s.getName() + " -- " + s.getVersion());
    }

    @CrossOrigin
    @RequestMapping(IDataController.GET_META_DATA)
    public List<DisplayableParameterMetadata> getMetadata() {
        return createByApplying(metadataRepo.findAll(), DisplayableParameterMetadata::new);
    }

    @CrossOrigin
    @RequestMapping(IDataController.GET_METADATA_BY_PAREMETER_ID)
    public DisplayableParameterMetadata getMetadataByParameterId(
            @RequestParam(value = IDataController.PARAMETER_ID) long id) {
        var mt = metadataRepo.findById(id);
        return mt.isEmpty() ? null : new DisplayableParameterMetadata(mt.get());
    }

    @CrossOrigin
    @RequestMapping(IDataController.GET_SENSOR_METADATA)
    public List<DisplayableSensorMetadata> getSensorMetadata() {
        return createByApplying(sensorMetadata.findAll(), DisplayableSensorMetadata::new);
    }

    @CrossOrigin
    @RequestMapping(IDataController.GET_METADATA_BY_SENSOR_METADATA_ID)
    public List<DisplayableParameterMetadata> getMetadataBySensorMetadataId(
            @RequestParam(value = IDataController.METADATA_ID) long id) {
        var mt = sensorMetadata.findById(id);
        if (mt.isEmpty()) return null;
        var sensor = mt.get();
        return createByApplying(sensor.getParameterMetadata(),
                DisplayableParameterMetadata::new);
    }

    @CrossOrigin
    @RequestMapping(
            value = IDataController.GET_DATA_OF_SENSOR_SINCE_DATE,
            method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DisplayableData> getDataOfSensorSinceData(
            @RequestParam(value = IDataController.SENSOR_ID) long id,
            @RequestParam(value = IDataController.PARAMETER_ID) long idParam,
            @RequestParam(value = IDataController.BEGIN_DATE)
            @DateTimeFormat(pattern = IDataController.DATE_PATTERN) Date start
    ) {
        Calendar dte = Calendar.getInstance();
        dte.add(Calendar.DAY_OF_MONTH, 1);
        return getDataOfSensorBetweenDate(id, idParam, start, dte.getTime());
    }

    @CrossOrigin
    @RequestMapping(value = IDataController.GET_DATA_OF_SENSOR_BETWEEN_DATE)
    public List<DisplayableData> getDataOfSensorBetweenDate(
            @RequestParam(value = IDataController.SENSOR_ID) long id,
            @RequestParam(value = IDataController.PARAMETER_ID) long idParam,
            @RequestParam(value = IDataController.BEGIN_DATE)
            @DateTimeFormat(pattern = IDataController.DATE_PATTERN) Date start,
            @RequestParam(value = IDataController.END_DATE)
            @DateTimeFormat(pattern = IDataController.DATE_PATTERN) Date endDate
    ) {
        return buildList(sensorData.findAllByDate(id, idParam, start, endDate));
    }

    @CrossOrigin
    @RequestMapping(value = IDataController.GET_DATA)
    public List<DisplayableData> getData() {
        return buildList(sensorData.findAll());
    }

    @CrossOrigin
    @RequestMapping(value = IDataController.SERVER_DATETIME)
    public long serverDatetime() {
        return Calendar.getInstance().getTimeInMillis() / 1000;
    }

    @CrossOrigin
    @RequestMapping(value = IDataController.CSV_DATA_BY_SENSOR_ID, produces = "text/csv")
    public String csvDataBySensorId(@RequestParam(value = IDataController.SENSOR_ID) Sensor s) {
        return formatter.format(buildList(sensorData.findAllBySensor(s)));
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
        // TODO data with this separator ?
        return SensorMetadata.DEFAULT_DATA_SEPARATOR;
    }

    interface Converter<MT, D> {
        D cast(MT mt);
    }
}
