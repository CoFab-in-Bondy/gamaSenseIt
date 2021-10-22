package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import ummisco.gamaSenseIt.springServer.data.model.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.SensorData;

import java.util.Date;
import java.util.List;

@CrossOrigin
//@RepositoryRestResource(collectionResourceRel = "sensorData", path = "sensorData")
@Repository
public interface ISensorDataRepository extends CrudRepository<SensorData, Long> {
    List<SensorData> findAllByCaptureDateLessThanEqualAndCaptureDateGreaterThanEqual(Date endDate, Date startDate);

    @Query("""
    SELECT s FROM SensorData s
        WHERE s.sensor.id = :idSensor""")
    List<SensorData> findAllBySensorId(long idSensor);
    List<SensorData> findAllBySensor(Sensor s);

    @Query("""
    SELECT s FROM SensorData s
        WHERE (:idParameter IS NULL OR s.sensor.id = :idSensor)
            AND (:idParameter IS NULL OR s.parameter.id = :idParameter)
            AND (:start IS NULL OR s.captureDate >= :start)
            AND (:end IS NULL OR s.captureDate <= :end)""")
    List<SensorData> advancedFindAll(
            @Param("idSensor") Long idSensor,
            @Param("idParameter") Long parameter,
            @Param("start") Date start,
            @Param("end") Date end
    );

}
