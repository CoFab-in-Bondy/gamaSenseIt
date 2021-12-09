package ummisco.gamaSenseIt.springServer.services.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.classes.Node;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;
import ummisco.gamaSenseIt.springServer.data.repositories.IParameterRepository;

import java.util.Date;

@Service
public class ExportJSON extends Export {

    @Autowired
    private IParameterRepository parameterRepo;

    public ExportJSON() {
        super(new MediaType("text", "json"), "json");
    }

    @Override
    protected byte[] toBytes(Sensor s, ParameterMetadata pmd, Date start, Date end) {
        var mapper = new ObjectMapper();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

        var smd = s.getSensorMetadata();

        var root = new Node() {{
            put("id", s.getId());
            put("name", s.getName());
            put("displayName", s.getDisplayName());
            put("subDisplayName", s.getSubDisplayName());
            put("latitude", s.getLatitude());
            put("longitude", s.getLongitude());
            put("metadata", new Node() {{
                put("id", smd.getId());
                put("description", smd.getDescription());
                put("name", smd.getName());
                put("version", smd.getVersion());
            }});
            put("parameters", parameterRepo.getRecords(s, pmd, start, end).toMap());
        }};

        try {
            return mapper.writer(prettyPrinter).writeValueAsBytes(root);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
