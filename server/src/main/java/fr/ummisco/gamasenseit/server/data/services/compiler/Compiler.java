package fr.ummisco.gamasenseit.server.data.services.compiler;

import fr.ummisco.gamasenseit.arduino.exception.ArduinoException;
import fr.ummisco.gamasenseit.server.data.model.sensor.Sensor;
import fr.ummisco.gamasenseit.arduino.cli.ArduinoManager;
import fr.ummisco.gamasenseit.arduino.cli.Properties;
import fr.ummisco.gamasenseit.arduino.setup.Installer;
import fr.ummisco.gamasenseit.server.security.SecurityUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import fr.ummisco.gamasenseit.arduino.cli.ArduinoCLI;
import fr.ummisco.gamasenseit.arduino.setup.Github;
import fr.ummisco.gamasenseit.arduino.struct.Arduino;
import fr.ummisco.gamasenseit.arduino.struct.Systems;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class Compiler {

    private static final Logger logger = LoggerFactory.getLogger(Compiler.class);

    @Value("${gamaSenseIt.broker.url}")
    private String brokerUrl;

    @Value("${gamaSenseIt.broker.username}")
    private String brokerUsername;

    @Value("${gamaSenseIt.broker.password}")
    private String brokerPassword;

    private String githubOwner;
    private String githubProject;
    private String githubCommit;
    private String arduinoVersion;

    private SecurityUtils securityUtils;
    private ArduinoManager arduinoManager;
    private String project;

    private String getBrokenAuthority() {
        try {
            return new URI(brokerUrl).getAuthority();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    public Compiler(
            SecurityUtils securityUtils,
            @Value("${gamaSenseIt.github.owner}") String githubOwner,
            @Value("${gamaSenseIt.github.project}") String githubProject,
            @Value("${gamaSenseIt.github.commit}") String githubCommit,
            @Value("${gamaSenseIt.arduino.version}") String arduinoVersion
            ) {
        this.arduinoVersion = arduinoVersion;
        this.githubOwner = githubOwner;
        this.githubProject = githubProject;
        this.githubCommit = githubCommit;
        this.securityUtils = securityUtils;
        var installer = new Installer();
        try {
            installer.getResolver().setVersion(arduinoVersion);
            String arduinoPath = installer.install();
            ArduinoCLI arduinoCLI = new ArduinoCLI(arduinoPath);
            arduinoCLI.libInstall("SD");
            arduinoManager = new ArduinoManager(arduinoCLI);

            var dir = Systems.userHome();
            System.out.println(dir);
            System.out.println("START GITHUB");
            System.out.println(githubOwner + " - " + githubProject + " - " + githubCommit);
            var github = new Github(githubOwner, githubProject, githubCommit, dir + "/.github-gamasenseit-QameleO");
            project = github.download();
        } catch (IOException | ArduinoException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBinary(Sensor sensor, Properties properties) throws IOException, ArduinoException {
        var tempDir = Files.createTempDirectory("compiler-");
        try {
            logger.info("Working in " + tempDir);
            properties = properties.copy()
                    .addText("GSM_USER", brokerUsername)
                    .addText("GSM_PASS", brokerPassword)
                    .addText("GSM_MQTT_BROKER", getBrokenAuthority())
                    .addText("SENSOR_NAME", sensor.getToken());
            logger.info("Compile with " + properties.toString());

            var executable = arduinoManager.compile(
                    Arduino.ARDUINO_AVG_MEGA,
                    project + "/software/QameleO",
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
