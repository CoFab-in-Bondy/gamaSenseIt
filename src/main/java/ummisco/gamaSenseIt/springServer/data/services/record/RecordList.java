package ummisco.gamaSenseIt.springServer.data.services.record;


import ummisco.gamaSenseIt.springServer.data.classes.Node;
import ummisco.gamaSenseIt.springServer.data.model.sensor.ParameterMetadata;

import java.util.*;

public class RecordList extends ArrayList<Record> {

    private final RecordListMetadata metadata;
    private final int total;

    public RecordList(List<ParameterMetadata> parameterMetadata, Map<Date, Map<Long, byte[]>> captured) {
        super(captured.size());
        this.metadata = new RecordListMetadata(parameterMetadata);
        for (var e : captured.entrySet()) {
            var values = new ArrayList<Object>();
            parameterMetadata.forEach(pmd ->
                    values.add(pmd.getDataType().convertToObject(e.getValue().getOrDefault(pmd.getId(), null)))
            );
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
    public void sortBy(final int index, boolean asc) {
        if (0 > index || index >= metadata.width())
            return;
        try {
            System.out.println("Sorting recordlist");
            Comparator<Record> cmp = Comparator.comparing(r -> ((Comparable<Object>)r.get(index)));
            if (asc)
                cmp = cmp.reversed();
            sort(cmp);
        } catch (ClassCastException ignored) {
            System.err.println("Error during casting for record");
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
