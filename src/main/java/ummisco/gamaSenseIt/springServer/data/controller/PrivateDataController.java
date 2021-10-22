package ummisco.gamaSenseIt.springServer.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ummisco.gamaSenseIt.springServer.data.model.*;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata.DataParameter;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorDataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorMetadataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorRepository;
import ummisco.gamaSenseIt.springServer.data.services.sensor.ISensorManagment;

@CrossOrigin
@RestController
@RequestMapping("/private/")
public class PrivateDataController {
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

    public PrivateDataController() {
    }

    public static boolean isNotNil(String string) {
        return string.equals(NIL_VALUE);
    }

    public Sensor getSensorById(long id) {
        var sensor = sensors.findById(id);
        return sensor.isEmpty() ? null : sensor.get();
    }

    @CrossOrigin()
    @RequestMapping(IDataController.ADD_SENSOR)
    public DisplayableSensor addSensor(
            @RequestParam(value = IDataController.NAME, defaultValue = NIL_VALUE) String name,
            @RequestParam(value = IDataController.DISPLAY_NAME, defaultValue = NIL_VALUE) String displayName,
            @RequestParam(value = IDataController.SUB_DISPLAY_NAME, defaultValue = NIL_VALUE) String subDisplayName,
            @RequestParam(value = IDataController.LONGITUDE, defaultValue = "0") double longitude,
            @RequestParam(value = IDataController.LATITUDE, defaultValue = "0") double latitude,
            @RequestParam(value = IDataController.SENSOR_METADATA) long idSensorType
    ) {
        var typeSensor = sensorTypeRepo.findById(idSensorType);

        if (typeSensor.isEmpty())
            return null;

        // TODO raise an error this sensor already exist
        var selectedSensors = sensors.findByName(name);
        if (!selectedSensors.isEmpty())
            return new DisplayableSensor(selectedSensors.get(0));

        Sensor sensor = new Sensor(name, displayName, subDisplayName, longitude, latitude, typeSensor.get());
        sensors.save(sensor);
        return new DisplayableSensor(sensor);
    }

    @RequestMapping(IDataController.UPDATE_SENSOR)
    public DisplayableSensor updateSensor(
            @RequestParam(value = IDataController.SENSOR_ID) long id,
            @RequestParam(value = IDataController.NAME, required = false, defaultValue = NIL_VALUE) String name,
            @RequestParam(value = IDataController.DISPLAY_NAME, required = false, defaultValue = NIL_VALUE) String displayName,
            @RequestParam(value = IDataController.SUB_DISPLAY_NAME, required = false, defaultValue = NIL_VALUE) String subDisplayName,
            @RequestParam(value = IDataController.LONGITUDE, required = false, defaultValue = NIL_VALUE) String longitude,
            @RequestParam(value = IDataController.LATITUDE, required = false, defaultValue = NIL_VALUE) String latitude
    ) {

        Sensor sensor = getSensorById(id);
        if (sensor == null)
            return null;

        if (isNotNil(name))
            sensor.setName(name);

        if (isNotNil(displayName))
            sensor.setDisplayName(displayName);

        if (isNotNil(subDisplayName))
            sensor.setSubDisplayName(subDisplayName);

        if (isNotNil(longitude))
            sensor.setLongitude(Double.parseDouble(longitude));

        if (isNotNil(longitude))
            sensor.setLatitude(Double.parseDouble(latitude));

        sensors.save(sensor);
        return new DisplayableSensor(sensor);
    }

    @CrossOrigin
    @RequestMapping(IDataController.ADD_SENSOR_METADATA)
    public DisplayableSensorMetadata addSensorMetadata(
            @RequestParam(value = IDataController.NAME, defaultValue = NIL_VALUE) String varName,
            @RequestParam(value = IDataController.VERSION, defaultValue = NIL_VALUE) String version,
            @RequestParam(value = IDataController.DATA_SEPARATOR, defaultValue = SensorMetadata.DEFAULT_DATA_SEPARATOR) String sep,
            @RequestParam(value = IDataController.MEASURED_DATA_ORDER, defaultValue = NIL_VALUE) String measuredDataOrder,
            @RequestParam(value = IDataController.DESCRIPTION, defaultValue = NIL_VALUE) String description
    ) {
        var sensorsMetadate = sensorMetadata.findByNameAndVersion(varName, version);
        if (!sensorsMetadate.isEmpty()) return null;

        var st = new SensorMetadata(varName, version, sep, description);
        st.setMeasuredDataOrder(measuredDataOrder);
        st = this.sensorMetadata.save(st);
        return new DisplayableSensorMetadata(st);
    }

    @CrossOrigin
    @RequestMapping(IDataController.ADD_PARAMETER_METADATA)
    public DisplayableParameterMetadata addParameterMetadata(
            @RequestParam(value = IDataController.METADATA_ID, defaultValue = "nil") long id,
            @RequestParam(value = IDataController.NAME, defaultValue = "nil") String varName,
            @RequestParam(value = IDataController.UNIT, defaultValue = "nil") String varUnit,
            @RequestParam(value = IDataController.DATA_FORMAT, defaultValue = "nil") String varFormat,
            @RequestParam(value = IDataController.MEASURED_PARAMETER, defaultValue = "nil") String measuredParameter
    ) {
        var md = sensorMetadata.findById(id);
        if (md.isEmpty())
            return null;

        ParameterMetadata.DataFormat df;
        DataParameter dp;
        try {
            df = ParameterMetadata.DataFormat.valueOf(varFormat);
            dp = DataParameter.valueOf(measuredParameter);
        } catch (IllegalArgumentException e) {
            return null;
        }
        var params = new ParameterMetadata(varName, varUnit, df, dp);
        params = sensorManagmentService.addParameterToSensorMetadata(md.get(), params);
        return new DisplayableParameterMetadata(params);
    }
}
