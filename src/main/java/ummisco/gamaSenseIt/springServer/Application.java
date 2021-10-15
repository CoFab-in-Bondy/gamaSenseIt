package ummisco.gamaSenseIt.springServer;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ummisco.gamaSenseIt.springServer.data.services.ISensorManagment;

import java.util.Calendar;
import java.util.Date;

@SpringBootApplication(scanBasePackages = {"ummisco.gamaSenseIt.springServer.security",
        "ummisco.gamaSenseIt.springServer.data.model", "ummisco.gamaSenseIt.springServer.data.repositories",
        "ummisco.gamaSenseIt.springServer.data.services", "ummisco.gamaSenseIt.springServer.data.controller",
        "ummisco.gamaSenseIt.springServer.qameleo"})
@EnableAutoConfiguration
//@EnableWebSecurity

@EnableTransactionManagement
@ImportResource(value = "classpath:application-config.xml")
public class Application {

    @Autowired
    ISensorManagment sensorManager;
    @Value("${gamaSenseIt.broker-url}")
    private String brokerURL;
    @Value("${gamaSenseIt.broker-username}")
    private String brokerLoggin;
    @Value("${gamaSenseIt.broker-password}")
    private String brokerPass;
    @Value("${gamaSenseIt.broker-topic}")
    private String brokerTopic;

    public static void main(String[] args) {
        System.out.println("start server");

        // GeometryFactory gf=new GeometryFactory();
        // ApplicationContext context = new
        // ClassPathXmlApplicationContext("classpath:application-config.xml");

        ApplicationContext context = SpringApplication.run(Application.class, args);
        // Question 1
        ISensorManagment saver = (ISensorManagment) context.getBean("SensorManagment");
        saver.saveDefaultSensorInit();
        // System.out.println(formater.formatMessage(initialMessage));

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
        return IntegrationFlows.from(mqttInbound()).transform(p -> p).handle(new MyMessageHandler("sensedData.csv")).get();
    }

//  private LoggingHandler logger() {
//    LoggingHandler loggingHandler = new LoggingHandler("INFO");
//    loggingHandler.setLoggerName("SENS_AIR_1");
//    return loggingHandler;
//  }

    @Bean
    public MessageProducerSupport mqttInbound() {

        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                MqttAsyncClient.generateClientId(), mqttClientFactory(), brokerTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        return adapter;
    }

    public class MyMessageHandler implements org.springframework.messaging.MessageHandler {
        String fileName;

        public MyMessageHandler(String flName) {
            this.fileName = flName;
        }

        @Override
        public void handleMessage(Message<?> arg0) throws MessagingException {

            Date dte = Calendar.getInstance().getTime();
//      SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
            System.out.println("Message recu : " + arg0.getPayload());
            sensorManager.saveData(arg0.getPayload().toString(), dte);

        }
    }

}