package com.monitor.core.api.model;

/**
 * Model class used for request/response serialization.
 * Wraps the current counts of specific log entry types.
 *
 * @author lazar.agatonovic
 */
public class LogStatisticsResponse {

    private Long errors;
    private Long warnings;
    private Long infos;

    public LogStatisticsResponse(final Long errors, final Long warnings, final Long infos) {
        this.errors = errors;
        this.warnings = warnings;
        this.infos = infos;
    }

    public Long getErrors() {
        return errors;
    }

    public Long getWarnings() {
        return warnings;
    }

    public Long getInfos() {
        return infos;
    }

    public void setErrors(Long errors) {
        this.errors = errors;
    }

    public void setWarnings(Long warnings) {
        this.warnings = warnings;
    }

    public void setInfos(Long infos) {
        this.infos = infos;
    }
}
