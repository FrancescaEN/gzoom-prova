package it.mapsgroup.gzoom.quartz.scheduler.util;

import it.mapsgroup.gzoom.quartz.scheduler.info.JobData;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.Date;
import java.util.Map;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

public final class TimerUtils {
    private static final Logger LOG = getLogger(TimerUtils.class);

    private TimerUtils() {}


    public static JobDetail buildJobDetail(String jobName, String jobDescription, final Class jobClass, final JobData info, String userLoginId) {
        final JobDataMap jobDataMap = new JobDataMap();

        Map<String, Object> map = info.getCallbackObject();
        for (String key : map.keySet()){
            jobDataMap.put(key, map.get(key));
        }
        jobDataMap.put("userLoginId", userLoginId);
        jobDataMap.put(jobName, info);

        return JobBuilder
                .newJob(jobClass)
                .withIdentity(jobName)
                .setJobData(jobDataMap)
                .withDescription(jobDescription)
                .storeDurably(true)
                .build();
    }

    public static Trigger buildCronTrigger(final String jobName, final JobData info){
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(info.getCronExpression());
        TriggerBuilder triggerBuilder =TriggerBuilder
                .newTrigger()
                .withIdentity(jobName)
                .forJob(jobName)
                .withSchedule(cronScheduleBuilder)
                .startAt(Date.from(info.getStartDate()));
        if(info.getEndDate() != null) triggerBuilder.endAt(Date.from(info.getEndDate()));

        return triggerBuilder.build();
    }
}