package ummisco.gamaSenseIt.springServer.data.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ummisco.gamaSenseIt.springServer.data.model.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.SensorData;
import ummisco.gamaSenseIt.springServer.data.model.SensorMetadata;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata.DataFormat;
import ummisco.gamaSenseIt.springServer.data.repositories.IParameterMetadataRepository;

@Service
public class SensorDataAnalyser implements ISensorDataAnalyser {

  @Autowired
  IParameterMetadataRepository metadataRepo;

  @Override
  public List<SensorData> analyseBulkData(String data, Date captureDate, Sensor s) {
    System.out.println("sensor " + s.getName() + " data " + data);
    ArrayList<SensorData> res = new ArrayList<SensorData>();
    SensorMetadata smd = s.getMetadata();
    String sep = smd.getDataSeparator();
    System.out.println("separator " + sep);
    int i = 0;
    String[] datas = data.split(sep);

    System.out.println("order_" + smd.getMeasuredDataOrder() + "_");
    String[] metadata = smd.getMeasuredDataOrder().split(SensorMetadata.MEASURE_ORDER_SEPARATOR);

    for (String xx : metadata) {
      System.out.println("meta_" + xx + "_");
    }

    for (String sid : metadata) {
      System.out.println("SID/" + sid + "/");
      long metaKey = Long.parseLong(sid);
      Optional<ParameterMetadata> md = s.getParameterMetadata(metaKey);
      if (md.isPresent()) {
        SensorData dt = null;
        ParameterMetadata pmd = md.get();
        if (pmd.getDataFormat().equals(DataFormat.DOUBLE)) {
          double localData = Double.valueOf(datas[i]).doubleValue();
          dt = new SensorData(localData, captureDate, md.get(), s);
        } else if (pmd.getDataFormat().equals(DataFormat.DOUBLE)) {
          long localData = Long.valueOf(datas[i]).longValue();
          dt = new SensorData(localData, captureDate, md.get(), s);
        } else if (pmd.getDataFormat().equals(DataFormat.STRING)) {
          String localData = datas[i];
          dt = new SensorData(localData, captureDate, md.get(), s);
        }
        res.add(dt);
      }
      i++;
    }
    return res;
  }

}
