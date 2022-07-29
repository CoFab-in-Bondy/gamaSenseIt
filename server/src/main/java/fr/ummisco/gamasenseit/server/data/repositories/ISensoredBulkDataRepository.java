package fr.ummisco.gamasenseit.server.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import fr.ummisco.gamasenseit.server.data.model.sensor.SensoredBulkData;

import java.util.Date;
import java.util.List;

@Repository
public interface ISensoredBulkDataRepository extends CrudRepository<SensoredBulkData, Long> {
    List<SensoredBulkData> findByCaptureDate(Date date);
}
