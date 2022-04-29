package ummisco.gamaSenseIt.springServer.data.services.sensor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ummisco.gamaSenseIt.springServer.data.model.sensor.*;
import ummisco.gamaSenseIt.springServer.data.model.user.*;
import ummisco.gamaSenseIt.springServer.data.repositories.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Service
public class SensorManagement implements ISensorManagement {

    @Autowired
    IParameterMetadataRepository parameterMetadataRepo;

    @Autowired
    ISensorRepository sensorRepo;

    @Autowired
    IAccessRepository accessRepository;

    @Autowired
    IAccessUserRepository accessUserRepository;

    @Autowired
    IAccessSensorRepository accessSensorRepository;

    @Autowired
    ISensoredBulkDataRepository bulkDataRepo;

    @Autowired
    ISensorMetadataRepository sensorMetadataRepo;

    @Autowired
    IParameterRepository analysedDataRepo;

    @Autowired
    ISensorDataAnalyser dataAnalyser;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void saveData(String message, Date date) {

        String[] data = message.split(";", 4);
        if (data.length < 4)
            return;
        long captureTimestamp, token;
        String sensorName = data[1];

        try {
            captureTimestamp = Long.parseLong(data[0]);
            token = Long.parseLong(data[2]);
        } catch (NumberFormatException e) {
            return;
        }

        String contents = data[3];
        List<Sensor> foundSensors = sensorRepo.findByName(sensorName);
        if (foundSensors.isEmpty())
            return;

        
        Sensor sensor = foundSensors.get(0);

        Date capturedate = new Date(captureTimestamp * 1000);
        
        long diff = Math.abs(date.getTime() - capturedate.getTime());
        long diffDays = (diff / (1000 * 60 * 60 * 24));
        if(diffDays > 3)
        	capturedate = date;

        /*
         * System.out.println(
         * "*************************************************************************************"
         * );
         *
         * System.out.println("capture date "+capturedateS);
         *
         * System.out.println("sensorName "+sensorName);
         * System.out.println("sensorName "+sensorName);
         * System.out.println("token "+token); System.out.println("contents "+contents);
         */

        SensoredBulkData bulkData = new SensoredBulkData(sensor, token, capturedate, date, contents);
        bulkDataRepo.save(bulkData);
        sensor.setNotified(false);
        sensorRepo.save(sensor);
        List<Parameter> parameters = dataAnalyser.analyseBulkData(contents, capturedate, sensor);
        // System.out.println("*************************************************************************************");

        analysedDataRepo.saveAll(parameters);
    }

    @Override
    public Sensor updateSensorInformation(Sensor s) {
        return sensorRepo.save(s);
    }

    @Override
    public ParameterMetadata addParameterToSensorMetadata(SensorMetadata smd, ParameterMetadata pmd) {
        pmd.setSensorMetadata(smd);
        sensorMetadataRepo.save(smd);
        return parameterMetadataRepo.save(pmd);
    }

    @Override
    public SensorMetadata addSensorMetadata(SensorMetadata smd, Collection<ParameterMetadata> pmds) {
        if (smd.getIcon() == null) {
            System.out.println("SET IMAGE ICON");
            try {
                var icon = resourceLoader.getResource("classpath:qameleo_icon.png").getInputStream().readAllBytes();
                smd.setIcon(icon);
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
        var smdSaved = sensorMetadataRepo.save(smd);
        int idx = Stream.concat(smdSaved.getParametersMetadata().stream(), pmds.stream())
                .map(ParameterMetadata::getIdx)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(-1) + 1;
        for (var pmd : pmds) {
            if (pmd.getIdx() == null) {
                idx ++;
                pmd.setIdx(idx);
            }
            pmd.setSensorMetadata(smdSaved);
            parameterMetadataRepo.save(pmd);
        }
        return smdSaved;
    }

    /**
     * Save a sensor to the persistent layer and add access to the creator in a special group owners
     *
     * @param sensor Sensor to save in persistent layer
     * @param userId User Id of original creator
     * @return The saved sensor with updated default field
     **/
    @Override
    @Transactional
    public Sensor addSensorForUser(Sensor sensor, long userId) {
        // TODO: qui ajoute les metasensor : les admins
        var sensorSaved = sensorRepo.save(sensor);
        var access = new Access(sensor.getDisplayName(), AccessPrivilege.MAINTENANCE);
        var accessId = accessRepository.save(access).getId();
        accessSensorRepository.save(new AccessSensor(accessId, sensorSaved.getId()));
        accessUserRepository.save(new AccessUser(accessId, userId, AccessUserPrivilege.MANAGE));
        return sensorSaved;
    }

    /**
     * Save a sensor to the persistent layer and add access to the creator in a special group owners
     *
     * @param sensor Sensor to save in persistent layer
     * @return The saved sensor with updated default field
     **/
    @Override
    public Sensor patchSensor(Sensor sensor) {
        return sensorRepo.save(sensor);
    }

    @Override
    public SensorMetadata addSensorMetadata(SensorMetadata sensorMetadata) {
        return sensorMetadataRepo.save(sensorMetadata);
    }
}
