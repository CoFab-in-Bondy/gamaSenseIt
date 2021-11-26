package ummisco.gamaSenseIt.springServer.data.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import ummisco.gamaSenseIt.springServer.data.model.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.Parameter;

import java.util.Date;
import java.util.List;

@CrossOrigin
//@RepositoryRestResource(collectionResourceRel = "sensorData", path = "sensorData")
@Repository
public interface IParameterRepository extends CrudRepository<Parameter, Long> {

    @Query("""
    SELECT p FROM Parameter p
        WHERE p.sensorId = :sensorId""")
    List<Parameter> findAllBySensorId(long sensorId);

    @Query("""
    SELECT p FROM Parameter p
        WHERE p.sensorId = :sensorId
            AND (:parameterMetadataId IS NULL OR p.parameterMetadataId = :parameterMetadataId)
            AND (:start IS NULL OR p.captureDate >= :start)
            AND (:end IS NULL OR p.captureDate <= :end)""")
    List<Parameter> advancedFindAll(long sensorId, @Nullable Long parameterMetadataId, @Nullable Date start, @Nullable Date end);
}
