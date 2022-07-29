package fr.ummisco.gamasenseit.server.data.repositories;

import fr.ummisco.gamasenseit.server.data.model.preference.InteractSensor;
import org.springframework.data.repository.CrudRepository;

public interface IInteractSensorRepository extends CrudRepository<InteractSensor, InteractSensor.InteractSensorPK> {
}
