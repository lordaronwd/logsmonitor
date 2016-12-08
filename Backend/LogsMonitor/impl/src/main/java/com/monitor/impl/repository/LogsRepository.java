package com.monitor.impl.repository;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Contains cached log values reducing the I/O overhead every time read request comes.
 * Enables current logs state lookup and also possibility of cleaning up the internal memory,
 * by purging the cached entries once they break the specified age limit.
 *
 * @author lazar.agatonovic
 */
@Component
public class LogsRepository {

    public enum LogType {
        ERROR,
        WARNING,
        INFO
    }

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Map<LogType, LinkedList<Long>> logsMap = new EnumMap<>(LogType.class);
    static {
        logsMap.put(LogType.ERROR, new LinkedList<>());
        logsMap.put(LogType.WARNING, new LinkedList<>());
        logsMap.put(LogType.INFO, new LinkedList<>());
    }

    private LogsRepository() {
    }

    /**
     * Retrieves the log statistics for specified amount of seconds in the past, starting from the current time.
     * @param timeWindowSeconds Monitoring interval used for reporting
     * @return current logs state holding the counts of log entries per type.
     */
    public CurrentLogsState getLogsState(final long timeWindowSeconds) {
        final long lowerMonitoringBound = System.currentTimeMillis() - (timeWindowSeconds * 1000);
        final long errorsCount = getMonitoredLogEntriesCount(logsMap.get(LogType.ERROR), lowerMonitoringBound);
        final long warningsCount = getMonitoredLogEntriesCount(logsMap.get(LogType.WARNING), lowerMonitoringBound);
        final long infosCount = getMonitoredLogEntriesCount(logsMap.get(LogType.INFO), lowerMonitoringBound);

        return new CurrentLogsState(errorsCount, warningsCount, infosCount);
    }

    /**
     * Caches long entry based on its type.
     * @param logType type of the log entry to be cached
     * @param logTimestamp log entry timestamp
     */
    public void addLogEntry(final LogType logType, final Long logTimestamp) {
        lock.writeLock().lock();
        try {
            logsMap.get(logType).add(logTimestamp);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Purges all cached log entries older than specified value of milliseconds ago from now.
     * @param timeWindowMilliseconds Allowed time int he past starting from current time.
     */
    public void purge(final long timeWindowMilliseconds) {
        final long currentTime = System.currentTimeMillis();
        final long minimalAge = currentTime - timeWindowMilliseconds;
        final LinkedList<Long> refinedErrors = refine(logsMap.get(LogType.ERROR), minimalAge);
        final LinkedList<Long> refinedWarnings = refine(logsMap.get(LogType.WARNING), minimalAge);
        final LinkedList<Long> refinedInfos = refine(logsMap.get(LogType.INFO), minimalAge);

        lock.writeLock().lock();
        try {
            logsMap.put(LogType.ERROR, refinedErrors);
            logsMap.put(LogType.WARNING, refinedWarnings);
            logsMap.put(LogType.INFO, refinedInfos);

        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Refine method is optimised with two assumptions:
     * 1) It might happen that log file is modified manually (time traveling problem), so we are always iterating through
     *    whole entries list. Preferably time traveling is not allowed, so we can stop the iteration first time we encounter
     *    log entry with minimal allowed age.
     * 2) Memory consumption when copying the log type list is not huge, assuming the reasonably specified log entry life time
     *
     * @param list List of log entry timestamps for particular log type
     * @param minimalAge The minimal age required for a log entry to remain cached
     * @return refined list containing only entries written withing previous @{link minimalAge} milliseconds
     */
    private LinkedList<Long> refine(final LinkedList<Long> list, final long minimalAge) {
        final LinkedList<Long> bufferList = new LinkedList<>(list);
        final Iterator<Long> iterator = bufferList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() < minimalAge) {
                iterator.remove();
            }
        }
        return bufferList;
    }

    /**
     * Gets the count of entries younger than specified time bound.
     * @param list List of log entry timestamps for particular log type
     * @param lowerMonitoringBound The minimal age required for a log entry to become the part of a result set.
     * @return entries count
     */
    private long getMonitoredLogEntriesCount(final LinkedList<Long> list, final long lowerMonitoringBound) {
        final ListIterator<Long> iterator = list.listIterator(list.size());
        long counter = 0L;
        while (iterator.hasPrevious() && iterator.previous() > lowerMonitoringBound) {
            counter++;
        }
        return counter;
    }
}
