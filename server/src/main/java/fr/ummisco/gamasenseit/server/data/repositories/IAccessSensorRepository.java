package fr.ummisco.gamasenseit.server.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import fr.ummisco.gamasenseit.server.data.model.user.AccessSensor;

@Repository
public interface IAccessSensorRepository extends CrudRepository<AccessSensor, AccessSensor.AccessSensorPK> {
}
