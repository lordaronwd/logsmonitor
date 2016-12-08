package com.monitor.impl.worker;

import com.monitor.core.api.facade.LogStatisticsFacade;
import com.monitor.core.api.model.LogStatisticsResponse;
import com.monitor.core.api.model.MonitoringIntervalModel;
import com.monitor.impl.config.ConfigProperties;
import com.monitor.impl.repository.CurrentLogsState;
import com.monitor.impl.repository.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@link LogStatisticsFacade} implementation class
 *
 * @author lazar.agatonovic
 */
@Service
public class LogStatisticsFacadeImpl implements LogStatisticsFacade {

    private LogsRepository logsRepository;
    private ConfigProperties configProperties;

    @Autowired
    public LogStatisticsFacadeImpl(LogsRepository logsRepository, ConfigProperties configProperties) {
        this.logsRepository = logsRepository;
        this.configProperties = configProperties;
    }

    @Override
    public LogStatisticsResponse getLogStatistics() {
        final long timeWindow = configProperties.getMonitoringInterval();
        final CurrentLogsState currentLogsState = logsRepository.getLogsState(timeWindow);

        return new LogStatisticsResponse(currentLogsState.getErrorsCount(),
                                         currentLogsState.getWarningsCount(),
                                         currentLogsState.getInfosCount());
    }

    @Override
    public Long getMonitoringInterval() {
        return configProperties.getMonitoringInterval();
    }

    @Override
    public void updateMonitoringInterval(MonitoringIntervalModel request) {
        configProperties.setMonitoringInterval(request.getMonitoringInterval());
    }
}
