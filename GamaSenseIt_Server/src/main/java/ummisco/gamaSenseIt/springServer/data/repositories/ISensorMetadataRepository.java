package ummisco.gamaSenseIt.springServer.data.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ummisco.gamaSenseIt.springServer.data.model.SensorMetadata;

@Repository
public interface ISensorMetadataRepository extends CrudRepository<SensorMetadata, Long> {
  List<SensorMetadata> findByNameAndVersion(String name, String version);
}
