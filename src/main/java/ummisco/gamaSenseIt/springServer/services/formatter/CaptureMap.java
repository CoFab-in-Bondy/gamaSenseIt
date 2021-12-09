package ummisco.gamaSenseIt.springServer.services.formatter;


import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CaptureMap {

    protected final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private final List<ParameterMetadata> parameterMetadataList;
    private final Map<Date, Map<Long, byte[]>> captured;
    private List<ParameterMetadata> parametersMetadataOrdered;


    public CaptureMap(Sensor sensor, List<ParameterMetadata> parameterMetadataMap, Map<Date, Map<Long, byte[]>> captured) {
        this.parameterMetadataList = new ArrayList<>();
        this.captured = captured;
        var ids = parameterMetadataMap.stream().map(ParameterMetadata::getId).collect(Collectors.toSet());
        for (var pmd : sensor.getSensorMetadata().getParametersMetadata())
            if (ids.contains(pmd.getId()))
                parameterMetadataList.add(pmd);
    }

    public String[] headers() {
        var headers = new ArrayList<String>();
        headers.add("capture_date");
        parameterMetadataList.forEach(pmd -> headers.add(pmd.getName()));
        return headers.toArray(String[]::new);
    }

    public String[] units() {
        var headers = new ArrayList<String>();
        headers.add("datetime");
        parameterMetadataList.forEach(pmd -> headers.add(pmd.getUnit()));
        return headers.toArray(String[]::new);
    }

    public void forEach(Consumer<String[]> consumer) {
        captured.forEach((date, map) -> {
            var values = new ArrayList<String>();
            values.add(0, dateFormat.format(date));
            parameterMetadataList.forEach(pmd -> values.add(pmd.getDataType().convertToString(map.getOrDefault(pmd.getId(), null))));
            consumer.accept(values.toArray(String[]::new));
        });
    }
}
