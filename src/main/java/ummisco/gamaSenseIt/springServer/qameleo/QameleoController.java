package ummisco.gamaSenseIt.springServer.qameleo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ummisco.gamaSenseIt.springServer.data.controller.DataController;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata.DataParameter;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.SensorData;
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
        Optional<Sensor> sns = sensors.findById(sensorID);
        if (sns.isEmpty())
            return null;
        Sensor s = sns.get();
        if (s.isHidden()) {
            return new QameleoData(s.getName(), s.getDisplayName(), s.getSubDisplayName(), s.getHiddenMessage());
        }

        Optional<Set<ParameterMetadata>> ps = s.getParameters();
        if (ps.isEmpty())
            return null;
        Set<ParameterMetadata> parameters = ps.get();

        HashMap<DataParameter, Double> res = new HashMap<DataParameter, Double>();
        Calendar start = Calendar.getInstance();
        Calendar startHour = Calendar.getInstance();
        Calendar enddate = Calendar.getInstance();
        enddate.add(Calendar.DAY_OF_MONTH, 1);
        start.add(Calendar.DAY_OF_MONTH, -1);
        startHour.add(Calendar.HOUR_OF_DAY, -1);

        for (ParameterMetadata p : parameters) {
            long idParam = p.getId();
            Double mean;
            if (p.getParameter().equals(DataParameter.TEMPERATURE) || p.getParameter().equals(DataParameter.HUMIDITY)) {
                mean = getMeanValue(sensorID, idParam, startHour.getTime(), enddate.getTime());
            } else {
                mean = getMeanValue(sensorID, idParam, start.getTime(), enddate.getTime());
            }
            res.put(p.getParameter(), mean == null ? 0.0 : mean);
        }
        return new QameleoData(s.getName(), s.getDisplayName(), s.getSubDisplayName(), res);
    }

    private Double getMeanValue(long id, long idParam, Date start, Date endDate) {
        List<SensorData> dts = this.sensorData.findAllByDate(id, idParam, start, endDate);
        if (dts.isEmpty())
            return null;
        double res = 0.0;
        for (SensorData d : dts)
            res += ((Double) d.getDataObject());
        res = res / dts.size();
        return res;

    }

}
