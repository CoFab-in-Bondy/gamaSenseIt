package fr.ummisco.gamasenseit.server.services.export;

import com.opencsv.CSVWriter;
import com.sun.istack.NotNull;
import fr.ummisco.gamasenseit.server.data.model.sensor.ParameterMetadata;
import fr.ummisco.gamasenseit.server.data.model.sensor.Sensor;
import fr.ummisco.gamasenseit.server.data.services.record.RecordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
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
        var byteArray = new ByteArrayOutputStream();
        var out = new OutputStreamWriter(byteArray, StandardCharsets.UTF_8);

        var writer = new CSVWriter(out);

        var records = recordManager.getRecords(sensor, parameterMetadata, start, end);
        var metadata = records.metadata();
        writer.writeNext(metadata.headers());
        writer.writeNext(Arrays.stream(metadata.ids()).map(Objects::toString).toArray(String[]::new));
        writer.writeNext(metadata.units());
        records.sortByDate();
        records.forEach(record -> writer.writeNext(record.asStrings()));
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray.toByteArray();
    }
}
