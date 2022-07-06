package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ummisco.gamaSenseIt.springServer.data.model.sensor.SensorMetadata;

import java.util.List;

@Repository
public interface ISensorMetadataRepository extends CrudRepository<SensorMetadata, Long> {
}
