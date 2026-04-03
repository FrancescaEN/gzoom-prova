package it.mapsgroup.gzoom.quartz.oldScheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.quartz.SchedulerConfig;
import it.mapsgroup.gzoom.report.service.ReportCallbackType;
import it.mapsgroup.gzoom.rest.InternalServerException;
import it.mapsgroup.gzoom.rest.ValidationException;
import org.quartz.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;
import static org.slf4j.LoggerFactory.getLogger;


/**
 * Continuously probes the Report status in order to update the local database.
 * <p>
 * Database ddl at  @see <a href="https://github.com/elventear/quartz-scheduler/tree/master/distribution/src/main/assembly/root/docs/dbTables">dbTables</a>
 *
 * @author Fabio G. Strozzi
 * @author Andrea Fossi
 */
@Service
public class ProbeSchedulerService {
    private static final Logger LOG = getLogger(ProbeSchedulerService.class);
    private final Scheduler scheduler;
    private final SchedulerConfig config;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProbeSchedulerService(Scheduler scheduler, SchedulerConfig config, ObjectMapper objectMapper) {
        this.scheduler = scheduler;
        this.config = config;
        this.objectMapper = objectMapper;
    }

    /**
     * Schedules the continuous polling of the Brain in order to retrieve and update the report status.
     *
     * @param id Execution identifier
     */
    public void scheduleReportProbe(String id, ReportCallbackType callback, Map<String, Object> params) {
        try {
            createSchedule(id, ProbeJob.ProbeType.REPORT, callback.name(), params, config.getReportProbeDelay(), config.getReportProbeRetries());
        } catch (Exception e) {
            LOG.error("Unexpected exception occurred while scheduling report probe [id={}]", id, e);
            throw new InternalServerException(Messages.CANNOT_SCHEDULE_PROBE);
        }
    }


    /**
     * Un-schedules for the continuous polling of a report.
     *
     * @param id Report identifier
     */
    public void unScheduleReportProbe(String id) {
        try {
            removeSchedule(id, ProbeJob.ProbeType.REPORT);
        } catch (Exception e) {
            LOG.error("Unexpected exception occurred while un-scheduling report probe [id={}]", id, e);
            throw new InternalServerException(Messages.CANNOT_UNSCHEDULE_PROBE);
        }
    }


    /**
     * Updates the scheduling for the given report.
     *
     * @param id        Report identifier
     * @param succeeded Whether operation succeeded or not
     */
    public void updateReportProbe(String id, boolean succeeded) {
        try {
            updateSchedule(id, ProbeJob.ProbeType.REPORT, succeeded, config.getReportProbeRetries());
        } catch (Exception e) {
            LOG.error("Unexpected exception occurred while updating schedule for report probe [id={}]", id, e);
            throw new InternalServerException(Messages.CANNOT_UNSCHEDULE_PROBE);
        }
    }

    private void updateSchedule(String id, ProbeJob.ProbeType type, boolean succeeded, int maxRetries) throws SchedulerException {
        JobKey jkey = asJobKey(id, type);
        TriggerKey tkey = asTriggerKey(id, type);

        if (!scheduler.checkExists(jkey)) {
            LOG.warn("Scheduling does not exist [key={}]", jkey);
            return;
        }

        JobDetail jobDetail = scheduler.getJobDetail(jkey);
        int retries = jobDetail.getJobDataMap().getInt(ProbeJob.PROBE_RETRY_KEY);

        // skip job update, everything is just fine
        if (succeeded && retries == maxRetries)
            return;

        // if succeeded we need to reset the counter
        // if failed we have to decrease it
        retries = succeeded ? maxRetries : retries - 1;

        if (retries == 0) {
            scheduler.deleteJob(jkey);
            LOG.info("Scheduling removed after {} consecutive retries [id={}, type={}, key={}]", maxRetries, id, type, jkey);
        } else {
            jobDetail.getJobDataMap().put(ProbeJob.PROBE_RETRY_KEY, retries);
            scheduler.addJob(jobDetail, true, true);
            LOG.info("Scheduling updated [id={}, type={}, retries={}/{}, key={}]", id, type, retries, maxRetries, jkey);
        }
    }

    private void removeSchedule(String id, ProbeJob.ProbeType type) throws SchedulerException {
        JobKey jkey = asJobKey(id, type);

        if (!scheduler.checkExists(jkey)) {
            LOG.warn("Scheduling does not exist [key={}]", jkey);
            return;
        }

        scheduler.deleteJob(jkey);
        LOG.info("Scheduling successfully removed [id={}, type={}, key={}]", id, type, jkey);
    }

    private void createSchedule(String id, ProbeJob.ProbeType type, String secondKey, Map<String, Object> params, int delaySec, int maxRetries) throws SchedulerException {
        JobKey jkey = asJobKey(id, type);
        TriggerKey tKey = asTriggerKey(id, type);

        if (scheduler.checkExists(jkey)) {
            LOG.warn("Scheduling already exists [key={}]", jkey);
        } else {
            create(jkey, tKey, id, type, secondKey, params, delaySec, maxRetries);
        }
    }

    private void create(JobKey jkey, TriggerKey tkey, String id, ProbeJob.ProbeType type, String secondKey, Map<String, Object> params, int delaySec, int maxRetries) throws SchedulerException {
        LOG.debug("Scheduling job [id={}, type={}, delay={}s]", id, type, delaySec);

        String jsonParams = null;
        try {
            if (params != null)
                jsonParams = objectMapper.writeValueAsString(new JsonTypeMap<>(params));
            else
                jsonParams = objectMapper.writeValueAsString(new JsonTypeMap<>(new HashMap<>()));
        } catch (JsonProcessingException e) {
            throw new ValidationException("Cannot serialize params");
        }
        JobDetail detail = newJob()
                .ofType(ProbeJob.class)
                .usingJobData(ProbeJob.PROBE_ID_KEY, id)
                .usingJobData(ProbeJob.PROBE_TYPE_KEY, type.toString())
                .usingJobData(ProbeJob.PROBE_TYPE_SND_KEY, secondKey)
                .usingJobData(ProbeJob.PROBE_RETRY_KEY, maxRetries)
                .usingJobData(ProbeJob.PROBE_PARAMS_KEY, jsonParams)
                .withDescription("Probe job for entity " + id + " of type " + type)
                .withIdentity(jkey)
                .build();

        Trigger trigger = newTrigger()
                .forJob(jkey)
                .usingJobData(ProbeJob.PROBE_ID_KEY, id)
                .usingJobData(ProbeJob.PROBE_TYPE_KEY, type.toString())
                .usingJobData(ProbeJob.PROBE_TYPE_SND_KEY, secondKey)
                .usingJobData(ProbeJob.PROBE_RETRY_KEY, maxRetries)
                .usingJobData(ProbeJob.PROBE_PARAMS_KEY, jsonParams)
                .withDescription("Probe trigger for entity " + id + " of type " + type)
                .withIdentity(tkey)
                .withSchedule(repeatSecondlyForever(delaySec))
                .build();

        // saves the job and plans its execution
        scheduler.scheduleJob(detail, trigger);
    }

    private static JobKey asJobKey(String id, ProbeJob.ProbeType type) {
        return jobKey(valueOf(id), type.toString());
    }

    private static TriggerKey asTriggerKey(String id, ProbeJob.ProbeType type) {
        return triggerKey(valueOf(id), type.toString());
    }
}
