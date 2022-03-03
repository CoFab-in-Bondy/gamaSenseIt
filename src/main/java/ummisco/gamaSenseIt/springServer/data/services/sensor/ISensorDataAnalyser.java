package ummisco.gamaSenseIt.springServer.data.services.sensor;

import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Parameter;

import java.util.Date;
import java.util.List;

public interface ISensorDataAnalyser {
    List<Parameter> analyseBulkData(String data, Date captureDate, Sensor s);
}
