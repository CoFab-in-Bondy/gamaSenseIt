package ummisco.gamaSenseIt.springServer.data.services.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SensorDataAnalyser implements ISensorDataAnalyser {

    private static final Logger logger = LoggerFactory.getLogger(ISensorDataAnalyser.class);

    @Override
    public List<Parameter> analyseBulkData(String bulkData, Date captureDate, Sensor sensor) {
        logger.info("Analysing : sensor " + sensor.getName() + " data " + bulkData);

        SensorMetadata smd = sensor.getSensorMetadata();
        String sep = smd.getDataSeparator();
        logger.debug("separator " + sep);
        String[] morsels = bulkData.split(sep);

        var pmds = smd.getParametersMetadata();

        if (logger.isDebugEnabled())
            for (var pmd : pmds)
                logger.debug("meta_" + pmd.getId() + "_");

        var res = new ArrayList<Parameter>();
        int i = 0;
        for (var pmd : pmds) {
            logger.debug("SID/" + pmd.getId() + "/");
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
