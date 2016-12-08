package com.monitor.impl.monitor;

import com.monitor.impl.config.ConfigProperties;
import com.monitor.impl.repository.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Scheduled helper class which allows cache purging.
 * Runs in a separate background thread and takes care that all out-aged log entries are successfully cleaned up.
 *
 * @author lazar.agatonovic
 */
@Component
public class AgedLogsCollector {

    private final LogsRepository logsRepository;
    private final long maximumLogAgeMilliseconds;

    @Autowired
    public AgedLogsCollector(ConfigProperties configProperties, LogsRepository logsRepository) {
        this.logsRepository = logsRepository;
        this.maximumLogAgeMilliseconds = configProperties.getMaximumLogAgeMilliseconds();
        init();
    }

    private void init() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                logsRepository.purge(maximumLogAgeMilliseconds);
            }
        }, 0L, maximumLogAgeMilliseconds);
    }
}
