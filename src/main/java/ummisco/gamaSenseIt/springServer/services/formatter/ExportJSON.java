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
import ummisco.gamaSenseIt.springServer.data.services.record.RecordManager;

import java.util.Date;

@Service
public class ExportJSON extends Export {

    @Autowired
    private RecordManager recordManager;

    public ExportJSON() {
        super(new MediaType("text", "json"), "json");
    }


    public Node toNode(Sensor s, ParameterMetadata pmd, Date start, Date end) {
            var smd = s.getSensorMetadata();
            return new Node() {{
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
                put("parameters", recordManager.getRecords(s, pmd, start, end).sortByDate().toNode());
            }};
    }

    @Override
    protected byte[] toBytes(Sensor s, ParameterMetadata pmd, Date start, Date end) {
        var mapper = new ObjectMapper();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        var root = toNode(s, pmd, start, end);
        try {
            return mapper.writer(prettyPrinter).writeValueAsBytes(root);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
