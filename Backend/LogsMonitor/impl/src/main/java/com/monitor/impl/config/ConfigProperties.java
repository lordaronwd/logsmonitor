package com.monitor.impl.config;

import com.monitor.core.api.controller.LogStatisticsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Contains the basic application configuration:
 *   - log file location   - file system path to the log file which is being monitored by the application
 *   - monitoring interval - monitoring interval given in seconds. Default value is provided by app configuration,
 *                           but can be overridden during runtime using the API exposed in {@link LogStatisticsController}
 *   - maximum log age     - part of the memory optimisation aspect. In order to avoid I/O overhead, every time when
 *                           request from UI arrives, we are caching the already read entries fora certain amount of time.
 *                           This property represents the life time of such cached entry.
 * @author lazar.agatonovic
 */
public class ConfigProperties {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigProperties.class);
    private static final int DEFAULT_LOG_AGE = 60; // 1 hour

    private final String logFileLocation;
    private Long monitoringInterval;
    private Long maximumLogAgeMilliseconds = convertToMilliseconds(DEFAULT_LOG_AGE);

    public ConfigProperties(final String logFileLocation,
                            final String monitoringInterval,
                            final String maximumLogAge) {
        validateProperties(logFileLocation, monitoringInterval);
        this.logFileLocation = logFileLocation;
        this.monitoringInterval = parseDefaultMonitoringInterval(monitoringInterval);
        setMaximumLogAgeMilliseconds(maximumLogAge);
    }

    public String getLogFileLocation() {
        return logFileLocation;
    }

    public Long getMaximumLogAgeMilliseconds() {
        return maximumLogAgeMilliseconds;
    }

    public Long getMonitoringInterval() {
        return monitoringInterval;
    }

    public void setMonitoringInterval(final Long monitoringInterval) {
        this.monitoringInterval = monitoringInterval;
    }

    private long convertToMilliseconds(final int minutes) {
        return minutes * 60 * 1000L;
    }

    private void validateProperties(final String logFileLocation, final String defaultMonitoringInterval) {
        if (!StringUtils.hasText(logFileLocation)) {
            throw new IllegalStateException("Log file location must be specified");
        }
        if (!StringUtils.hasText(defaultMonitoringInterval)) {
            throw new IllegalStateException("Default monitoring interval must be specified");
        }
    }

    private void setMaximumLogAgeMilliseconds(final String maximumLogAgeMilliseconds) {
        if (StringUtils.hasText(maximumLogAgeMilliseconds)) {
            try {
                Integer minutes = Integer.valueOf(maximumLogAgeMilliseconds);
                if (minutes <= 0) {
                    LOG.warn(String.format("Specified maximum log entry age is not allowed," +
                            " hence default of %d minutes will be used ", DEFAULT_LOG_AGE));
                    minutes = DEFAULT_LOG_AGE;
                }
                this.maximumLogAgeMilliseconds = convertToMilliseconds(minutes);
            } catch (final NumberFormatException e) {
                throw new IllegalStateException("Maximum log entry age must be numeric");
            }
        }
    }

    private Long parseDefaultMonitoringInterval(final String defaultMonitoringInterval) {
        try {
            return Long.valueOf(defaultMonitoringInterval);
        } catch (final NumberFormatException e) {
            throw new IllegalStateException("Default monitoring interval must be numeric");
        }
    }
}
