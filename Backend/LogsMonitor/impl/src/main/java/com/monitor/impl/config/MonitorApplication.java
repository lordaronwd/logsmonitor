package com.monitor.impl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Starts the application and sets up the basic configuration.
 *
 * @author lazar.agatonovic
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.monitor")
public class MonitorApplication {

    @Autowired
    private Environment environment;

    @Bean
    public ConfigProperties configProperties() {
        final String defaultMonitoringInterval = environment.getProperty("monitoring.interval.seconds");
        final String logFileLocation = environment.getProperty("log.file.location");
        final String maximumLogEntryAge = environment.getProperty("max.log.entry.age.hours");

        return new ConfigProperties(logFileLocation, defaultMonitoringInterval, maximumLogEntryAge);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(MonitorApplication.class, args);
    }

}