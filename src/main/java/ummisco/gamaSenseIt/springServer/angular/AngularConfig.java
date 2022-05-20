package ummisco.gamaSenseIt.springServer.angular;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import ummisco.gamaSenseIt.springServer.data.controller.IRoute;
import ummisco.gamaSenseIt.springServer.security.SecurityUtils;

import java.io.IOException;

@Configuration
public class AngularConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(AngularConfig.class);
    private final SecurityUtils securityUtils;
    @Value("${gamaSenseIt.cors-url:}")
    private String corsUrl;

    @Autowired
    public AngularConfig(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    @Bean
    public AngularRedirectExceptionResolver angularRedirectExceptionResolver() {
        return new AngularRedirectExceptionResolver();
    }

    public boolean isEndpoint(String location) {
        for (var endpoint : IRoute.ENDPOINTS)
            if (location.startsWith(endpoint))
                return true;
        return false;
    }

    /* replace the basic recourse handler*/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        registry.addResourceHandler("/**") // for all resources
                .addResourceLocations("classpath:/static/") // search in static
                .resourceChain(true) // or in cache resources
                .addResolver(new PathResourceResolver() {
                    // si la resource n'existe pas on retourne sur l'index.html
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource res = super.getResource(resourcePath, location);
                        if (res == null) {
                            if (isEndpoint("/" + resourcePath))
                                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "/" + resourcePath);
                            else
                                throw new AngularRedirectException();
                        }
                        return res;
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
        logger.info("Base url at " + securityUtils.getRootUrl());
    }
}
