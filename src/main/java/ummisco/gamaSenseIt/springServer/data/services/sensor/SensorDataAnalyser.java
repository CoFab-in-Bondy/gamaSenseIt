package ummisco.gamaSenseIt.springServer.data.services.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Parameter;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.sensor.SensorMetadata;
import ummisco.gamaSenseIt.springServer.data.repositories.IParameterMetadataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorMetadataRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SensorDataAnalyser implements ISensorDataAnalyser {

    @Autowired
    private IParameterMetadataRepository pmdRepo;

    @Autowired
    private ISensorMetadataRepository smdRepo;

    private static final Logger logger = LoggerFactory.getLogger(ISensorDataAnalyser.class);

    @Override
    public List<Parameter> analyseBulkData(String bulkData, Date captureDate, Sensor sensor) {
        logger.info("Analysing : sensor " + sensor.getName() + " data " + bulkData);

        // need to use a repository because hibernate proxy is disabled here
        SensorMetadata smd = smdRepo.findById(sensor.getSensorMetadataId()).get();
        String sep = smd.getDataSeparator();
        logger.info("separator " + sep);
        String[] morsels = bulkData.split(sep);

        // need to use a repository because hibernate proxy is disabled here
        var pmds = pmdRepo.findBySensorMetadataIdOrderByIdx(smd.getId());

        var res = new ArrayList<Parameter>();
        int i = 0;
        for (var pmd : pmds) {
            logger.info("SID/" + pmd.getId() + "/");
            var p = pmd.createParameter(morsels[i], captureDate, sensor);
            if (p != null)
                res.add(p);
            else
                logger.warn("Invalid message " + sensor + " data " + bulkData);
            i++;
        }
        return res;
    }

}
