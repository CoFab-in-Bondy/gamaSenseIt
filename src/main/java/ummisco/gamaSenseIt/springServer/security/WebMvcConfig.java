package ummisco.gamaSenseIt.springServer.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    @Value("${gamaSenseIt.cors-url:}")
    private String corsUrl;

    /* replace the basic recourse handler*/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**") /* for all resources */
                .addResourceLocations("classpath:/static/") /* search in static */
                .resourceChain(true) /* or in cache resources*/
                .addResolver(new PathResourceResolver() {
                    /* si la ressource n'existe pas on retourne sur l'index.html */
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource res = super.getResource(resourcePath, location);
                        return res != null ? res : new ClassPathResource("/static/index.html");
                    }
                });
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (corsUrl != null && !corsUrl.isEmpty()) {
            logger.info("CORS enabled for : " + corsUrl);
            registry.addMapping("/**").allowedOrigins(corsUrl).allowedMethods("*");
        } else {
            logger.info("CORS disabled");
            registry.addMapping("/**").allowedOrigins().allowedMethods("*");
        }
    }
}
