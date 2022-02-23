package ummisco.gamaSenseIt.springServer.services.activation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.SensorMetadata;
import ummisco.gamaSenseIt.springServer.data.model.user.User;
import ummisco.gamaSenseIt.springServer.data.model.user.UserPrivilege;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorRepository;
import ummisco.gamaSenseIt.springServer.data.services.sensor.SensorManagment;
import ummisco.gamaSenseIt.springServer.data.services.user.UserManagment;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class BaseActivation {

    @Autowired
    UserManagment userManagment;

    @Autowired
    SensorManagment sensorManagment;

    @Autowired
    ISensorRepository sensorRepo;

    @PostConstruct
    public void activate() throws IOException {
        if (sensorRepo.count() != 0) return;

        var nmarilleau = userManagment.createIfNotExistUser("luis", "bondel", "nmarilleau@gmail.com", "123456", UserPrivilege.ADMIN);
        var space = userManagment.createIfNotExistUser(" ", " ", " ", " ", UserPrivilege.ADMIN);
        var admin = userManagment.createIfNotExistUser("admin", "admin", "admin", "admin", UserPrivilege.ADMIN);
        var user = userManagment.createIfNotExistUser("user", "user", "user", "user", UserPrivilege.USER);
        var pub = userManagment.createIfNotExistUser("public", "public", "public", "public", UserPrivilege.USER);

        var qameleo2 = sensorManagment.addSensorMetadata(
                new SensorMetadata("Qaméléo", "1.0.0", ":", "Des magnifiques capteur de qualité de l'air"),
                List.of(
                        new ParameterMetadata("Température", "°C", ParameterMetadata.DataFormat.DOUBLE, "temp"),
                        new ParameterMetadata("Humidité", "°C", ParameterMetadata.DataFormat.DOUBLE, "humi"),
                        new ParameterMetadata("Particule par minute de taille 10", "°C", ParameterMetadata.DataFormat.DOUBLE, "nuage"),
                        new ParameterMetadata("Particule par minute de taille 2.5", "°C", ParameterMetadata.DataFormat.DOUBLE, "nuage"),
                        new ParameterMetadata("Particule par minute de taille 1", "°C", ParameterMetadata.DataFormat.DOUBLE, "nuage"),
                        new ParameterMetadata("o1", "°C", ParameterMetadata.DataFormat.DOUBLE, "other")
                )
        );

        var qameleo1 = sensorManagment.addSensorMetadata(
                new SensorMetadata("Qaméléo v2", "1.0.1", ":", "Des magnifiques capteur de qualité de l'air v2"),
                List.of(
                        new ParameterMetadata("Température", "°C", ParameterMetadata.DataFormat.DOUBLE, "temp"),
                        new ParameterMetadata("Humidité", "°C", ParameterMetadata.DataFormat.DOUBLE, "humi"),
                        new ParameterMetadata("Particule par minute de taille 10", "°C", ParameterMetadata.DataFormat.DOUBLE, "nuage"),
                        new ParameterMetadata("Particule par minute de taille 2.5", "°C", ParameterMetadata.DataFormat.DOUBLE, "nuage"),
                        new ParameterMetadata("Particule par minute de taille 1", "°C", ParameterMetadata.DataFormat.DOUBLE, "nuage")
                )
        );

        generateDataForSensorMetadata(
                qameleo1,
                user,
                30,
                "DAK_QAM",
                "Qaméléo Dakar ",
                "Désolé, ce capteur n''est pas disponible",
                -17.4572,
                14.7286,
                0.05
        );

        generateDataForSensorMetadata(
                qameleo2,
                user,
                30,
                "DIJ_QAM",
                "Qaméléo Dijon ",
                "Désolé, ce capteur n''est pas disponible",
                5.0441,
                47.325,
                0.05
        );
    }

    public List<Sensor> generateDataForSensorMetadata(SensorMetadata smd, User user, long count, String prefixName, String prefixDisplayName, String hiddenMessage, double locationX, double locationY, double spray) {
        List<Sensor> all = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            var vlocationX = locationX + (Math.random() - 0.5) * 2 * spray;
            var vlocationY = locationY + (Math.random() - 0.5) * 2 * spray;
            var s = new Sensor(prefixName + i, prefixDisplayName + i, vlocationX, vlocationY, smd);
            s.setHiddenMessage(hiddenMessage);
            s.setSubDisplayName("NoneDisplay");
            all.add(sensorManagment.addSensorForUser(s, user.getId()));
        }
        return all;
    }
}
