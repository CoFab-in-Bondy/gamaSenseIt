package ummisco.gamaSenseIt.springServer.services.formatter;

import com.opencsv.CSVWriter;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;
import ummisco.gamaSenseIt.springServer.data.repositories.IParameterRepository;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Service
public class ExportCSV extends Export {

    @Autowired
    private IParameterRepository parameterRepo;


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

        var records = parameterRepo.getRecords(sensor, parameterMetadata, start, end);
        writer.writeNext(records.headers());
        writer.writeNext(Arrays.stream(records.ids()).map(Objects::toString).toArray(String[]::new));
        writer.writeNext(records.units());
        records.forEach(record -> writer.writeNext(record.asStrings()));
        return out.toString().getBytes();
    }
}
