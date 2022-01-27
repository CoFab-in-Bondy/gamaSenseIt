package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;

import java.util.List;

@Repository
public interface IParameterMetadataRepository extends CrudRepository<ParameterMetadata, Long> {
    List<ParameterMetadata> findBySensorMetadataIdOrderByIdx(long id);

}
