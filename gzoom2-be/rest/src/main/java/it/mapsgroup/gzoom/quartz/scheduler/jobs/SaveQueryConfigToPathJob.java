package it.mapsgroup.gzoom.quartz.scheduler.jobs;

import it.mapsgroup.gzoom.mybatis.dao.QueryConfigDao;
import it.mapsgroup.gzoom.mybatis.dto.QueryConfig;
import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import it.mapsgroup.gzoom.service.QueryExecutorService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveQueryConfigToPathJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(SaveQueryConfigToPathJob.class);

    @Autowired
    private QueryExecutorService queryExecutorService;
    @Autowired
    private QueryConfigDao queryConfigDao;

    private String outputPath;
    private String queryId;
    private String userLoginId;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        JobKey key = context.getJobDetail().getKey();
        LOG.info("Execution service: SaveQueryConfigToPathJob for job: " + key);
        LOG.info("\nPARAMS: " +
                "\noutputPath = " + this.outputPath +
                "\nqueryId = " + this.queryId );

        if (this.queryId != null && this.outputPath != null && this.userLoginId != null) {
            UserLogin userLogin = new UserLogin();
            userLogin.setUserLoginId(this.userLoginId);
            QueryConfig queryConfig = this.queryConfigDao.getQueryConfig(this.queryId);
            if (queryConfig != null) {
                String outputpath = this.queryExecutorService.execQuery(queryConfig, userLogin, this.outputPath);
                LOG.info("OutputPath '{}'", outputpath);
            }
            else {
                LOG.error("QueryConfig missed");
            }

        }
        else {
            LOG.error("Parameters missed");
        }

    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }
}
