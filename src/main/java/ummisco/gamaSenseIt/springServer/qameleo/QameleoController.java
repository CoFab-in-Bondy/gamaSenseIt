package ummisco.gamaSenseIt.springServer.qameleo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ummisco.gamaSenseIt.springServer.data.controller.PublicDataController;
import ummisco.gamaSenseIt.springServer.data.repositories.IParameterRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorRepository;

import java.util.*;

@RestController
@RequestMapping("/qameleo/")
public class QameleoController {
    @Autowired
    PublicDataController dataController;

    @Autowired
    ISensorRepository sensorsRepo;

    @Autowired
    IParameterRepository parametersRepo;

    @CrossOrigin
    @RequestMapping(value = IQameleoController.AIR_QUALITY)
    public QameleoData getLastData(@RequestParam(value = IQameleoController.SENSOR_ID) long sensorID) {
        var sensorFound = sensorsRepo.findById(sensorID);
        if (sensorFound.isEmpty())
            return null;
        var sensor = sensorFound.get();

        if (sensor.isHidden()) {
            return new QameleoData(sensor.getName(), sensor.getDisplayName(),
                    sensor.getSubDisplayName(), sensor.getHiddenMessage());
        }

        var pmds = sensor.getSensorMetadata().getParametersMetadata();

        var qameleoData = new HashMap<String, Double>();
        Calendar start = Calendar.getInstance();
        Calendar startHour = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 1);
        start.add(Calendar.DAY_OF_MONTH, -1);
        startHour.add(Calendar.HOUR_OF_DAY, -1);

        for (var pmd : pmds) {
            var relativeStart = switch (pmd.getName()) {
                case "temperature", "humidity" -> startHour;
                default -> start;
            };
            Double mean = getMeanValue(sensorID, pmd.getId(), relativeStart.getTime(), endDate.getTime());
            qameleoData.put(pmd.getName(), mean == null ? 0.0 : mean);

        }
        return new QameleoData(sensor.getName(), sensor.getDisplayName(), sensor.getSubDisplayName(), qameleoData);
    }

    private Double getMeanValue(long id, long idParam, Date start, Date endDate) {
        var parameters = this.parametersRepo.advancedFindAll(id, idParam, start, endDate);
        if (parameters.isEmpty())
            return null;
        double sum = 0.0;
        // FIXME unsafe cast, make generic for data
        for (var p : parameters)
            sum += ((Double) p.value());
        return sum / parameters.size();
    }
}
