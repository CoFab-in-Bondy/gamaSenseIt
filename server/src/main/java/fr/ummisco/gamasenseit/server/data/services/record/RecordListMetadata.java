package fr.ummisco.gamasenseit.server.data.services.record;

import fr.ummisco.gamasenseit.server.data.model.sensor.DataFormat;
import fr.ummisco.gamasenseit.server.data.classes.Node;
import fr.ummisco.gamasenseit.server.data.model.sensor.ParameterMetadata;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Function;

public class RecordListMetadata {

    private final List<ParameterMetadata> parametersMetadata;

    public RecordListMetadata(List<ParameterMetadata> parameterMetadata) {
        this.parametersMetadata = parameterMetadata;
    }

    @SuppressWarnings("unchecked")
    private <T> T[] buildMetadata(Class<T> klass, T dateCase, Function<ParameterMetadata, T> extractor) {
        int size = parametersMetadata.size() + 1;
        var headers = (T[]) Array.newInstance(klass, size);
        headers[0] = dateCase;
        for (int i = 1; i < size; i++)
            headers[i] = extractor.apply(parametersMetadata.get(i - 1));
        return headers;
    }

    public String[] headers() {
        return buildMetadata(String.class, "captureDate", ParameterMetadata::getName);
    }

    public DataFormat[] types() {
        return buildMetadata(DataFormat.class, DataFormat.DATE, ParameterMetadata::getDataType);
    }

    public String[] units() {
        return buildMetadata(String.class, "datetime", ParameterMetadata::getUnit);
    }

    public Long[] ids() {
        return buildMetadata(Long.class, -1L, ParameterMetadata::getId);
    }

    public String[] formats() {
        return buildMetadata(String.class, "DATE", p-> p.getDataType().toString());
    }

    public int width() {
        return parametersMetadata.size() + 1;
    }

    public Node toNode() {
        return new Node() {{
            put("headers", headers());
            put("ids", ids());
            put("units", units());
            put("formats", formats());
            put("width", width());
        }};
    }
}
