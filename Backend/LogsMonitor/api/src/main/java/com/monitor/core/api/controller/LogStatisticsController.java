package com.monitor.core.api.controller;

import com.monitor.core.api.facade.LogStatisticsFacade;
import com.monitor.core.api.model.LogStatisticsResponse;
import com.monitor.core.api.model.MonitoringIntervalModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Web controller class which acts as a bridge between the user interface and the application logic.
 * Entry point for all HTTP calls directed to the monitor logs application.
 *
 * @author lazar.agatonovic
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/logs-monitor")
public class LogStatisticsController {

    @Autowired
    private LogStatisticsFacade logStatisticsFacade;

    @RequestMapping(path = "/statistics",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public LogStatisticsResponse getLogStatistics() {
        return logStatisticsFacade.getLogStatistics();
    }

    @RequestMapping(path = "/monitoring-interval",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public MonitoringIntervalModel getMonitoringInterval() {
        final Long monitoringInterval = logStatisticsFacade.getMonitoringInterval();
        return new MonitoringIntervalModel(monitoringInterval);
    }

    @RequestMapping(path = "/monitoring-interval",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateMonitoringInterval(@RequestBody MonitoringIntervalModel request) {
        logStatisticsFacade.updateMonitoringInterval(request);
    }
}
