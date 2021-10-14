package ummisco.gamaSenseIt.springServer.data.services;

// import java.text.NumberFormat;
// import java.util.Calendar;
import java.util.Date;
import java.util.List;
// import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import ummisco.gamaSenseIt.springServer.data.model.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.SensorData;
// import ummisco.gamaSenseIt.springServer.data.model.DisplayableData;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata.DataFormat;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata.DataParameter;
import ummisco.gamaSenseIt.springServer.data.model.SensorMetadata;
import ummisco.gamaSenseIt.springServer.data.model.SensoredBulkData;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorDataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.IParameterMetadataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorMetadataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensoredBulkDataRepository;

@Service("SensorManagment")
public class SensorManagment implements ISensorManagment {

  @Autowired
  IParameterMetadataRepository parameterSensorRepo;
  @Autowired
  ISensorRepository sensorRepo;
  @Autowired
  ISensoredBulkDataRepository bulkDataRepo;
  @Autowired
  ISensorMetadataRepository sensorMetadataRepo;
  @Autowired
  ISensorDataRepository analysedDataRepo;
  @Autowired
  ISensorDataAnalyser dataAnalyser;

  @Override
  public void saveDefaultSensorInit() {
/*
    GeometryFactory gf = new GeometryFactory();

    SensorMetadata mtype = new SensorMetadata(DEFAULT_SENSOR_TYPE_NAME, DEFAULT_SENSOR_VERSION);
    SensorMetadata qamelio = new SensorMetadata("Qamelio", "1", ":",DEFAULT_DESCRIPTION);
    sensorMetadataRepo.save(mtype);

    qamelio = addSensorMetadata(qamelio);
    ParameterMetadata pp1 = new ParameterMetadata("pm 1", "mg/m3", DataFormat.DOUBLE, DataParameter.PM1);
    ParameterMetadata pp2 = new ParameterMetadata("pm 2.5", "mg/m3", DataFormat.DOUBLE, DataParameter.PM2_5);
    ParameterMetadata pp10 = new ParameterMetadata("pm 10", "mg/m3", DataFormat.DOUBLE, DataParameter.PM10);

    ParameterMetadata t1 = new ParameterMetadata("temperature", "c", DataFormat.DOUBLE, DataParameter.TEMPERATURE);
    ParameterMetadata h1 = new ParameterMetadata("humidity", "%", DataFormat.DOUBLE, DataParameter.HUMIDITY);
    addParameterToSensorMetadata(qamelio, pp1);
    addParameterToSensorMetadata(qamelio, pp2);
    addParameterToSensorMetadata(qamelio, pp10);
    addParameterToSensorMetadata(qamelio, h1);
    addParameterToSensorMetadata(qamelio, t1);

    Point p = gf.createPoint(new Coordinate(45, 3));
    Sensor s1 = new Sensor(DEFAULT_SENSOR_NAME, DEFAULT_SENSOR_DISPLAY_NAME, DEFAULT_SENSOR_PLACE, p, mtype);
    String slogan = "La recherche scientifique au service de la qualité de l'air que vous respirez.";
    String subDisplayName = "IRD";
    Sensor s2 = new Sensor("SENSOR_2", slogan, subDisplayName, p, qamelio);
    sensorRepo.save(s1);
    sensorRepo.save(s2);

    String description = "comment utiliser le capteur";
    SensorMetadata smd = new SensorMetadata("capMetadata", "v0", ":",description);
    smd = addSensorMetadata(smd);
    ParameterMetadata p1 = new ParameterMetadata("temperature", "c", DataFormat.DOUBLE, DataParameter.TEMPERATURE);
    ParameterMetadata p2 = new ParameterMetadata("humidity", "%", DataFormat.DOUBLE, DataParameter.HUMIDITY);
    addParameterToSensorMetadata(smd, p2);
    addParameterToSensorMetadata(smd, p1);

    Sensor sx = new Sensor("node_1", DEFAULT_SENSOR_DISPLAY_NAME, DEFAULT_SENSOR_PLACE, p, smd);
    sensorRepo.save(sx);
*/
  }

  @Override
  public void saveData(String message, Date date) {

    String[] data = message.split(";");
    if (data.length < 4)
      return;
    long capturedateS, token;
    String sensorName = data[1];

    try {
      capturedateS = Long.parseLong(data[0]);
      token = Long.parseLong(data[2]);
    } catch (NumberFormatException e) {
      return;
    }

    String contents = data[3];
    List<Sensor> foundSensors = sensorRepo.findByName(sensorName);
    Sensor selectedSensor = null;
    if (foundSensors.isEmpty())
      return;

    /*
     * if(foundSensors.isEmpty()) { SensorMetadata typeSens =
     * sensorMetadataRepo.findByNameAndVersion(DEFAULT_SENSOR_TYPE_NAME,
     * DEFAULT_SENSOR_VERSION).get(0); GeometryFactory gf=new GeometryFactory();
     * Point p = gf.createPoint(new Coordinate(0, 0)); selectedSensor = new
     * Sensor(sensorName,p,typeSens); sensorRepo.save(selectedSensor); } else {
     */
    selectedSensor = foundSensors.get(0);
    // }

    Date capturedate = new Date(capturedateS * 1000);

    /*
     * System.out.println(
     * "*************************************************************************************"
     * );
     * 
     * System.out.println("capture date "+capturedateS);
     * 
     * System.out.println("sensorName "+sensorName);
     * System.out.println("sensorName "+sensorName);
     * System.out.println("token "+token); System.out.println("contents "+contents);
     */

    SensoredBulkData bulkData = new SensoredBulkData(selectedSensor, token, capturedate, date, contents);
    bulkDataRepo.save(bulkData);
    List<SensorData> aData = dataAnalyser.analyseBulkData(contents, capturedate, selectedSensor);
    // System.out.println("*************************************************************************************");

    analysedDataRepo.saveAll(aData);
  }

  @Override
  public Sensor updateSensorInformation(Sensor s) {
    return sensorRepo.save(s);
  }

  @Override
  public SensorMetadata addSensorMetadata(SensorMetadata s) {

    return sensorMetadataRepo.save(s);
  }

  @Override
  public ParameterMetadata addParameterToSensorMetadata(SensorMetadata s, ParameterMetadata md) {
    md.setSensorMetadata(s);
    ParameterMetadata res = parameterSensorRepo.save(md);
    s.addmeasuredData(res);
    sensorMetadataRepo.save(s);
    return res;
  }

}
