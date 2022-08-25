package fr.ummisco.gamasenseit.server.services.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ummisco.gamasenseit.server.data.classes.Node;
import fr.ummisco.gamasenseit.server.data.model.sensor.ParameterMetadata;
import fr.ummisco.gamasenseit.server.data.model.sensor.Sensor;
import fr.ummisco.gamasenseit.server.data.services.record.RecordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service("ExportJSON")
public class ExportJSON extends Export {

    @Autowired
    private RecordManager recordManager;

    public ExportJSON() {
        super(new MediaType("text", "json"), "json", true);
    }


    public Node toNode(
            Sensor s,
            ParameterMetadata pmd,
            Date start,
            Date end,
            Integer sort,
            Boolean asc,
            Integer page,
            Integer count,
            boolean complete
    ) {

        var records = recordManager.getRecords(s, pmd, start, end);
        int size = records.size();
        records.sortBy(Objects.requireNonNullElse(sort, 0), Objects.requireNonNullElse(asc, false));

        if (page != null)
            records.page(page, Objects.requireNonNullElse(count, 50));
        else if (count != null)
            records.page(0, count);

        var parameters = new Node() {{put("values", records);put("total", size);}};
        if (complete) {
            var root = s.toNode();
            root.put("parameters", parameters);
            return root;
        } else {
            return parameters;
        }
    }

    public Node toNode(
            Sensor s,
            ParameterMetadata pmd,
            Date start,
            Date end
    ) {
        return toNode(s, pmd, start, end, null, null, null, null, true);
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
