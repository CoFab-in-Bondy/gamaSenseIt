package ummisco.gamaSenseIt.springServer.data.services.record;


import ummisco.gamaSenseIt.springServer.data.classes.Node;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

public class RecordList extends ArrayList<Record> {

    private final List<ParameterMetadata> parametersMetadata;

    public RecordList(List<ParameterMetadata> parameterMetadata, Map<Date, Map<Long, byte[]>> captured) {
        super(captured.size());
        this.parametersMetadata = parameterMetadata;


        for (var e : captured.entrySet()) {
            var values = new ArrayList<Object>();
            parameterMetadata.forEach(pmd ->
                    values.add(pmd.getDataType().convertToObject(e.getValue().getOrDefault(pmd.getId(), null)))
            );
            super.add(new Record(e.getKey(), values));
        }
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
        return ids().length;
    }

    public Node toNode() {
        return new Node() {{
            put("metadata", new Node() {{
                put("headers", headers());
                put("ids", ids());
                put("units", units());
                put("formats", formats());
                put("width", parametersMetadata.size());
                put("length", size());
            }});
            put("values", RecordList.this);
        }};
    }

    public RecordList sortByDate() {
        sort(Record::compareTo);
        return this;
    }

    @SuppressWarnings("unchecked")
    public RecordList sortBy(final int index) {
        if (0 > index || index <= width())
            return this;
        try {
            sort(Comparator.comparing(r -> ((Comparable<Object>)r.get(index))));
        } catch (ClassCastException ignored) {}
        return this;
    }
}
