package com.monitor.core.api.model;

/**
 * Model class used for request/response serialization.
 * Wraps the monitoring interval value.
 *
 * @author lazar.agatonovic
 */
public class MonitoringIntervalModel {

    private Long monitoringInterval;

    public MonitoringIntervalModel() {
    }

    public MonitoringIntervalModel(Long monitoringInterval) {
        this.monitoringInterval = monitoringInterval;
    }

    public Long getMonitoringInterval() {
        return monitoringInterval;
    }

    public void setMonitoringInterval(final Long monitoringInterval) {
        this.monitoringInterval = monitoringInterval;
    }
}
