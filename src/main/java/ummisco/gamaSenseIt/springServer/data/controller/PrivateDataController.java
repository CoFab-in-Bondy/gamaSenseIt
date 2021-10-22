package ummisco.gamaSenseIt.springServer.data.controller;

import org.springframework.web.bind.annotation.*;
import ummisco.gamaSenseIt.springServer.data.model.*;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata.DataParameter;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorDataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorMetadataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorRepository;
import ummisco.gamaSenseIt.springServer.data.services.sensor.ISensorManagment;

@CrossOrigin
@RestController
@RequestMapping(Route.PRIVATE)
public class PrivateDataController extends DataController {

    /*-----------------------*
     | Data                  |
     *-----------------------*/

    /*-----------------------*
     | Sensors               |
     *-----------------------*/

    public Sensor getSensorById(long id) {
        var sensor = sensors.findById(id);
        return sensor.isEmpty() ? null : sensor.get();
    }

    @CrossOrigin
    @RequestMapping(value = Route.SENSORS, method = RequestMethod.POST)
    public DisplayableSensor addSensor(
            @RequestParam(value = Param.NAME, defaultValue = NIL) String name,
            @RequestParam(value = Param.DISPLAY_NAME, defaultValue = NIL) String displayName,
            @RequestParam(value = Param.SUB_DISPLAY_NAME, defaultValue = NIL) String subDisplayName,
            @RequestParam(value = Param.LONGITUDE, defaultValue = "0") double longitude,
            @RequestParam(value = Param.LATITUDE, defaultValue = "0") double latitude,
            @RequestParam(value = Param.SENSOR_METADATA) long idSensorType
    ) {
        var typeSensor = sensorsMetadata.findById(idSensorType);

        if (typeSensor.isEmpty())
            return null;

        // TODO raise an error this sensor already exist
        var selectedSensors = sensors.findByName(name);
        if (!selectedSensors.isEmpty())
            return selectedSensors.get(0).convert();

        Sensor sensor = new Sensor(name, displayName, subDisplayName, longitude, latitude, typeSensor.get());
        sensors.save(sensor);
        return sensor.convert();
    }

    @CrossOrigin
    @RequestMapping(value = Route.SENSORS, method = RequestMethod.PATCH)
    public DisplayableSensor updateSensor(
            @RequestParam(value = Param.SENSOR_ID) long id,
            @RequestParam(value = Param.NAME, required = false) String name,
            @RequestParam(value = Param.DISPLAY_NAME, required = false) String displayName,
            @RequestParam(value = Param.SUB_DISPLAY_NAME, required = false) String subDisplayName,
            @RequestParam(value = Param.LONGITUDE, required = false) Double longitude,
            @RequestParam(value = Param.LATITUDE, required = false) Double latitude
    ) {

        Sensor sensor = getSensorById(id);
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

        sensors.save(sensor);
        return sensor.convert();
    }

    /*-----------------------*
     | Types                 |
     *-----------------------*/

    @CrossOrigin
    @RequestMapping(value = Route.TYPES, method = RequestMethod.POST)
    public DisplayableSensorMetadata addType(
            @RequestParam(value = Param.NAME, defaultValue = NIL) String varName,
            @RequestParam(value = Param.VERSION, defaultValue = NIL) String version,
            @RequestParam(value = Param.DATA_SEPARATOR, defaultValue = SensorMetadata.DEFAULT_DATA_SEPARATOR) String sep,
            @RequestParam(value = Param.MEASURED_DATA_ORDER, defaultValue = NIL) String measuredDataOrder,
            @RequestParam(value = Param.DESCRIPTION, defaultValue = NIL) String description
    ) {
        var sensorsMetadate = sensorsMetadata.findByNameAndVersion(varName, version);
        if (!sensorsMetadate.isEmpty()) return null;

        var st = new SensorMetadata(varName, version, sep, description);
        st.setMeasuredDataOrder(measuredDataOrder);
        st = this.sensorsMetadata.save(st);
        return new DisplayableSensorMetadata(st);
    }

    /*-----------------------*
     | Parameters            |
     *-----------------------*/

    @CrossOrigin
    @RequestMapping(value = Route.PARAMETERS, method = RequestMethod.POST)
    public DisplayableParameterMetadata addParameter(
            @RequestParam(value = Param.METADATA_ID, defaultValue = NIL) long id,
            @RequestParam(value = Param.NAME, defaultValue = NIL) String varName,
            @RequestParam(value = Param.UNIT, defaultValue = NIL) String varUnit,
            @RequestParam(value = Param.DATA_FORMAT, defaultValue = NIL) String varFormat,
            @RequestParam(value = Param.MEASURED_PARAMETER, defaultValue = NIL) String measuredParameter
    ) {
        var md = sensorsMetadata.findById(id);
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
        params = sensorsManagement.addParameterToSensorMetadata(md.get(), params);
        return new DisplayableParameterMetadata(params);
    }

    /*-----------------------*
     | Server                |
     *-----------------------*/
}
