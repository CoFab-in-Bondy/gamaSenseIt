package ummisco.gamaSenseIt.springServer.data.services.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.*;
import ummisco.gamaSenseIt.springServer.data.repositories.IParameterMetadataRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SensorDataAnalyser implements ISensorDataAnalyser {

    @Autowired
    IParameterMetadataRepository metadataRepo;

    // TODO make it better
    @Override
    public List<SensorData> analyseBulkData(String bulkData, Date captureDate, Sensor sensor) {
        System.out.println("sensor " + sensor.getName() + " data " + bulkData);
        var res = new ArrayList<SensorData>();
        SensorMetadata metadata = sensor.getMetadata();
        String sep = metadata.getDataSeparator();
        System.out.println("separator " + sep);
        int i = 0;
        String[] datas = bulkData.split(sep);

        System.out.println("order_" + metadata.getMeasuredDataOrder() + "_");
        String[] measure = metadata.getMeasuredDataOrder().split(SensorMetadata.MEASURE_ORDER_SEPARATOR);

        for (String xx : measure) {
            System.out.println("meta_" + xx + "_");
        }

        for (String sid : measure) {
            System.out.println("SID/" + sid + "/");
            long metaKey = Long.parseLong(sid);
            var optParams = sensor.getParameterMetadata(metaKey);
            if (optParams.isPresent()) {
                SensorData data = null;
                ParameterMetadata params = optParams.get();
                if (params.getDataFormat().equals(ParameterMetadata.DataFormat.DOUBLE)) {
                    double localData = Double.parseDouble(datas[i]);
                    data = new SensorData(localData, captureDate, optParams.get(), sensor);
                } else if (params.getDataFormat().equals(ParameterMetadata.DataFormat.INTEGER)) {
                    long localData = Long.parseLong(datas[i]);
                    data = new SensorData(localData, captureDate, optParams.get(), sensor);
                } else if (params.getDataFormat().equals(ParameterMetadata.DataFormat.STRING)) {
                    String localData = datas[i];
                    data = new SensorData(localData, captureDate, optParams.get(), sensor);
                }
                res.add(data);
            }
            i++;
        }
        return res;
    }

}
