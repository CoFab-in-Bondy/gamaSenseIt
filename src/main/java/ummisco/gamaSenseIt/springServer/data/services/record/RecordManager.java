package ummisco.gamaSenseIt.springServer.data.services.record;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.sensor.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.data.repositories.IParameterRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecordManager {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Autowired
    private IParameterRepository parameterRepo;

    public RecordList getRecords(
            @NotNull Sensor sensor,
            @Nullable ParameterMetadata parameterMetadata,
            @Nullable Date start,
            @Nullable Date end
    ) {
        var captures = new HashMap<Date, Map<Long, byte[]>>();
        var parameters = parameterRepo.advancedFindAll(sensor.getId(), parameterMetadata != null ? parameterMetadata.getId() : null, start, end);
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
}
