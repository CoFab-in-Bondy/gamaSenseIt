package ummisco.gamaSenseIt.springServer.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

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
                        Resource requestedResource = location.createRelative(resourcePath);
                        return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
                                : new ClassPathResource("/static/index.html");
                    }
                });
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(
                "http://localhost:80", // HTTP Server
                "https://localhost:443", // HTTPS Server
                "http://localhost:8080", // HTTP Server
                "https://localhost:8443", // HTTPS Server
                "http://localhost:4200"  // Node Server
        );
    }
}
