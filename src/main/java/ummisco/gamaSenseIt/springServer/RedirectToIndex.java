package ummisco.gamaSenseIt.springServer;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class RedirectToIndex implements WebMvcConfigurer {

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
}
