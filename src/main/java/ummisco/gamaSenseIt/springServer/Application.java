package ummisco.gamaSenseIt.springServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("Starting server ...");
        ApplicationContext context = SpringApplication.run(Application.class, args);
    }
}