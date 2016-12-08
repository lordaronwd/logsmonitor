package com.monitor.impl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        if (!registry.hasMappingForPattern("/static/**")) {
            registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        }
        if (!registry.hasMappingForPattern("logsmonitorapp/**")) {
            registry.addResourceHandler("logsmonitorapp/**").addResourceLocations("classpath:/");
        }
    }

}