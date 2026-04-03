package it.mapsgroup.gzoom.quartz.scheduler.jobs;

import it.mapsgroup.gzoom.email.EmailService;
import it.mapsgroup.gzoom.mybatis.dao.DistributionDao;
import it.mapsgroup.gzoom.mybatis.dao.QueryConfigDao;
import it.mapsgroup.gzoom.mybatis.dto.Distribution;
import it.mapsgroup.gzoom.mybatis.dto.QueryConfig;
import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import it.mapsgroup.gzoom.service.ConfigurationImpl;
import it.mapsgroup.gzoom.service.QueryExecutorService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 */
@Component
public class DistQueryConfigToEmailJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(DistQueryConfigToEmailJob.class);
    @Autowired
    private QueryExecutorService queryExecutorService;
    @Autowired
    private QueryConfigDao queryConfigDao;
    @Autowired
    private DistributionDao distributionDao;
    @Autowired
    private ConfigurationImpl config;
    @Autowired
    private EmailService emailService;

    private String userLoginId;
    private String distributionQueryId;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        JobKey key = context.getJobDetail().getKey();

        LOG.info("Execution service: DistQueryConfigToPathJob for job: " + key);
        LOG.info("\nPARAMS: " +
                "\ndistributionQueryId = " + this.distributionQueryId );

        if (this.distributionQueryId != null && this.userLoginId != null) {
            UserLogin userLogin = new UserLogin();
            userLogin.setUserLoginId(this.userLoginId);
            QueryConfig queryDistribution = this.queryConfigDao.getQueryConfig(this.distributionQueryId);
            List<Distribution> distributionList = this.distributionDao.findByQueryConfig(queryDistribution);
            if (distributionList.size() > 0) {
                for (Distribution item : distributionList) {
                    if (item.getQueryId() != null) {
                        QueryConfig queryConfig = this.queryConfigDao.getQueryConfig(item.getQueryId());

                        if (item.getCond0() != null && !item.getCond0().equals(""))
                            queryConfig.setCond0Info(item.getCond0());
                        if (item.getCond1() != null && !item.getCond1().equals(""))
                            queryConfig.setCond1Info(item.getCond1());
                        if (item.getCond2() != null && !item.getCond2().equals(""))
                            queryConfig.setCond2Info(item.getCond2());
                        if (item.getCond3() != null && !item.getCond3().equals(""))
                            queryConfig.setCond3Info(item.getCond3());
                        if (item.getCond4() != null && !item.getCond4().equals(""))
                            queryConfig.setCond4Info(item.getCond4());
                        if (item.getCond5() != null && !item.getCond5().equals(""))
                            queryConfig.setCond5Info(item.getCond5());
                        if (item.getCond6() != null && !item.getCond6().equals(""))
                            queryConfig.setCond6Info(item.getCond6());
                        if (item.getCond7() != null && !item.getCond7().equals(""))
                            queryConfig.setCond7Info(item.getCond7());

                        Map<String, Object> params = new HashMap<>();
                        if (item.getWorkEffortId() != null && !item.getWorkEffortId().equals(""))
                            params.put("#WORKEFFORTID#", item.getWorkEffortId());
                        if (item.getUserId() != null && !item.getUserId().equals(""))
                            userLogin.setUserLoginId(item.getUserId());
                        params.put("fileName", item.getFileName());

                        String outputpath = this.queryExecutorService.execQuery(queryConfig, params, userLogin, config.getDocumentPath() + "/scheduler");
                        LOG.info("OutputPath '{}'", outputpath);

                        this.emailService.sendMessageWithAttachment(item.getEmailTo(), item.getEmailSubject(), item.getEmailBody(), outputpath);
                        File myObj = new File(outputpath);
                        if (myObj.delete()) {
                            LOG.info("Deleted the file: " + myObj.getName());
                        } else {
                            LOG.warn("Failed to delete the file: {}", outputpath);
                        }
                    }

                }
            }
            else {
                LOG.error("Distributions missed");
            }
        }
        else {
            LOG.error("Parameters missed");
        }

    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public void setDistributionQueryId(String distributionQueryId) {
        this.distributionQueryId = distributionQueryId;
    }
}
