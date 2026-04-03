package it.mapsgroup.gzoom.quartz.oldScheduler;

import org.quartz.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Executes a certain check and saves its result.
 *
 * @author Fabio G. Strozzi
 * @author Andrea Fossi
 */
//@SuppressWarnings("SpringJavaAutowiredMembersInspection")
//@DisallowConcurrentExecution
public class ProbeJob implements Job {
    private static final Logger LOG = getLogger(ProbeJob.class);

    static final String PROBE_TYPE_KEY = "probe-type";
    static final String PROBE_TYPE_SND_KEY = "probe-snd-type";
    static final String PROBE_RETRY_KEY = "probe-retry";
    static final String PROBE_ID_KEY = "probe-id";
    static final String PROBE_PARAMS_KEY = "probe-params";

    enum ProbeType {REPORT}

    @Autowired
    private ProbeService probeService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey key = context.getJobDetail().getKey();
        LOG.debug("Executing job [key={}]", key);

        StopWatch sw = new StopWatch();
        sw.start();

        JobDataMap map = context.getMergedJobDataMap();

        String id = map.getString(PROBE_ID_KEY);

        ProbeType type = ProbeType.valueOf(map.getString(PROBE_TYPE_KEY));

        try {
            switch (type) {
                case REPORT:
                    probeService.probeReport(id, map.getString(PROBE_TYPE_SND_KEY), map.getString(PROBE_PARAMS_KEY));
                    break;
                default:
                    throw new IllegalStateException("Unexpected probe type: " + type);
            }
        } catch (Exception e) {
            LOG.error("Probing job failed [type={}, id={}]", type, id, e);
            throw new JobExecutionException(e);
        } finally {
            sw.stop();
        }

        LOG.info("Probing job of type {} with id {} took {}s", type, id, sw.getTotalTimeSeconds());
    }


    public void setProbeService(ProbeService probeService) {
        this.probeService = probeService;
    }
}
