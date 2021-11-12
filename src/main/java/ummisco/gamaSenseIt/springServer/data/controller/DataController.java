package ummisco.gamaSenseIt.springServer.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ummisco.gamaSenseIt.springServer.data.model.IConvertible;
import ummisco.gamaSenseIt.springServer.data.repositories.IParameterMetadataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.IParameterRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorMetadataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorRepository;
import ummisco.gamaSenseIt.springServer.data.services.sensor.ISensorManagment;
import ummisco.gamaSenseIt.springServer.services.formatter.FormattedResponseFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;


public abstract class DataController {
    final static String NIL = "nil";

    final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");

    @Autowired
    protected ISensorRepository sensorsRepo;

    @Autowired
    protected ISensorMetadataRepository sensorsMetadataRepo;

    @Autowired
    protected ISensorManagment sensorsManagementRepo;

    @Autowired
    protected IParameterRepository parametersRepo;

    @Autowired
    protected IParameterMetadataRepository parametersMetadataRepo;

    @Autowired
    protected FormattedResponseFactory formattedResponseFactory;

    public static <M, D> ArrayList<D> display(Iterable<M> list, Converter<M, D> caster) {
        var result = new ArrayList<D>();
        for (var obj : list)
            result.add(caster.cast(obj));
        return result;
    }

    public static <D> ArrayList<D> display(Iterable<? extends IConvertible<D>> list) {
        var result = new ArrayList<D>();
        for (var convertible : list)
            result.add(convertible.convert());
        return result;
    }

    public static <D> D display(Optional<? extends IConvertible<D>> optionalConvertible) {
        return optionalConvertible.isEmpty() ? null : optionalConvertible.get().convert();
    }

    interface Converter<M, D> {
        D cast(M m);
    }
}
