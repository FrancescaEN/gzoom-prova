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
public class AnonymizeSubjectsJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(AnonymizeSubjectsJob.class);

    @Autowired
    private PersonService personService;
    @Autowired
    private PartyService partyService;
    @Autowired
    private ContactMechService contactMechService;
    @Autowired
    private WorkEffortService workEffortService;
    @Autowired
    private UserLoginService userLoginService;


    private int minusYears;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        JobKey key = context.getJobDetail().getKey();

        LOG.info("Execution service: AnonymizeSubjectsJob for job: " + key);
        LOG.info("\nPARAMS: " +
                "\nminusYears = " + this.minusYears);
        if (this.minusYears > 0) {
            Instant nowMinusYears = ZonedDateTime.now().minusYears(this.minusYears).toInstant();
            try {
                this.personService.anonymizePerson(nowMinusYears);
            } catch (Exception e) {
                LOG.error("Error update anonymizePerson: " + e.getMessage());
            }

            try {
                this.contactMechService.anonymizeContactMech(nowMinusYears);
            } catch (Exception e) {
                LOG.error("Error update anonymizeContactMech:" + e.getMessage());
            }

            try {
                this.workEffortService.anonymizeWorkEffort(nowMinusYears);
            } catch (Exception e) {
                LOG.error("Error update anonymizeWorkEffort:" + e.getMessage());
            }

            try {
                this.userLoginService.anonymizeUserLogin(nowMinusYears);
            } catch (Exception e) {
                LOG.error("Error update anonymizeUserLogin:" + e.getMessage());
            }

            /*ESEGUIRE PER ULTIMA*/
            try {
                this.partyService.anonymizeParty(nowMinusYears);
            } catch (Exception e) {
                LOG.error("Error update anonymizeParty:" + e.getMessage());
            }
        } else {
            LOG.error("Error execution service: AnonymizeSubjectsJob for job: " + key);
            LOG.error("minusYears <= 0");
        }
    }

    public void setMinusYears(int minusYears) {
        this.minusYears = minusYears;
    }
}
