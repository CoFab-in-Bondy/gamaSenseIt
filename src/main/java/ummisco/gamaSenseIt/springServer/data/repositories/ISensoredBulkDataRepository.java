package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ummisco.gamaSenseIt.springServer.data.model.sensor.SensoredBulkData;

import java.util.Date;
import java.util.List;

@Repository
public interface ISensoredBulkDataRepository extends CrudRepository<SensoredBulkData, Long> {
    List<SensoredBulkData> findByCaptureDate(Date date);
}
