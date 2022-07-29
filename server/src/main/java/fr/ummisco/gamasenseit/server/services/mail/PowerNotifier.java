package fr.ummisco.gamasenseit.server.services.mail;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import fr.ummisco.gamasenseit.server.data.model.sensor.Sensor;
import fr.ummisco.gamasenseit.server.data.model.user.Access;
import fr.ummisco.gamasenseit.server.data.model.user.AccessPrivilege;
import fr.ummisco.gamasenseit.server.data.model.user.AccessSensor;
import fr.ummisco.gamasenseit.server.data.repositories.ISensorRepository;
import fr.ummisco.gamasenseit.server.data.services.date.DateUtils;
import fr.ummisco.gamasenseit.server.security.SecurityUtils;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Service
public class PowerNotifier {


    private static final Logger logger = LoggerFactory.getLogger(PowerNotifier.class);

    @Autowired
    private ISensorRepository sensorRepository;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private SecurityUtils securityUtils;
    @Value("${gamaSenseIt.power-notifier-delay}")
    private long initialDelayString;

    private static final String mailTemplate = getMailTemplate();

    private static String getMailTemplate() {
        String tpl;
        try {
            tpl = StreamUtils.copyToString( new ClassPathResource("mail/template.html").getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            tpl = "<a href=\"%s\">GamaSenseIt</a><p>%s</p><h1>%s</h1><h2>%s</h2><p>%s</p><a href=\"%s\">Capteur</a> <!-- %s -->";
        }
        // Only keep %s
        return tpl.replaceAll("%([^s]|$)", "%%$1");
    }

    public void sendMailForSensorDown(String email, Sensor sensor) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        if (!email.endsWith("@ird.fr")) return; // support only ird address for the development

        InternetAddress address;
        try {
            address = new InternetAddress("noreply@irday.fr", "GamaSenseIt Notifier");
        } catch (UnsupportedEncodingException e) {
            address = new InternetAddress("noreply@irday.fr");
        }
        helper.setFrom(address);
        helper.setTo(email);
        helper.setSubject("Signal perdu - " + sensor.getName());
        var body = mailTemplate.formatted(
                securityUtils.getFrontUrl(),
                esc(String.format("#%05d", sensor.getId())),
                esc(sensor.getName()),
                esc(sensor.getIndications()),
                sensor.getLastCaptureDate() != null ? "le " + esc(DateUtils.formatPretty(sensor.getLastCaptureDate())) : "toujours",
                securityUtils.getFrontUrl() + "/sensors/" + sensor.getId(),
                securityUtils.getFrontUrl() + "/sensors/" + sensor.getId()
        );
        helper.setText(body, true);
        helper.addInline("ico", new ClassPathResource("mail/ico.png"));
        helper.addInline("ird", new ClassPathResource("mail/ird.png"));
        helper.addInline("ummisco", new ClassPathResource("mail/ummisco.png"));
        helper.addInline("photo", new ByteArrayResource(sensor.getPhoto(), "Photo du capteur"), "image/png");
        logger.info("Sending mail for sensor " + sensor + " to " + email);
        emailSender.send(message);
    }

    private String esc(String text) {
        return StringEscapeUtils.escapeHtml4(text);
    }

    @Scheduled(
        fixedDelayString = "${gamaSenseIt.power-notifier-period}",
        initialDelayString = "${gamaSenseIt.power-notifier-delay}"
    )
    @Transactional
    public void checkPowerOff() {
        logger.info("Checking notifications for power off sensor");
        sensorRepository.findPowerOffNotAlreadyNotified(initialDelayString)
            .parallelStream().forEach(sensor -> {
                sensor.setNotified(true);
                sensorRepository.save(sensor);
                sensor.getAccessSensors().parallelStream()
                    .map(AccessSensor::getAccess)
                    .filter(access -> access.getPrivilege().equals(AccessPrivilege.MAINTENANCE))
                    .map(Access::getUsers)
                    .flatMap(Set::stream).distinct()
                    .forEach(user -> {
                        try {
                            sendMailForSensorDown(user.getMail(), sensor);
                        } catch (MessagingException err) {
                            err.printStackTrace();
                        }
                    });
            });
        logger.info("Check end");
    }

}
