package ummisco.gamaSenseIt.springServer.data.repositories;

import com.sun.istack.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import ummisco.gamaSenseIt.springServer.data.classes.RecordList;
import ummisco.gamaSenseIt.springServer.data.model.Parameter;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
//@RepositoryRestResource(collectionResourceRel = "sensorData", path = "sensorData")
@Repository
public interface IParameterRepository extends CrudRepository<Parameter, Long> {

    @Query("""
            SELECT p FROM Parameter p
                WHERE p.sensorId = :sensorId""")
    List<Parameter> findAllBySensorId(long sensorId);


    default RecordList getRecords(
            @NotNull Sensor sensor,
            @Nullable ParameterMetadata parameterMetadata,
            @Nullable Date start,
            @Nullable Date end
    ) {
        var captures = new HashMap<Date, Map<Long, byte[]>>();
        var parameters = advancedFindAll(sensor.getId(), parameterMetadata != null ? parameterMetadata.getId() : null, start, end);
        parameters.forEach(p -> {
            var capture = captures.computeIfAbsent(p.getCaptureDate(), date -> new HashMap<>());
            System.out.println();
            capture.put(p.getParameterMetadataId(), p.getData());
        });
        var pmds = parameterMetadata == null
                ? sensor.getSensorMetadata().getParametersMetadata()
                : List.of(parameterMetadata);
        return new RecordList(pmds, captures);
    }

    @Query("""
            SELECT p FROM Parameter p
                WHERE p.sensorId = :sensorId
                    AND (:parameterMetadataId IS NULL OR p.parameterMetadataId = :parameterMetadataId)
                    AND (:start IS NULL OR p.captureDate >= :start)
                    AND (:end IS NULL OR p.captureDate <= :end)
                ORDER BY p.captureDate""")
    List<Parameter> advancedFindAll(long sensorId, @Nullable Long parameterMetadataId, @Nullable Date start, @Nullable Date end);
}
