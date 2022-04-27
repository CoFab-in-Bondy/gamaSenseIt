package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.repository.CrudRepository;
import ummisco.gamaSenseIt.springServer.data.model.preference.InteractSensor;

public interface IInteractSensorRepository extends CrudRepository<InteractSensor, InteractSensor.InteractSensorPK> {
}
