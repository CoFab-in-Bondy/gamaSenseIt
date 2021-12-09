package ummisco.gamaSenseIt.springServer.services.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;

import java.util.Date;

@Service
public class ExportJSON extends Export {

    public ExportJSON() {
        super(new MediaType("text", "json"), "json");
    }

    @Override
    protected byte[] toBytes(Sensor s, ParameterMetadata pmd, Date start, Date end) {
        var mapper = new ObjectMapper();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

        var smd = s.getSensorMetadata();

        var sNode = mapper.createObjectNode()
                .put("id", s.getId())
                .put("name", s.getName())
                .put("displayName", s.getDisplayName())
                .put("subDisplayName", s.getSubDisplayName())
                .put("latitude", s.getLatitude())
                .put("longitude", s.getLongitude());

        sNode.set("metadata", mapper.createObjectNode()
                .put("description", smd.getDescription())
                .put("name", smd.getName())
                .put("version", smd.getVersion()));

        var pmdNode = mapper.createObjectNode();
        sNode.set("parameters", pmdNode);

        CaptureMap captures = getCaptures(s, pmd, start, end);

        var headersNode = mapper.createArrayNode();
        pmdNode.set("headers", headersNode);
        for (var header : captures.headers())
            headersNode.add(header);

        var unitsNode = mapper.createArrayNode();
        pmdNode.set("units", unitsNode);
        for (var unit : captures.units())
            unitsNode.add(unit);

        var valuesNode = mapper.createArrayNode();
        pmdNode.set("values", valuesNode);
        captures.forEach(values -> {
            var captureNode = mapper.createArrayNode();
            valuesNode.add(captureNode);

            for (var value : values)
                captureNode.add(value);
        });

        try {
            return mapper.writer(prettyPrinter).writeValueAsBytes(sNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }


    private interface ConsumerParameter {
        void accept(long idParameterMetadata, Date captureDate, Object value);
    }
}
