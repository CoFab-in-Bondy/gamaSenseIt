package ummisco.gamaSenseIt.springServer.data.services.record;

import ummisco.gamaSenseIt.springServer.data.classes.Node;
import ummisco.gamaSenseIt.springServer.data.model.sensor.ParameterMetadata;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Function;

public class RecordListMetadata {

    private final List<ParameterMetadata> parametersMetadata;

    RecordListMetadata(List<ParameterMetadata> parameterMetadata) {
        this.parametersMetadata = parameterMetadata;
    }

    @SuppressWarnings("unchecked")
    private <T> T[] metadata(Class<T> klass, T dateCase, Function<ParameterMetadata, T> extractor) {
        int size = parametersMetadata.size() + 1;
        var headers = (T[]) Array.newInstance(klass, size);
        headers[0] = dateCase;
        for (int i = 1; i < size; i++)
            headers[i] = extractor.apply(parametersMetadata.get(i - 1));
        return headers;
    }

    public String[] headers() {
        return metadata(String.class, "captureDate", ParameterMetadata::getName);
    }

    public String[] units() {
        return metadata(String.class, "datetime", ParameterMetadata::getUnit);
    }

    public Long[] ids() {
        return metadata(Long.class, -1L, ParameterMetadata::getId);
    }

    public String[] formats() {
        return metadata(String.class, "DATE", p-> p.getDataType().toString());
    }

    public long width() {
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
