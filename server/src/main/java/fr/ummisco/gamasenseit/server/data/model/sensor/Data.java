package fr.ummisco.gamasenseit.server.data.model.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ummisco.gamasenseit.server.data.services.date.DateUtils;

public class Data {
    /* Point for javascript */

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
        this.name = DateUtils.formatJavascript(date);
        this.value = new Object[]{DateUtils.formatCompact(date), parameter.value()};
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
