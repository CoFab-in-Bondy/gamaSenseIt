package fr.ummisco.gamasenseit.server.data.services.sensor;

import fr.ummisco.gamasenseit.server.data.model.sensor.Sensor;
import fr.ummisco.gamasenseit.server.data.model.sensor.Parameter;

import java.util.Date;
import java.util.List;

public interface ISensorDataAnalyser {
    List<Parameter> analyseBulkData(String data, Date captureDate, Sensor s);
}
