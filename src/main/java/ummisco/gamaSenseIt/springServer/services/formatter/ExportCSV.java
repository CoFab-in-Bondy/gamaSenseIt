package ummisco.gamaSenseIt.springServer.services.formatter;

import com.opencsv.CSVWriter;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;
import ummisco.gamaSenseIt.springServer.data.services.record.RecordManager;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Service("ExportCSV")
public class ExportCSV extends Export {

    @Autowired
    private RecordManager recordManager;


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

        var records = recordManager.getRecords(sensor, parameterMetadata, start, end);
        var metadata = records.getMetadata();
        writer.writeNext(metadata.headers());
        writer.writeNext(Arrays.stream(metadata.ids()).map(Objects::toString).toArray(String[]::new));
        writer.writeNext(metadata.units());
        records.forEach(record -> writer.writeNext(record.asStrings()));
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString().getBytes();
    }
}
