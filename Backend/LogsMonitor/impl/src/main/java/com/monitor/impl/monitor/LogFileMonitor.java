package com.monitor.impl.monitor;

import com.monitor.impl.config.ConfigProperties;
import com.monitor.impl.repository.LogsRepository;
import com.monitor.impl.repository.LogsRepository.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * This class represents the real time file monitor.
 * At first run goes through the file line by line, parsing each one and storing the valuable info.
 * Once it reaches the end of the file, goes to the sleep mode until new log entry is added to the file.
 *
 * @author lazar.agatonovic
 */
@Component
public class LogFileMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(LogFileMonitor.class);
    private static final int RELEVANT_TOKENS_COUNT = 3;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    private final ConfigProperties configProperties;
    private final LogsRepository logsRepository;

    @Autowired
    private LogFileMonitor(ConfigProperties configProperties, LogsRepository logsRepository) {
        this.configProperties = configProperties;
        this.logsRepository = logsRepository;
        init();
    }

    private void init() {
        new Thread(() -> {
            final Path filePath = Paths.get(configProperties.getLogFileLocation());

            try (final BufferedReader br = Files.newBufferedReader(filePath)) {
                while (true) {
                    String line = br.readLine();
                    if (StringUtils.hasText(line)) {
                        updateLogsState(line);
                    } else {
                        Thread.sleep(1000L);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateLogsState(final String logEntryLine) {
        Optional<TypeTimePair> optionalPair = parseLogEntryLine(logEntryLine);

        if (optionalPair.isPresent()) {
            TypeTimePair typeTimePair = optionalPair.get();
            logsRepository.addLogEntry(typeTimePair.getType(), typeTimePair.getTime());
        }
    }

    private Optional<TypeTimePair> parseLogEntryLine(final String line) {
        final String[] tokens = line.split(" ");

        if (tokens.length < RELEVANT_TOKENS_COUNT) { // malformed line
            return Optional.empty();
        }

        final String date = tokens[0].concat(" ").concat(tokens[1]);
        final String type = tokens[2];

        try {
            final LogType logType = getLogType(type);
            final Date logEntryDate = SDF.parse(date);
            return Optional.of(new TypeTimePair(logType, logEntryDate.getTime()));
        } catch (final ParseException | IllegalStateException e) {
            LOG.info("Skipping the malformed log entry: " + line);
        }

        return Optional.empty();
    }

    private LogType getLogType(final String logType) {
        try {
            return LogType.valueOf(logType);
        } catch (final IllegalArgumentException e) {
            throw new IllegalStateException("Malformed log type");
        }
    }

    private class TypeTimePair {
        private LogType type;
        private long time;

        TypeTimePair(final LogType type, final long time) {
            this.type = type;
            this.time = time;
        }

        public LogType getType() {
            return type;
        }

        public long getTime() {
            return time;
        }
    }

}
