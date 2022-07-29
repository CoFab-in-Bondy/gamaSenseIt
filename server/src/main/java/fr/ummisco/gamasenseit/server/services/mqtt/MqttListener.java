package fr.ummisco.gamasenseit.server.services.mqtt;

import fr.ummisco.gamasenseit.server.data.services.sensor.SensorManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class MqttListener implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MqttListener.class);

    @Autowired
    private SensorManagement sensorManagment;

    private String filename;

    public MqttListener(String filename) {
        this.filename = filename;
    }

    @Override
    public void handleMessage(Message<?> arg0) throws MessagingException {
        logger.info("Message MQTT reçu : " + arg0.getPayload());
        sensorManagment.saveData(arg0.getPayload().toString());
    }

}
