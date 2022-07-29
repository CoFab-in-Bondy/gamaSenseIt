package fr.ummisco.gamasenseit.server.services.mqtt;


import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class MqttConfig {
    @Value("${gamaSenseIt.broker.timeout}")
    private int brokerTimeout;
    @Value("${gamaSenseIt.broker.url}")
    private String brokerURL;
    @Value("${gamaSenseIt.broker.username}")
    private String brokerLoggin;
    @Value("${gamaSenseIt.broker.password}")
    private String brokerPass;
    @Value("${gamaSenseIt.broker.topic}")
    private String brokerTopic;

    @Autowired
    private MqttTimePublisher notifier;

    @Bean
    public MessageProducerSupport mqttInbound() {

        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                MqttAsyncClient.generateClientId(), mqttClientFactory(), brokerTopic);
        adapter.setCompletionTimeout(brokerTimeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        return adapter;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{brokerURL});
        options.setUserName(brokerLoggin);
        options.setPassword(brokerPass.toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public IntegrationFlow mqttInFlow() {
        return IntegrationFlows.from(mqttInbound()).transform(p -> p).handle(new MqttListener("sensedData.csv")).get();
    }
}
