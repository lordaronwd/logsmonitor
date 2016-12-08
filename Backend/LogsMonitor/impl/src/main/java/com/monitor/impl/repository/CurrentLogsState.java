package com.monitor.impl.repository;

/**
 * Convenience class used for representing the log statistics at the specific moment during the run time.
 *
 * @author lazar.agatonovic
 */
public class CurrentLogsState {

    private final long errorsCount;
    private final long warningsCount;
    private final long infosCount;

    public CurrentLogsState(long errorsCount, long warningsCount, long infosCount) {
        this.errorsCount = errorsCount;
        this.warningsCount = warningsCount;
        this.infosCount = infosCount;
    }

    public long getErrorsCount() {
        return errorsCount;
    }

    public long getWarningsCount() {
        return warningsCount;
    }

    public long getInfosCount() {
        return infosCount;
    }
}
