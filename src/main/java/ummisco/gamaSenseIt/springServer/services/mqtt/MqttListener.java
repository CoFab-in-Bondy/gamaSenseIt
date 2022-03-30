package ummisco.gamaSenseIt.springServer.services.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import ummisco.gamaSenseIt.springServer.data.services.sensor.SensorManagment;

import java.util.Calendar;
import java.util.Date;

public class MqttListener implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MqttListener.class);

    @Autowired
    private SensorManagment sensorManagment;

    private String filename;

    public MqttListener(String filename) {
        this.filename = filename;
    }

    @Override
    public void handleMessage(Message<?> arg0) throws MessagingException {
        Date now = Calendar.getInstance().getTime();
        logger.info("Message MQTT reçu : " + arg0.getPayload());
        sensorManagment.saveData(arg0.getPayload().toString(), now);
    }

}
