package ummisco.gamaSenseIt.springServer.data.services.sensor;

import ummisco.gamaSenseIt.springServer.data.model.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.SensorData;

import java.util.Date;
import java.util.List;

public interface ISensorDataAnalyser {
    List<SensorData> analyseBulkData(String data, Date captureDate, Sensor s);
}
