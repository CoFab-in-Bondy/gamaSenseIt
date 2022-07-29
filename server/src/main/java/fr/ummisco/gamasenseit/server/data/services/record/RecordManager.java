package fr.ummisco.gamasenseit.server.data.services.record;

import com.sun.istack.NotNull;
import fr.ummisco.gamasenseit.server.data.repositories.IParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import fr.ummisco.gamasenseit.server.data.model.sensor.ParameterMetadata;
import fr.ummisco.gamasenseit.server.data.model.sensor.Sensor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecordManager {

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
            capture.put(p.getParameterMetadataId(), p.getData());
        });
        var pmds = parameterMetadata == null
                ? sensor.getSensorMetadata().getParametersMetadata()
                : List.of(parameterMetadata);
        return new RecordList(pmds, captures);
    }
}
