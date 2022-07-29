package fr.ummisco.gamasenseit.server.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import fr.ummisco.gamasenseit.server.data.model.sensor.ParameterMetadata;

import java.util.List;

@Repository
public interface IParameterMetadataRepository extends CrudRepository<ParameterMetadata, Long> {
    List<ParameterMetadata> findBySensorMetadataIdOrderByIdx(long id);

}
