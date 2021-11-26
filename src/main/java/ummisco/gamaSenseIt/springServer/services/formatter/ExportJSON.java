package ummisco.gamaSenseIt.springServer.services.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sun.istack.NotNull;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;

import java.util.*;

@Service
public class ExportJSON extends Export {

    public ExportJSON() {
        super(new MediaType("text", "json"), "json");
    }

    private void parameters(
            @NotNull Sensor s,
            @Nullable ParameterMetadata pmd,
            @Nullable Date start,
            @Nullable Date end,
            ConsumerParameter consumer
    ) {
        if (pmd == null) {
            streamParametersBySensor(s, start, end, (idParameterMetadata, name, unit, captureDate, value) -> {
                consumer.accept(idParameterMetadata, captureDate, value);
                return null;
            }).forEach(n -> {
            }); // forEach for run lazy stream
        } else {
            streamParametersByParameterMetadata(s, pmd, start, end, (captureDate, value) -> {
                consumer.accept(pmd.getId(), captureDate, value);
                return null;
            }).forEach(n -> {
            }); // forEach for run lazy stream
        }
    }

    private ArrayNode addParameterMetadataToArrayNode(ObjectMapper mapper, ParameterMetadata pmd, ArrayNode array) {
        var arr = mapper.createArrayNode();
        array.addObject()
                .put("name", pmd.getName())
                .put("unit", pmd.getUnit())
                .set("captures", arr);
        List<ParameterMetadata> pmds;
        return arr;
    }

    @Override
    protected byte[] toBytes(Sensor s, ParameterMetadata pmd, Date start, Date end) {
        var mapper = new ObjectMapper();
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

        var pmdArrayNode = mapper.createArrayNode();
        sNode.set("parameters", pmdArrayNode);


        Map<Long, Integer> idParameterMetadataToIndex;
        ArrayNode[] captures;

        if (pmd == null) {
            var pmds = smd.getParametersMetadata();
            idParameterMetadataToIndex = new HashMap<>();
            captures = new ArrayNode[pmds.size()];
            for (int i = 0; i < pmds.size(); i++) {
                var pmd2 = pmds.get(i);
                captures[i] = addParameterMetadataToArrayNode(mapper, pmd2, pmdArrayNode);
                idParameterMetadataToIndex.put(pmd2.getId(), i);
            }
        } else {
            idParameterMetadataToIndex = Collections.singletonMap(pmd.getId(), 0);
            captures = new ArrayNode[]{addParameterMetadataToArrayNode(mapper, pmd, pmdArrayNode)};
        }

        parameters(s, pmd, start, end, (idParameterMetadata, captureDate, value) -> {
            var arr = captures[idParameterMetadataToIndex.get(idParameterMetadata)];
            arr.addObject()
                    .put("date", dateFormat.format(captureDate))
                    .put("value", value.toString());
        });

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(sNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }


    private interface ConsumerParameter {
        void accept(long idParameterMetadata, Date captureDate, Object value);
    }
}
