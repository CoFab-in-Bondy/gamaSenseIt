package ummisco.gamaSenseIt.springServer.data.controller;

import java.util.List;
import java.util.Optional;
//import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import ummisco.gamaSenseIt.springServer.data.model.DisplayableData;
import ummisco.gamaSenseIt.springServer.data.model.DisplayableParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.DisplayableSensor;
import ummisco.gamaSenseIt.springServer.data.model.DisplayableSensorMetadata;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.SensorMetadata;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata.DataFormat;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata.DataParameter;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;
//import ummisco.gamaSenseIt.springServer.data.model.SensorData;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorDataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorMetadataRepository;
import ummisco.gamaSenseIt.springServer.data.services.ISensorManagment;

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

//  private Sensor findSensor(String id, String name) {
//    Sensor s = null;
//    if (!id.equals("nil")) {
//      Optional<Sensor> o = sensors.findById(Long.valueOf(id).longValue());
//      if (o.isPresent())
//        s = o.get();
//    } else {
//      List<Sensor> ls = sensors.findByName(name);
//      if (ls.size() > 0)
//        s = ls.get(0);
//    }
//    return s;
//  }

  private Sensor findSensor(long id) {
    Long lid = null;
    Sensor s = null;
    try {
      lid = Long.valueOf(id);
      s = sensors.findById(lid).get();
    } catch (NumberFormatException e) {
      return null;
    }
    return s;
  }

  @CrossOrigin()
  @RequestMapping(IDataController.ADD_SENSOR)
  public DisplayableSensor addSensor(
      @RequestParam(value = IDataController.NAME, required = true, defaultValue = NIL_VALUE) String name,
      @RequestParam(value = IDataController.DISPLAY_NAME, required = true, defaultValue = NIL_VALUE) String displayName,
      @RequestParam(value = IDataController.SUB_DISPLAY_NAME, required = true, defaultValue = NIL_VALUE) String subDisplayName,
      @RequestParam(value = IDataController.LONGITUDE, required = true, defaultValue = "0") double longitude,
      @RequestParam(value = IDataController.LATITUDE, required = true, defaultValue = "0") double latitude,
      @RequestParam(value = IDataController.SENSOR_METADATA, required = true) long idSensorType) {

    Optional<SensorMetadata> type = sensorTypeRepo.findById(idSensorType);

    if (!type.isPresent())
      return null;

    List<Sensor> selectedSensor = sensors.findByName(name);
    if (!selectedSensor.isEmpty())
      return new DisplayableSensor(selectedSensor.get(0));

    Optional<SensorMetadata> st = sensorTypeRepo.findById(idSensorType);
    // if(sensorManagmentService.)
    if (!st.isPresent())
      return null;

    Sensor s = new Sensor(name, displayName, subDisplayName, longitude, latitude, st.get());
    sensors.save(s);
    return new DisplayableSensor(s);
  }

  @RequestMapping(IDataController.UPDATE_SENSOR)
  public DisplayableSensor updateSensor(@RequestParam(value = IDataController.SENSOR_ID, required = true) long id,
      @RequestParam(value = IDataController.NAME, required = false, defaultValue = NIL_VALUE) String name,
      @RequestParam(value = IDataController.DISPLAY_NAME, required = false, defaultValue = NIL_VALUE) String displayName,
      @RequestParam(value = IDataController.SUB_DISPLAY_NAME, required = false, defaultValue = NIL_VALUE) String subDisplayName,
      @RequestParam(value = IDataController.LONGITUDE, required = false, defaultValue = NIL_VALUE) String longitude,
      @RequestParam(value = IDataController.LATITUDE, required = false, defaultValue = NIL_VALUE) String latitude) {

    Sensor s = findSensor(id);
    if (s == null)
      return null;

    if (!name.equals(NIL_VALUE)) {
      s.setName(name);
    }
    if (!displayName.equals(NIL_VALUE)) {
      s.setDisplayName(displayName);
    }
    if (!subDisplayName.equals(NIL_VALUE)) {
      s.setSubDisplayName(subDisplayName);
    }
    if (!longitude.equals(NIL_VALUE)) {
      double data = Double.valueOf(longitude).doubleValue();
      s.setLongitude(data);
    }
    if (!latitude.equals(NIL_VALUE)) {
      double data = Double.valueOf(latitude).doubleValue();
      s.setLatitude(data);
    }
    sensors.save(s);
    return new DisplayableSensor(s);
  }

  @CrossOrigin
  @RequestMapping(IDataController.ADD_SENSOR_METADATA)
  public DisplayableSensorMetadata addSensorMetadata(
      @RequestParam(value = IDataController.NAME, required = true, defaultValue = NIL_VALUE) String varName,
      @RequestParam(value = IDataController.VERSION, required = true, defaultValue = NIL_VALUE) String version,
      @RequestParam(value = IDataController.DATA_SEPARATOR, required = true, defaultValue = SensorMetadata.DEFAULT_DATA_SEPARATOR) String sep,
      @RequestParam(value = IDataController.MEASURED_DATA_ORDER, required = true, defaultValue = NIL_VALUE) String measuredDataOrder,
      @RequestParam(value = IDataController.DESCRIPTION, required = true, defaultValue = NIL_VALUE) String description) {
    List<SensorMetadata> lsm = sensorMetadata.findByNameAndVersion(varName, version);
    SensorMetadata st = null;
    if (lsm.size() == 0) {
      st = new SensorMetadata(varName, version, sep, description);
      st.setMeasuredDataOrder(measuredDataOrder);
      st = this.sensorMetadata.save(st);
      return new DisplayableSensorMetadata(st);
    }
    return null;
  }

  @CrossOrigin
  @RequestMapping(IDataController.ADD_PARAMETER_META_DATA)
  public DisplayableParameterMetadata addParameterMetadata(
      @RequestParam(value = IDataController.METADATA_ID, required = true, defaultValue = "nil") long id,
      @RequestParam(value = IDataController.NAME, required = true, defaultValue = "nil") String varName,
      @RequestParam(value = IDataController.UNIT, required = true, defaultValue = "nil") String varUnit,
      @RequestParam(value = IDataController.DATA_FORMAT, required = true, defaultValue = "nil") String varFormat,
      @RequestParam(value = IDataController.MEASURED_PARAMETER, required = true, defaultValue = "nil") String mesuredParameter) {
    Optional<SensorMetadata> md = sensorMetadata.findById(id);
    if (!md.isPresent())
      return null;
    DataFormat df = null;
    DataParameter dp = null;
    try {
      df = DataFormat.valueOf(varFormat);
      dp = DataParameter.valueOf(mesuredParameter);
    } catch (IllegalArgumentException e) {
      return null;
    }
    ParameterMetadata smd = new ParameterMetadata(varName, varUnit, df, dp);
    smd = sensorManagmentService.addParameterToSensorMetadata(md.get(), smd);
    return new DisplayableParameterMetadata(smd);
  }
}
