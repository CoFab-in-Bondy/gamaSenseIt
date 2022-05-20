package ummisco.gamaSenseIt.springServer.data.model.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Data {

    @JsonProperty("name")
    private String name;
    @JsonProperty("value")
    private Object[] value;

    public Data(String name, Object... values) {
        this.name = name;
        this.value = values;
    }

    public Data(Parameter parameter) {
        var date = parameter.getCaptureDate();
        var dt = new SimpleDateFormat("EE MMM dd yyyy HH:mm:ss Z", Locale.ENGLISH);
        var dts = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH);
        this.name = dt.format(date);
        this.value = new Object[]{dts.format(date), parameter.value()};
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object[] getValue() {
        return value;
    }

    public void setValue(Object[] value) {
        this.value = value;
    }
}
