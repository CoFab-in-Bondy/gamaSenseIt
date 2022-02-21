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
import ummisco.gamaSenseIt.springServer.data.services.record.RecordManager;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service("ExportJSON")
public class ExportJSON extends Export {

    @Autowired
    private RecordManager recordManager;

    public ExportJSON() {
        super(new MediaType("text", "json"), "json");
    }


    public Node toNode(
            Sensor s,
            ParameterMetadata pmd,
            Date start,
            Date end,
            Integer sort,
            Boolean asc,
            Integer page,
            Integer count
    ) {
        var root = s.toNode();
        var records = recordManager.getRecords(s, pmd, start, end);
        records.sortBy(Objects.requireNonNullElse(sort, 0), Objects.requireNonNullElse(asc, true));

        if (page != null)
            records.page(page, Objects.requireNonNullElse(count, 50));
        else if (count != null)
            records.page(0, count);

        root.put("parameters", records.toNode());
        System.out.println("RESULT OF " + s + " " + pmd + " " + start + " " + end + " " + sort + " " + asc + " " + page + " " + count);
        return root;
    }

    public Node toNode(
            Sensor s,
            ParameterMetadata pmd,
            Date start,
            Date end
    ) {
        return toNode(s, pmd, start, end, null, null, null, null);
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
