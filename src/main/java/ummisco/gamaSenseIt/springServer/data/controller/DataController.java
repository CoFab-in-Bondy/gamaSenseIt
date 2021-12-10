package ummisco.gamaSenseIt.springServer.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ummisco.gamaSenseIt.springServer.data.repositories.IParameterMetadataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.IParameterRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorMetadataRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorRepository;
import ummisco.gamaSenseIt.springServer.data.services.record.RecordManager;
import ummisco.gamaSenseIt.springServer.data.services.sensor.ISensorManagment;
import ummisco.gamaSenseIt.springServer.services.formatter.ExportResolver;

import java.util.ArrayList;
import java.util.function.Function;


public abstract class DataController {
    final static String NIL = "nil";

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
    protected RecordManager recordManager;

    @Autowired
    protected ExportResolver export;

    public static <M, D> ArrayList<D> apply(Iterable<M> list, Function<M, D> caster) {
        var result = new ArrayList<D>();
        for (var obj : list)
            result.add(caster.apply(obj));
        return result;
    }
}
