package ummisco.gamaSenseIt.springServer.services.activation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.sensor.DataFormat;
import ummisco.gamaSenseIt.springServer.data.model.sensor.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.sensor.SensorMetadata;
import ummisco.gamaSenseIt.springServer.data.model.user.User;
import ummisco.gamaSenseIt.springServer.data.model.user.UserPrivilege;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorRepository;
import ummisco.gamaSenseIt.springServer.data.services.sensor.SensorManagement;
import ummisco.gamaSenseIt.springServer.data.services.user.UserManagment;
import ummisco.gamaSenseIt.springServer.services.compiler.Compiler;
import ummisco.gamaSenseIt.springServer.services.mail.PowerNotifier;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class BaseActivation {

    @Autowired
    UserManagment userManagment;

    @Autowired
    SensorManagement sensorManagment;

    @Autowired
    ISensorRepository sensorRepo;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private Compiler compiler;

    @Autowired
    private PowerNotifier notifier;

    @PostConstruct
    public void activate() throws IOException, MessagingException {
        var sensor = sensorRepo.findAll().iterator().next();
        compiler.getBinary(sensor);

        int count = 0;
        for (var s : sensorRepo.findAll())
            count ++;
        if (count != 0) return;

        var nmarilleau = userManagment.createIfNotExistUser("luis", "bondel", "nmarilleau@gmail.com", "123456", UserPrivilege.ADMIN);
        var space = userManagment.createIfNotExistUser(" ", " ", " ", " ", UserPrivilege.ADMIN);
        var admin = userManagment.createIfNotExistUser("admin", "admin", "admin", "admin", UserPrivilege.ADMIN);
        var user = userManagment.createIfNotExistUser("user", "user", "user", "user", UserPrivilege.USER);
        var pub = userManagment.createIfNotExistUser("public", "public", "public", "public", UserPrivilege.USER);

        var qameleo2 = sensorManagment.addSensorMetadata(
                new SensorMetadata("Qaméléo", "1.0.0", ":", "Des magnifiques capteur de qualité de l'air"),
                List.of(
                        new ParameterMetadata("PM1", "mg/m³", DataFormat.DOUBLE, "nuage"),
                        new ParameterMetadata("PM2.5", "mg/m³", DataFormat.DOUBLE, "nuage"),
                        new ParameterMetadata("PM10", "mg/m³", DataFormat.DOUBLE, "nuage"),
                        new ParameterMetadata("Humidité", "%", DataFormat.DOUBLE, "humi"),
                        new ParameterMetadata("Température", "°C", DataFormat.DOUBLE, "temp"),
                        new ParameterMetadata("Température", "°C",DataFormat.DOUBLE, "temp")
                )
        );

        var qameleo1 = sensorManagment.addSensorMetadata(
                new SensorMetadata("Qaméléo v2", "1.0.1", ":", "Des magnifiques capteur de qualité de l'air v2"),
                List.of(
                        new ParameterMetadata("PM1", "mg/m³", DataFormat.DOUBLE, "nuage"),
                        new ParameterMetadata("PM2.5", "mg/m³", DataFormat.DOUBLE, "nuage"),
                        new ParameterMetadata("PM10", "mg/m³", DataFormat.DOUBLE, "nuage"),
                        new ParameterMetadata("Humidité", "%", DataFormat.DOUBLE, "humi"),
                        new ParameterMetadata("Température", "°C", DataFormat.DOUBLE, "temp")
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
            var s = new Sensor();
            s.setToken(prefixName + i);
            s.setName(prefixDisplayName + i);
            s.setLongitude(locationX + (Math.random() - 0.5) * 2 * spray);
            s.setLatitude(locationY + (Math.random() - 0.5) * 2 * spray);
            s.setSensorMetadata(smd);
            s.setHiddenMessage(hiddenMessage);
            s.setIndications("NoneDisplay");
            s.setDescription("Quisque vulputate, arcu laoreet ornare cursus, lectus dui laoreet magna, et fringilla nulla elit a velit. Praesent auctor scelerisque nunc non eleifend. Phasellus vel turpis nulla. Cras tristique semper mauris nec elementum. Quisque congue maximus metus, vel mattis dolor elementum imperdiet. Quisque tempor, mi ac imperdiet lobortis, tortor lorem bibendum lectus, in venenatis massa lectus non ante. Aenean ultrices arcu nulla, sit amet mollis ante facilisis in. Nunc tristique tempus erat sed fermentum. Suspendisse potenti. Aliquam posuere feugiat eros id dapibus. Mauris dictum est tellus.\n" +
                    "\n" +
                    "Sed malesuada augue sit amet mattis efficitur. Pellentesque dapibus, sem at consectetur luctus, augue mi facilisis neque, eu ultrices dolor libero ut ex. Suspendisse laoreet risus nibh, ut vestibulum justo feugiat at. Pellentesque sodales nunc at porta eleifend. Sed quis diam a massa tempor mollis vitae id ipsum. Donec tempor porttitor erat, nec ullamcorper nisl venenatis a. Vivamus maximus ut nibh in placerat. Etiam consequat justo in magna vestibulum, quis pulvinar ipsum tincidunt. Praesent et dictum velit. Quisque et enim mauris. Nam tincidunt ultrices dignissim. Nulla et tristique ex, ac aliquam eros. Aliquam gravida orci porta, iaculis sem ac, commodo augue. Mauris sed arcu ut sapien placerat imperdiet. Maecenas vel facilisis ligula.\n" +
                    "\n" +
                    "Interdum et malesuada fames ac ante ipsum primis in faucibus. Vivamus in turpis quis ex tempus volutpat. Phasellus dictum, nulla id hendrerit ultricies, quam nulla posuere purus, eget ultricies enim massa nec elit. Curabitur dignissim ullamcorper blandit. Aenean hendrerit rutrum quam, eu pulvinar turpis finibus at. Donec varius in tortor vel condimentum. Ut rhoncus diam est. Donec efficitur ipsum eu est semper semper. Nam non libero fermentum, faucibus urna et, pretium sem. Nunc vitae ante pretium, malesuada leo vel, pharetra quam. Aliquam erat volutpat. Praesent fermentum aliquam semper. Nullam ornare diam sed leo dignissim, maximus suscipit purus fringilla. Donec eu maximus felis, sed euismod justo. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Nam mollis eu lorem sed elementum.\n" +
                    "\n" +
                    "Donec bibendum tristique sapien, nec dapibus leo hendrerit at. Donec accumsan purus eros, eu volutpat est consequat ac. Nam quis massa diam. Nulla varius laoreet molestie. Sed at posuere ligula. Donec sed neque ac leo egestas vehicula aliquet id ex. Phasellus vitae convallis libero. In leo quam, vulputate ac arcu sed, porttitor vehicula purus. Nulla commodo erat egestas tincidunt rutrum. Sed posuere eros vitae facilisis mollis. Sed fermentum leo eget ligula congue euismod. Sed maximus pharetra convallis. Praesent vitae justo dictum, consequat quam sed, mattis sapien. Etiam sollicitudin, lacus convallis consectetur finibus, ipsum purus porttitor purus, id pellentesque leo ante in ex.\n" +
                    "\n" +
                    "Cras euismod imperdiet diam, eget faucibus sapien ultricies at. Donec in vestibulum mauris, a ullamcorper magna. Sed ultricies odio eu eros aliquam, venenatis cursus eros blandit. Sed vulputate finibus nisi in molestie. Curabitur eu gravida odio, vitae laoreet elit. Etiam diam lacus, tristique at consectetur vel, rhoncus at velit. Duis maximus elit sed orci accumsan aliquet.");
            s.setMaintenanceDescription("Sed euismod ipsum id lorem imperdiet convallis. Suspendisse vel ligula odio. Cras mollis at risus mattis sodales. Vestibulum at lectus tempus erat vulputate efficitur. Praesent sapien leo, gravida vitae eros eget, finibus pharetra nisl. Donec commodo nulla vel enim molestie consectetur. Phasellus nec posuere est, in congue nulla. Etiam molestie, sapien vitae varius porta, quam diam efficitur enim, a efficitur neque arcu non nulla. Nam eu luctus neque, eu accumsan tellus. Nullam ut vestibulum nulla.\n" +
                    "\n" +
                    "Nam sollicitudin efficitur lacus, ac placerat ligula faucibus eu. Fusce justo elit, congue ornare enim nec, congue interdum neque. Nullam vulputate purus magna, non interdum urna iaculis quis. Curabitur ultricies mi augue, in hendrerit neque rhoncus eget. Vivamus id massa ligula. Cras nec ligula sed velit viverra maximus quis porttitor lectus. In eget velit id nulla commodo viverra eu et diam. Etiam vulputate fringilla dui, at vehicula quam aliquam eu. Pellentesque euismod leo ligula, ut sagittis dui bibendum sodales. Donec condimentum fermentum aliquam. Phasellus aliquet elementum lectus, eu feugiat ex. Donec id purus non augue imperdiet condimentum.\n" +
                    "\n" +
                    "Ut quis neque lorem. Proin vitae purus sed sapien accumsan semper ac quis arcu. Duis vitae varius enim, at ornare lacus. Duis augue lectus, faucibus at urna sed, viverra blandit nunc. Sed sit amet arcu a ante fringilla convallis. Aliquam semper ex tellus, non viverra velit iaculis ac. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut id libero sit amet lorem auctor molestie.\n" +
                    "\n" +
                    "Aenean libero elit, lacinia quis lobortis a, mollis et leo. Aliquam risus ligula, dignissim lacinia fermentum eleifend, posuere ac libero. Vivamus eget urna iaculis eros sagittis ultrices ut non nulla. Aenean efficitur porta tincidunt. Nulla porttitor scelerisque lacinia. Suspendisse quis faucibus est, eu imperdiet tellus. Quisque risus augue, fringilla ac nibh gravida, posuere faucibus arcu. Fusce nec fermentum dolor. Praesent quam est, vulputate sed est a, ullamcorper suscipit purus. In nec luctus lorem. Donec hendrerit bibendum nisi non tempus. Praesent cursus, diam sed sollicitudin aliquam, ex elit venenatis tortor, vel cursus risus quam a felis. Etiam vehicula nunc dolor, eget ullamcorper dui fringilla eget. Cras fermentum feugiat nisl sit amet tempor. Vivamus nunc quam, tincidunt eu justo et, ultricies semper libero. Morbi vulputate nisl vel lacus suscipit auctor.\n" +
                    "\n" +
                    "In id ex enim. Donec convallis lobortis urna quis pretium. Praesent varius tincidunt diam vel congue. Aliquam accumsan mollis augue, eget suscipit quam elementum at. Suspendisse molestie luctus lacus. Aliquam pulvinar blandit felis nec elementum. Ut ut sapien eget massa maximus lobortis ultrices ut nibh. Praesent at sapien nec sapien imperdiet viverra. Maecenas id consectetur nisl, non laoreet felis. Donec ex neque, imperdiet id massa sed, iaculis semper metus. Fusce blandit dolor vitae erat sodales venenatis. Mauris at sapien commodo, porta justo vel, malesuada quam.");
            //s.setPhoto(new byte[]{-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 32, 0, 0, 0, 32, 8, 2, 0, 0, 0, -4, 24, -19, -93, 0, 0, 0, 68, 73, 68, 65, 84, 72, -57, 99, -108, 95, -11, -97, -127, -106, -128, -119, -127, -58, 96, -44, -126, 81, 11, 70, 45, 24, -75, 96, 68, 88, -64, -56, -16, -65, -108, 22, -26, -78, -4, 20, 99, -6, -57, -6, -117, -13, 41, 11, -115, 28, -2, -121, -3, -43, 104, 36, -113, 90, 48, 106, -63, -88, 5, -93, 22, 12, 33, 11, 0, -38, 33, 9, 106, 10, -5, -45, -107, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126});
            try {
                var photo = resourceLoader.getResource("classpath:qameleo_photo.png").getInputStream().readAllBytes();
                s.setPhoto(photo);
            } catch (IOException err) {
                err.printStackTrace();
            }

            all.add(sensorManagment.addSensorForUser(s, user.getId()));
        }
        return all;
    }
}
