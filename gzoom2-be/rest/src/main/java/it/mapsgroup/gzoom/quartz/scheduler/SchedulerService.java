package it.mapsgroup.gzoom.quartz.scheduler;

import it.mapsgroup.gzoom.mybatis.dao.QrtzJobDetailsDao;
import it.mapsgroup.gzoom.mybatis.dao.QrtzTriggersDao;
import it.mapsgroup.gzoom.mybatis.dto.QrtzJobDetails;
import it.mapsgroup.gzoom.mybatis.dto.QrtzTriggers;
import it.mapsgroup.gzoom.quartz.scheduler.dto.Frequency;
import it.mapsgroup.gzoom.quartz.scheduler.dto.ServiceJob;
import it.mapsgroup.gzoom.quartz.scheduler.dto.ServiceJobInfo;
import it.mapsgroup.gzoom.quartz.scheduler.info.JobData;
import it.mapsgroup.gzoom.quartz.scheduler.util.TimerUtils;
import it.mapsgroup.gzoom.service.ConfigurationImpl;
import org.quartz.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * This class contains all the methods that interface with the quartz scheduler.
 *
 * @author Leonardo Minaudo
 */
@Service
public class SchedulerService {
    private final static Logger LOG = getLogger(SchedulerService.class);
    private final Scheduler scheduler;
    private final ConfigurationImpl config;
    private final QrtzJobDetailsDao qrtzJobDetailsDao;
    private final QrtzTriggersDao qrtzTriggersDao;
    private final ServiceJob serviceList;

    @Autowired
    public SchedulerService(Scheduler scheduler, ConfigurationImpl config, QrtzJobDetailsDao qrtzJobDetailsDao, QrtzTriggersDao qrtzTriggersDao) {
        this.scheduler = scheduler;
        this.config = config;
        this.qrtzJobDetailsDao = qrtzJobDetailsDao;
        this.serviceList = config.getServiceJob();
        this.qrtzTriggersDao = qrtzTriggersDao;
    }

    public <T extends Job> void scheduleJobDetail(String jobName, String jobDescription, Class<T> jobClass, JobData info, String userLoginId) throws SchedulerException {
        JobDetail jobDetail = TimerUtils.buildJobDetail(jobName, jobDescription, jobClass, info, userLoginId);
        Trigger trigger = TimerUtils.buildCronTrigger(jobName, info);

        this.scheduler.scheduleJob(jobDetail, trigger);
    }

    public <T extends Job> void addJob(String jobName, String jobDescription, Class<T> jobClass, JobData info, String userLoginId) throws SchedulerException {
        JobDetail jobDetail = TimerUtils.buildJobDetail(jobName, jobDescription, jobClass, info, userLoginId);
        this.scheduler.addJob(jobDetail, false, false);
    }

    public <T extends Job> void addTrigger(String jobName, JobData info) throws SchedulerException {
        Trigger trigger = TimerUtils.buildCronTrigger(jobName, info);
        this.scheduler.scheduleJob(trigger);
    }

    public JobData getJobData(String jobName) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName));
            if (jobDetail == null) {
                LOG.error("Failed to find timer with ID '{}'", jobName);
                return null;
            }
            return (JobData) jobDetail.getJobDataMap().get(jobName);
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public <T extends Job> void updateTrigger(Class<T> jobClass, String jobName, JobData info, String userLoginId) throws SchedulerException {
        JobDetail jobDetail = this.scheduler.getJobDetail(JobKey.jobKey(jobName));
        JobDetail jobDetailNew = TimerUtils.buildJobDetail(jobName, jobDetail.getDescription(), jobClass, info, userLoginId);
        this.scheduler.addJob(jobDetailNew, true);
        Trigger trigger = TimerUtils.buildCronTrigger(jobName, info);
        if (this.scheduler.getTrigger(TriggerKey.triggerKey(jobName)) != null) {
            this.scheduler.rescheduleJob(TriggerKey.triggerKey(jobName), trigger);
        } else {
            this.scheduler.scheduleJob(trigger);
        }
    }

    public boolean deleteJob(String id) {
        try {
            return scheduler.deleteJob(new JobKey(id));
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }


    @PostConstruct
    public void init() {
        if (config.isEnableScheduler()) {
            try {
                scheduler.start();
                scheduleServicesRequired();

            } catch (SchedulerException e) {
                LOG.error(e.getMessage(), e);
            }
        } else {
            LOG.error("Scheduler is disabled");
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void scheduleServicesRequired() {
        this.serviceList.getServicesRequired().forEach(serviceJobInfo -> {
            String jobName = serviceJobInfo.getName();
            String className = serviceJobInfo.getClassName();

            List<QrtzTriggers> qrtzTriggersList = this.qrtzTriggersDao.selectByJobName(jobName);
            List<QrtzJobDetails> qrtzJobDetailsList = this.qrtzJobDetailsDao.selectByClassName(className);

            if (qrtzJobDetailsList.isEmpty()) {
                try {
                    Class jobClass = (Class<? extends Job>) Class.forName(className);
                    JobData info = new JobData();
                    Map<String, Object> callbackObject = new HashMap<>();
                    serviceJobInfo.getParameters().forEach(parameterJob -> {
                        callbackObject.put(parameterJob.getKey(), parameterJob.getDefaultValue());
                    });
                    info.setCallbackObject(callbackObject);
                    info.setCronExpression(serviceJobInfo.getCronExpression());
                    info.setStartDate(Instant.now());
                    info.setFrequency(Frequency.CUSTOM.toString());
                    scheduleJobDetail(serviceJobInfo.getName(), serviceJobInfo.getDescription(), jobClass, info, "system");
                } catch (ClassNotFoundException e) {
                    LOG.error("ERROR: scheduleServiceRequired() -> ClassNotFoundException by " + className + ": " + e.getMessage());
                } catch (SchedulerException e) {
                    LOG.error("ERROR: scheduleServiceRequired() -> SchedulerException by " + serviceJobInfo.getKey() + ": " + e);
                }
            } else if (qrtzTriggersList.isEmpty()) {
                try {
                    Class jobClass = (Class<? extends Job>) Class.forName(className);

                    JobData info = new JobData();
                    Map<String, Object> callbackObject = new HashMap<>();
                    serviceJobInfo.getParameters().forEach(parameterJob -> {
                        callbackObject.put(parameterJob.getKey(), parameterJob.getDefaultValue());
                    });
                    info.setCallbackObject(callbackObject);
                    info.setCronExpression(serviceJobInfo.getCronExpression());
                    info.setStartDate(Instant.now());
                    info.setFrequency(Frequency.CUSTOM.toString());
                    updateTrigger(jobClass, jobName, info, "system");
                } catch (ClassNotFoundException e) {
                    LOG.error("ERROR: scheduleServiceRequired() -> ClassNotFoundException by " + className + ": " + e.getMessage());
                } catch (SchedulerException e) {
                    LOG.error("ERROR: scheduleServiceRequired() -> SchedulerException by " + serviceJobInfo.getKey() + ": " + e);
                }

            }
        });

    }
}
