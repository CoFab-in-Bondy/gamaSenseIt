package ummisco.gamaSenseIt.springServer.qameleo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ummisco.gamaSenseIt.springServer.data.controller.DataController;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata.DataParameter;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorDataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorRepository;

import java.util.*;

@RestController
@RequestMapping("/qameleo/")
public class QameleoController {
    @Autowired
    DataController dataController;

    @Autowired
    ISensorRepository sensors;

    @Autowired
    ISensorDataRepository sensorData;

    @CrossOrigin
    @RequestMapping(value = IQameleoController.AIR_QUALITY)
    public QameleoData getLastData(@RequestParam(value = IQameleoController.SENSOR_ID, required = true) long sensorID) {
        var sensorFound = sensors.findById(sensorID);
        if (sensorFound.isEmpty())
            return null;
        var sensor = sensorFound.get();
        if (sensor.isHidden()) {
            return new QameleoData(sensor.getName(), sensor.getDisplayName(),
                    sensor.getSubDisplayName(), sensor.getHiddenMessage());
        }

        var parametersFound = sensor.getParameters();
        if (parametersFound.isEmpty())
            return null;
        var parameters = parametersFound.get();

        var qameleoData = new HashMap<DataParameter, Double>();
        Calendar start = Calendar.getInstance();
        Calendar startHour = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 1);
        start.add(Calendar.DAY_OF_MONTH, -1);
        startHour.add(Calendar.HOUR_OF_DAY, -1);

        for (var param : parameters) {
            var relativeStart = switch (param.getParameter()) {
                case TEMPERATURE, HUMIDITY -> startHour;
                default -> start;
            };
            Double mean = getMeanValue(sensorID, param.getId(), relativeStart.getTime(), endDate.getTime());
            qameleoData.put(param.getParameter(), mean == null ? 0.0 : mean);
        }
        return new QameleoData(sensor.getName(), sensor.getDisplayName(), sensor.getSubDisplayName(), qameleoData);
    }

    private Double getMeanValue(long id, long idParam, Date start, Date endDate) {
        var sensorData = this.sensorData.findAllByDate(id, idParam, start, endDate);
        if (sensorData.isEmpty())
            return null;
        double sum = 0.0;
        // FIXME unsafe cast
        for (var dataFromSensor : sensorData)
            sum += ((Double) dataFromSensor.getDataObject());
        return sum / sensorData.size();

    }

}
