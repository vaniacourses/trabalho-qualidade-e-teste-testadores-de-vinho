package com.example.waiterapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Serve a SPA (index.html) na rota "/".
 * Documentação da API: springdoc-openapi (/v3/api-docs, /swagger-ui/index.html).
 */
@Configuration
public class SpaForwardingConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
