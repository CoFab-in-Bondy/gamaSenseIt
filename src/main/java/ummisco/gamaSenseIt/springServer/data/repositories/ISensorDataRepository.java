package ummisco.gamaSenseIt.springServer.data.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import ummisco.gamaSenseIt.springServer.data.model.SensorData;

@CrossOrigin
//@RepositoryRestResource(collectionResourceRel = "sensorData", path = "sensorData")
@Repository
public interface ISensorDataRepository extends CrudRepository<SensorData, Long> {
  List<SensorData> findAllByCaptureDateLessThanEqualAndCaptureDateGreaterThanEqual(Date endDate, Date startDate);

  @Query("select a from SensorData a where a.sensor.id = :idSensor and a.parameter.id = :idParameter and a.captureDate >= :start and  a.captureDate <= :end ")
  List<SensorData> findAllByDate(@Param(value = "idSensor") long idSensor,
      @Param(value = "idParameter") long parameter, @Param(value = "start") Date start, @Param(value = "end") Date end);

}
