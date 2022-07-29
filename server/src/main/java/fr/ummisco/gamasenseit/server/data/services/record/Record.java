package fr.ummisco.gamasenseit.server.data.services.record;

import com.sun.istack.NotNull;
import fr.ummisco.gamasenseit.server.data.services.date.DateUtils;

import java.util.*;

public class Record extends ArrayList<Object> implements Comparable<Record> {

    public Date date;

    public Record(@NotNull Date date, @NotNull Collection<Object> values) {
        super(values);
        add(0, date);
        this.date = date;
    }
    @NotNull
    public  Object[] asObjects() {
        return toArray(Object[]::new);
    }
    
    @NotNull
    public  String[] asStrings() {
        var strings = new String[size()];
        strings[0] = DateUtils.formatCompact(date);
        for (int i = 1; i < size(); i ++)
            strings[i] = get(i) == null? "null" : get(i).toString();
        return strings;
    }

    @Override
    public int compareTo(Record o) {
        return date.compareTo(o.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(date);
    }

    @Override
    public String toString() {
        return Arrays.toString(asObjects());
    }
}
