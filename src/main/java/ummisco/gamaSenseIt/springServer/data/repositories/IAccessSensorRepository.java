package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ummisco.gamaSenseIt.springServer.data.model.user.AccessSensor;


@Repository
public interface IAccessSensorRepository extends CrudRepository<AccessSensor, AccessSensor.AccessSensorPK>  {
}
