package it.mapsgroup.gzoom.quartz.scheduler.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.QrtzJobDetailsDao;
import it.mapsgroup.gzoom.mybatis.dao.QrtzTriggersDao;
import it.mapsgroup.gzoom.mybatis.dto.QrtzJobDetails;
import it.mapsgroup.gzoom.mybatis.dto.QrtzTriggers;
import it.mapsgroup.gzoom.quartz.scheduler.SchedulerService;
import it.mapsgroup.gzoom.quartz.scheduler.dto.InfoBase;
import it.mapsgroup.gzoom.quartz.scheduler.info.JobData;
import it.mapsgroup.gzoom.quartz.scheduler.dto.ServiceJob;
import it.mapsgroup.gzoom.service.ConfigurationImpl;
import org.quartz.Job;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Leonardo Minaudo
 */
@Service
public class PlannerService {
    private static final Logger LOG = getLogger(PlannerService.class);

    private final ServiceJob serviceList;
    private final QrtzJobDetailsDao qrtzJobDetailsDao;
    private final QrtzTriggersDao qrtzTriggersDao;
    private final SchedulerService schedulerService;

    @Autowired
    public PlannerService(ConfigurationImpl config, QrtzJobDetailsDao qrtzJobDetailsDao, QrtzTriggersDao qrtzTriggersDao, SchedulerService schedulerService) {
        this.qrtzJobDetailsDao = qrtzJobDetailsDao;
        this.qrtzTriggersDao = qrtzTriggersDao;
        this.schedulerService = schedulerService;
        this.serviceList = config.getServiceJob();
    }

    public ServiceJob getServiceList() {
        return  this.serviceList;
    }

    public Result<QrtzJobDetails> getJobDetails() {
        List<QrtzJobDetails> list = this.qrtzJobDetailsDao.selectBySchedName("schedulerFactory");
        return new Result<>(list, list.size());
    }

    public JobData getJobData(String jobName) {
        return this.schedulerService.getJobData(jobName);
    }

    public boolean createJob(InfoBase infoBase) throws ClassNotFoundException, SchedulerException {

        Class jobClass = (Class<? extends Job>) Class.forName(infoBase.getJobDetails().getJobClassName());
        JobData info = new JobData();

        info.setCallbackObject(infoBase.getCallbackObject());
        info.setCronExpression(infoBase.getCronExpression());
        info.setStartDate(infoBase.getStartDate());
        info.setEndDate(infoBase.getEndDate());
        info.setFrequency(infoBase.getFrequency());

        this.schedulerService.scheduleJobDetail(infoBase.getJobDetails().getJobName(), infoBase.getJobDetails().getDescription(), jobClass, info, principal().getUserLoginId());
        return true;
    }

    public boolean deleteJob(final String id) {
        return this.schedulerService.deleteJob(id);
    }

    public boolean updateDesc(String jobName, String desc) {
        return this.qrtzJobDetailsDao.updateDesc(jobName, desc);
    }


    public boolean updateTrigger( InfoBase infoBase) throws SchedulerException, ClassNotFoundException {
        Class jobClass = (Class<? extends Job>) Class.forName(infoBase.getJobDetails().getJobClassName());

        JobData info = new JobData();

        info.setCallbackObject(infoBase.getCallbackObject());
        info.setCronExpression(infoBase.getCronExpression());
        info.setStartDate(infoBase.getStartDate());
        info.setEndDate(infoBase.getEndDate());
        info.setFrequency(infoBase.getFrequency());

        this.schedulerService.updateTrigger(jobClass, infoBase.getJobDetails().getJobName(), info, principal().getUserLoginId());
        return true;
    }
    public Result<QrtzTriggers> getTriggers() {
        List<QrtzTriggers> list = this.qrtzTriggersDao.selectAll();
        return new Result<>(list, list.size());
    }
}
