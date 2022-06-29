package ummisco.gamaSenseIt.springServer.data.services.record;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ummisco.gamaSenseIt.springServer.data.model.sensor.ParameterMetadata;

import java.util.*;

public class RecordList extends ArrayList<Record> {

    private static final Logger logger = LoggerFactory.getLogger(RecordList.class);

    private final List<ParameterMetadata> parametersMetadata;

    public RecordList(List<ParameterMetadata> parametersMetadata, Map<Date, Map<Long, byte[]>> captured) {
        super(captured.size());
        this.parametersMetadata = parametersMetadata;
        for (var e : captured.entrySet()) {
            var values = new ArrayList<Object>();
            parametersMetadata.forEach(pmd -> {
                var data = e.getValue().getOrDefault(pmd.getId(), null);
                values.add(pmd.getDataType().bytesToObject(data));
            });
            super.add(new Record(e.getKey(), values));
        }
    }

    public RecordListMetadata metadata() {
        return new RecordListMetadata(this.parametersMetadata);
    }

    public List<ParameterMetadata> getParametersMetadata() {
        return parametersMetadata;
    }

    public void sortByDate() {
        sort(Record::compareTo);
    }

    @SuppressWarnings("unchecked")
    public void sortBy(final int index, final boolean asc) {
        if (0 > index || index >= getParametersMetadata().size())
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
