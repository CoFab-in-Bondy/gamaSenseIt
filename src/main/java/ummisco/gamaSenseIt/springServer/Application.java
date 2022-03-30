package ummisco.gamaSenseIt.springServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import ummisco.gamaSenseIt.springServer.data.services.sensor.ISensorManagement;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("Starting server ...");

        // GeometryFactory gf=new GeometryFactory();
        // ApplicationContext context = new
        // ClassPathXmlApplicationContext("classpath:application-config.xml");

        ApplicationContext context = SpringApplication.run(Application.class, args);

        // Question 1
        ISensorManagement saver = (ISensorManagement) context.getBean("SensorManagment");
        saver.saveDefaultSensorInit();
        // System.out.println(formater.formatMessage(initialMessage));

    }


}