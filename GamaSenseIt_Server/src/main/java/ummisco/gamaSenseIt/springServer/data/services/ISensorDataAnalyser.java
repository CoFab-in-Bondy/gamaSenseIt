package ummisco.gamaSenseIt.springServer.data.services;

import java.util.Date;
import java.util.List;

import ummisco.gamaSenseIt.springServer.data.model.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.SensorData;

public interface ISensorDataAnalyser {
  List<SensorData> analyseBulkData(String data, Date captureDate, Sensor s);
}
