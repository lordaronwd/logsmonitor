package com.monitor.core.api.facade;

import com.monitor.core.api.model.LogStatisticsResponse;
import com.monitor.core.api.model.MonitoringIntervalModel;

/**
 * This class defines the basic application functionality.
 *
 * @author lazar.agatonovic
 */
public interface LogStatisticsFacade {

    /**
     * Retrieves the current log statistics holding the count of
     * errors, warnings and info log entries at specific moment.
     */
    LogStatisticsResponse getLogStatistics();

    /**
     * Retrieves the current monitoring interval value given in seconds.
     */
    Long getMonitoringInterval();

    /**
     * Updates the current value of monitoring interval with value provided in the request
     */
    void updateMonitoringInterval(final MonitoringIntervalModel request);
}
