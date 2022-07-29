package fr.ummisco.gamasenseit.server.services.export;

import fr.ummisco.gamasenseit.server.data.model.sensor.ParameterMetadata;
import fr.ummisco.gamasenseit.server.data.model.sensor.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service("ExportResolver")
public class ExportResolver {

    @Autowired
    private List<Export> exports;

    @Autowired
    private ExportJSON exportDefault;

    public Export resolve(String name) {
        if (name != null)
            for (var export : exports)
                if (export.getExt().equals(name))
                    return export;
        return exportDefault;
    }

    public ResponseEntity<Resource> format(
            String ext, Sensor s, ParameterMetadata pmd, Date start, Date end) throws IOException {
        return resolve(ext).export(s, pmd, start, end);
    }

}

