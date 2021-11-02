package ummisco.gamaSenseIt.springServer.data.services.sensor;

import ummisco.gamaSenseIt.springServer.data.model.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.Parameter;

import java.util.Date;
import java.util.List;

public interface ISensorDataAnalyser {
    List<Parameter> analyseBulkData(String data, Date captureDate, Sensor s);
}
