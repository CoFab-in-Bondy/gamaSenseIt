package fr.ummisco.gamasenseit.server.data.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import fr.ummisco.gamasenseit.server.data.model.sensor.Parameter;

import java.util.Date;
import java.util.List;

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

    List<Parameter> findBySensorIdEqualsAndParameterMetadataIdEqualsOrderByCaptureDate(Long sensorId, Long parameterMetadataId);
}
