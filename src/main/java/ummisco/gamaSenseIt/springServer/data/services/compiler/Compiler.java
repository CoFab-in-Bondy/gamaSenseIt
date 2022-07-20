package ummisco.gamaSenseIt.springServer.data.services.compiler;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.cli.ArduinoCLI;
import ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.cli.ArduinoException;
import ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.cli.ArduinoManager;
import ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.cli.Properties;
import ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.setup.FileHelper;
import ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.setup.Github;
import ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.setup.Installer;
import ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.struct.Arduino;
import ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.struct.Systems;
import ummisco.gamaSenseIt.springServer.security.SecurityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class Compiler {

    private static final Logger logger = LoggerFactory.getLogger(Compiler.class);

    private SecurityUtils securityUtils;
    private ArduinoManager arduinoManager;
    private String project;

    @Autowired
    public Compiler(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
        FileHelper.disableSSL();
        var installer = new Installer();
        try {
            installer.getResolver().setVersion("0.24.0");
            String arduinoPath = installer.install();
            arduinoManager = new ArduinoManager(new ArduinoCLI(arduinoPath));

            var dir = Systems.userHome();
            System.out.println(dir);
            System.out.println("START GITHUB");
            var github = new Github(
                    "CoFab-in-Bondy",
                    "QameleO",
                    "4930bd62e696fc0053d18eea97e4fd6fe2f12f1e",
                    dir + "/.github-gamasenseit-QameleO"
            );
            project = github.download();
        } catch (IOException | ArduinoException e) {
            e.printStackTrace();
        }

    }

    @Value("${gamaSenseIt.make}")
    private String make;

    static void copyRecursive(String dest) throws IOException {
        String dir = "/compiler/";
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + dir + "**");
        for (Resource resource : resources) {
            if (resource.exists() & resource.isReadable() && resource.contentLength() > 0) {
                URL url = resource.getURL();
                String urlString = url.toExternalForm();
                String targetName = urlString.substring(urlString.indexOf(dir) + dir.length());
                File destination = new File(dest, targetName);
                Files.copy(url.openStream(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    public byte[] getBinary(Sensor sensor, Properties properties) throws IOException, ArduinoException {
        var tempDir = Files.createTempDirectory("compiler-");
        try {
            logger.info("Working in " + tempDir);
            copyRecursive(tempDir.toString());

            properties = properties.copy().addText("SENSOR_NAME", sensor.getToken());
            System.out.println("TEST : " + properties.asString());

            var executable = arduinoManager.compile(
                    Arduino.ARDUINO_AVG_MEGA,
                    project + "/software/Qameleo",
                    tempDir.toString(),
                    project + "/software/libraries",
                    properties
            );
            var bytes = Files.readAllBytes(Path.of(executable));
            logger.info("Binary (" + bytes.length + "o) is ready at " + executable);
            return bytes;
        } finally {
            FileUtils.deleteDirectory(tempDir.toFile());
            logger.info("Directory deleted");
        }
    }

    /*
    public void compile() throws IOException, ArduinoException {
        Properties props = Properties.build()
                .addString("SENSOR_NAME", getSensorName(), 256)
                .addString("CALL_TARGET", getCallTarget(), 256)
                .addString("SMS_TARGET", getSmsTarget(), 256)
                .addLong("NETWORK_REGISTER_TIMEOUT", getNetworkRegisterTimeout())
                .addString("FILE_NAME_LOG", getFileNameLog(), 256)
                .addString("FILE_NAME_BUFFER", getFileNameBuffer(), 256)
                .addString("FILE_NAME_SAVE", getFileNameSave(), 256)
                .addString("GSM_PIN_CODE", getGsmPinCode(), 256)
                .addString("GSM_APN", getGsmApn(), 256)
                .addString("GSM_USER", getGsmUser(), 256)
                .addString("GSM_PASS", getGsmPass(), 256)
                .addString("GSM_MQTT_BROKER", getGsmMqttBorker(), 256)
                .addInt("MEASURE_PERIOD", getMeasurePeriod())
                .addInt("SEND_PERIOD", getSendPeriod())
                .addInt("RECEIVE_UNIXTIME_PERIOD", getReceiveUnixtimePeriod())
                .addInt("REBOOT_DELAY", getRebootDelay());
        if (isDhtSensor()) props.define("DHT_SENSOR");
        if (isShtSensor()) props.define("SHT_SENSOR");
        if (isNextPmSensor()) props.define("NextPM_SENSOR");
        if (isDhtSensor()) props.define("PMS7003_SENSOR");
        this.arduino.compile(
                Arduino.ARDUINO_AVG_MEGA,
                this.source + "/software/QameleO",
                this.source + "/build",
                this.source + "/software/libraries",
                props
        );
    }
    */
}
