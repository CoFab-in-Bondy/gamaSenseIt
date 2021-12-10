package ummisco.gamaSenseIt.springServer.data.services.record;

import com.sun.istack.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

public class Record extends ArrayList<Object> implements Comparable<Record> {

    protected final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public Date date;

    public Record(@NotNull Date date, @NotNull Collection<Object> values) {
        super(values);
        add(0, dateFormat.format(date));
        this.date = date;
    }

    public @NotNull
    Object[] asObjects() {
        return toArray(Object[]::new);
    }

    public @NotNull
    String[] asStrings() {
        return stream().map(Objects::toString).toArray(String[]::new);
    }

    @Override
    public int compareTo(Record o) {
        return date.compareTo(o.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(date);
    }
}
