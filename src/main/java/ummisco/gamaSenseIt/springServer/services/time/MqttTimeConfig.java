package ummisco.gamaSenseIt.springServer.services.time;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ummisco.gamaSenseIt.springServer.security.jwt.JwtRequestFilter;

@Configuration
@EnableScheduling
public class MqttTimeConfig {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Value("${gamaSenseIt.broker-time-topic}")
    private String brokerTimeTopic;
    @Value("${gamaSenseIt.broker-time-period}")
    private int brokerTimeUpdatePeriode;
    
    @Autowired
    MqttPahoClientFactory mqttClientFactory;
    
    @Autowired
    MqttTimePublisher notifier;
    
    @Scheduled(fixedDelayString = "${gamaSenseIt.broker-time-period}")
    public void scheduleFixedDelayTask() {
    	notifier.publishCurrentDate();
        logger.debug("Fixed delay task - " + System.currentTimeMillis() / 1000);
    }
    
    
   @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler handlerOut() {
    	MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("Client_" + System.currentTimeMillis(), mqttClientFactory);
        messageHandler.setDefaultTopic(brokerTimeTopic);
        messageHandler.setAsync(true);
        return messageHandler;
    }  
    
    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface TimeNotifier {

        void sendToMqtt(String data);

    }
}
