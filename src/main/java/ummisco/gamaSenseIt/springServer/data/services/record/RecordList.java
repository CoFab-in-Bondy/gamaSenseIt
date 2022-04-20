package ummisco.gamaSenseIt.springServer.data.services.record;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.comparator.Comparators;
import ummisco.gamaSenseIt.springServer.data.classes.Node;
import ummisco.gamaSenseIt.springServer.data.model.sensor.ParameterMetadata;

import java.util.*;

public class RecordList extends ArrayList<Record> {

    private static final Logger logger = LoggerFactory.getLogger(RecordList.class);

    private final RecordListMetadata metadata;
    private final int total;

    public RecordList(List<ParameterMetadata> parameterMetadata, Map<Date, Map<Long, byte[]>> captured) {
        super(captured.size());
        this.metadata = new RecordListMetadata(parameterMetadata);
        for (var e : captured.entrySet()) {
            var values = new ArrayList<Object>();
            parameterMetadata.forEach(pmd -> {
                var data = e.getValue().getOrDefault(pmd.getId(), null);
                values.add(pmd.getDataType().convertToObject(data));
            });
            super.add(new Record(e.getKey(), values));
        }
        this.total = size();
    }

    public RecordListMetadata getMetadata() {
        return this.metadata;
    }
    
    public Node toNode() {
        return new Node() {{
            put("metadata", metadata.toNode());
            put("values", RecordList.this);
            put("total", RecordList.this.total);
        }};
    }

    public RecordList sortByDate() {
        sort(Record::compareTo);
        return this;
    }

    @SuppressWarnings("unchecked")
    public void sortBy(final int index, final boolean asc) {
        if (0 > index || index >= metadata.width())
            return;
        try {
            // null-friendly comparator
            sort((r1, r2) -> {
                var v1 = (Comparable<Object>)r1.get(index);
                var v2 = (Comparable<Object>)r2.get(index);
                if (v1 == null) {
                    return (v2 == null) ? 0 : 1;
                } else if (v2 == null) {
                    return -1;
                } else {
                    return asc? v2.compareTo(v1): v1.compareTo(v2);
                }
            });
        } catch (ClassCastException ignored) {
            logger.error("Error during casting for record");
        }
    }

    public void page(int page, int count) {
        int start = page * count;
        int end = (page + 1) * count;

        if (start >= size() || end <= 0) {
            clear();
            return;
        }
        if (start < 0)
            start = 0;

        if (end >= size())
            end = size();

        var sub = new LinkedList<Record>();
        for (int i = start; i < end; i ++)
            sub.add(get(i));
        clear();
        addAll(sub);
    }
}
