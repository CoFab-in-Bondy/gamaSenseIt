package ummisco.gamaSenseIt.springServer.services.formatter;

import com.opencsv.CSVWriter;
import com.sun.istack.NotNull;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;

import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportCSV extends Export {

    public ExportCSV() {
        super(new MediaType("text", "csv"), "csv");
    }

    private List<String[]> parameters(Sensor sensor, @Nullable ParameterMetadata parameterMetadata, @Nullable Date start, @Nullable Date end) {
        if (parameterMetadata == null) {
            return streamParametersBySensor(sensor, start, end, (idParameterMetadata, name, unit, captureDate, value) -> new String[]{
                    name,
                    unit,
                    dateFormat.format(captureDate),
                    value.toString(),
            }).collect(Collectors.toList());
        } else {
            return streamParametersByParameterMetadata(sensor, parameterMetadata, start, end, (captureDate, value) -> new String[]{
                    dateFormat.format(captureDate),
                    value.toString()
            }).collect(Collectors.toList());
        }
    }

    @Override
    protected byte[] toBytes(
            @NotNull Sensor sensor,
            @Nullable ParameterMetadata parameterMetadata,
            @Nullable Date start,
            @Nullable Date end
    ) {
        var out = new StringWriter();
        var writer = new CSVWriter(out);
        if (parameterMetadata == null)
            writer.writeNext(new String[]{"name", "unit", "captureDate", "captureDate"});
        else
            writer.writeNext(new String[]{"captureDate", "value"});
        for (var line : parameters(sensor, parameterMetadata, start, end))
            writer.writeNext(line);
        return out.toString().getBytes();
    }
}
