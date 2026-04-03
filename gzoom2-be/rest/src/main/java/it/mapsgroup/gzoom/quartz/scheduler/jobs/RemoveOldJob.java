package it.mapsgroup.gzoom.quartz.scheduler.jobs;

import it.mapsgroup.gzoom.service.*;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;

@Component
public class RemoveOldJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(RemoveOldJob.class);

    @Autowired
    private UserLoginHistoryService userLoginHistoryService;

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private VisitService visitService;


    private int minusYears;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        JobKey key = context.getJobDetail().getKey();

        LOG.info("Execution service: RemoveOldJob for job: " + key);
        LOG.info("\nPARAMS: " +
                "\nminusYears = " + this.minusYears);
        if (this.minusYears > 0) {
            Instant nowMinusYears = ZonedDateTime.now().minusYears(this.minusYears).toInstant();

            try {
                this.visitorService.deleteOld(nowMinusYears);
            } catch (Exception e) {
                LOG.error("Error delete visitor:" + e.getMessage());
            }

            try {
                this.visitService.deleteOld(nowMinusYears);
            } catch (Exception e) {
                LOG.error("Error delete visit:" + e.getMessage());
            }

            try {
                this.userLoginHistoryService.deleteOld(nowMinusYears);
            } catch (Exception e) {
                LOG.error("Error delete UserLoginHistory:" + e.getMessage());
            }

        } else {
            LOG.error("Error execution service: RemoveOldJob for job: " + key);
            LOG.error("minusYears <= 0");
        }
    }

    public void setMinusYears(int minusYears) {
        this.minusYears = minusYears;
    }
}
