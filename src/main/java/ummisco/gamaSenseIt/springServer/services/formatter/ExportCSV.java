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

@Service
public class ExportCSV extends Export {

    public ExportCSV() {
        super(new MediaType("text", "csv"), "csv");
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

        var captures = getCaptures(sensor, parameterMetadata, start, end);
        writer.writeNext(captures.headers());
        writer.writeNext(captures.units());
        captures.forEach(writer::writeNext);
        return out.toString().getBytes();
    }
}
