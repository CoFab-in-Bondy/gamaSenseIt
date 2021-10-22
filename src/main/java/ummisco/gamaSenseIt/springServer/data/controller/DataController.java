package ummisco.gamaSenseIt.springServer.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ummisco.gamaSenseIt.springServer.data.model.IConvertible;
import ummisco.gamaSenseIt.springServer.data.repositories.IParameterMetadataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorDataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorMetadataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorRepository;
import ummisco.gamaSenseIt.springServer.data.services.sensor.ISensorManagment;
import ummisco.gamaSenseIt.springServer.services.formatter.FormattedResponseFactory;

import java.util.ArrayList;
import java.util.Optional;


public abstract class DataController {
    final static String NIL = "nil";

    @Autowired
    protected ISensorRepository sensors;

    @Autowired
    protected ISensorMetadataRepository sensorsMetadata;

    @Autowired
    protected ISensorManagment sensorsManagement;

    @Autowired
    protected ISensorDataRepository sensorsData;

    @Autowired
    protected IParameterMetadataRepository parametersMetadata;

    @Autowired
    protected FormattedResponseFactory formatter;

    public static <MT, D> ArrayList<D> display(Iterable<MT> list, PublicDataController.Converter<MT, D> caster) {
        var result = new ArrayList<D>();
        for (var data : list)
            result.add(caster.cast(data));
        return result;
    }

    public static <D> ArrayList<D> display(Iterable<? extends IConvertible<D>> list) {
        var result = new ArrayList<D>();
        for (var data : list)
            result.add(data.convert());
        return result;
    }

    public static <D> D display(Optional<? extends IConvertible<D>> optional) {
        return optional.isEmpty() ? null : optional.get().convert();
    }

    interface Converter<MT, D> {
        D cast(MT mt);
    }
}
