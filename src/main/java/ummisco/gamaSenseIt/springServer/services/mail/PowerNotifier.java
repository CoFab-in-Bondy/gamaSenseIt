package ummisco.gamaSenseIt.springServer.services.mail;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.data.repositories.ISensorRepository;
import ummisco.gamaSenseIt.springServer.security.jwt.JwtRequestFilter;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@Service
public class PowerNotifier {


    private static final Logger logger = LoggerFactory.getLogger(PowerNotifier.class);

    private final SimpleDateFormat format = new SimpleDateFormat("EEEEE d MMMMM yyyy à H:mm:ss.SSS", new Locale("fr", "FR"));
    @Autowired
    private ISensorRepository sensorRepository;
    @Autowired
    private JavaMailSender emailSender;
    @Value("${gamaSenseIt.power-notifier-delay}")
    private long initialDelayString;

    public void sendSensorDown(String email, Sensor sensor) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(new InternetAddress("noreply@irday.fr"));
        helper.setTo(email);
        helper.setSubject(esc(sensor.getDisplayName()) + " - Pas de singal");
        helper.setText("""
                    <html>
                        <body>
                            <img src='cid:test' style='width: 240px; margin-left: auto; margin-right: auto; '>
                            <h1 style='margin: 0px; padding: 0px; margin-bottom: 5px'> Signal perdu - %s </h1>
                            <h2 style='margin: 0px; padding: 0px; margin-bottom: 5px'> %s </h2>
                            Le capteur a céssé d'êmettre des données depuis %s !
                            <a href='%s' target='_blank' style='display: block; text-decoration: none; background-color: green; color: white; border-radius: 5px; width: 180px; text-align: center; padding: 5px; margin-top: 10px; margin-bottom: 10px'>Voir le capteur</a>
                            Cordialement, L'équipe de GamaSenseIt
                        </body>
                    </html>
                """.formatted(
                esc(sensor.getDisplayName()),
                esc(sensor.getSubDisplayName()),
                sensor.getLastCaptureDate() != null ? "le " + esc(format.format(sensor.getLastCaptureDate())) : "toujours",
                esc("https://localhost:8443/sensors/" + sensor.getId())
        ), true);
        helper.addInline("test", new ClassPathResource("mail/qameleo.png"));
        // helper.addAttachment("important", new ClassPathResource("mail/background.jpg"));
        logger.info("Sending mails for sensor " + sensor);
        //emailSender.send(message);
    }

    private String esc(String text) {
        return StringEscapeUtils.escapeHtml4(text);
    }

    @Scheduled(
            fixedDelayString = "${gamaSenseIt.power-notifier-period}",
            initialDelayString = "${gamaSenseIt.power-notifier-delay}"
    )
    public void checkPowerOff() {
        List<Sensor> off = sensorRepository.findPowerOffNotAlreadyNotified(initialDelayString);
        logger.info("Checking notifications for power off sensor : " + off.size() + " found");
        for (var s : off) {
            try {
                sendSensorDown("***REMOVED***.***REMOVED***@gmail.fr", s);
            } catch (MessagingException err) {
                err.printStackTrace();
            }
        }
    }

}
