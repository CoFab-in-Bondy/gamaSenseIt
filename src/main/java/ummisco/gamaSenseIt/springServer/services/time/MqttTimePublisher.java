package ummisco.gamaSenseIt.springServer.services.time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ummisco.gamaSenseIt.springServer.services.time.MqttTimeConfig.TimeNotifier;

@Service
public class MqttTimePublisher implements IMqttTimePublisher{

	@Autowired
	TimeNotifier notifier;
	
	
	String topic;
	
	
	public void publishCurrentDate() {
		notifier.sendToMqtt(Long.toString(System.currentTimeMillis() / 1000));	
	}

}
