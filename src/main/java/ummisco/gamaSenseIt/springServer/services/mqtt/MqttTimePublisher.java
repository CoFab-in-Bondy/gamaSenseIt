package ummisco.gamaSenseIt.springServer.services.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MqttTimePublisher {

	private static final Logger logger = LoggerFactory.getLogger(MqttTimePublisher.class);


	@Autowired
	private MqttOut.TimeNotifier notifier;


	@Scheduled(fixedDelayString = "${gamaSenseIt.broker-time-period}")
	public void scheduleFixedDelayTask() {
		publishCurrentDate();
		logger.debug("Fixed delay task - " + System.currentTimeMillis() / 1000);
	}

	public void publishCurrentDate() {
		notifier.sendToMqtt(Long.toString(System.currentTimeMillis() / 1000));	
	}

}
