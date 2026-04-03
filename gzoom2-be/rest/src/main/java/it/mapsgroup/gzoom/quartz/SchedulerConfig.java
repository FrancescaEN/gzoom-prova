package it.mapsgroup.gzoom.quartz;

/**
 * @author Andrea Fossi.
 */
public interface SchedulerConfig {
    /**
     * seconds
     *
     * @return
     */
    default int getReportProbeDelay() {
        return 10;
    }

    /**
     * numneber of retries
     *
     * @return
     */
    default int getReportProbeRetries() {
        return 10;
    }

}
