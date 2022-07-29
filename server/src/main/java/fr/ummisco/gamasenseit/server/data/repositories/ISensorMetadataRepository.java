package fr.ummisco.gamasenseit.server.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import fr.ummisco.gamasenseit.server.data.model.sensor.SensorMetadata;

@Repository
public interface ISensorMetadataRepository extends CrudRepository<SensorMetadata, Long> {
}
