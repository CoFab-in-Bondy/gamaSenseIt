package ummisco.gamaSenseIt.springServer.services.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.sensor.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;

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
            String ext, Sensor s, ParameterMetadata pmd, Date start, Date end) {
        return resolve(ext).export(s, pmd, start, end);
    }

}

