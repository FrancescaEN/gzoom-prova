package it.mapsgroup.gzoom.quartz.scheduler.jobs;

import it.mapsgroup.gzoom.email.EmailService;
import it.mapsgroup.gzoom.mybatis.dao.QueryConfigDao;
import it.mapsgroup.gzoom.mybatis.dto.QueryConfig;
import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import it.mapsgroup.gzoom.quartz.scheduler.info.JobData;
import it.mapsgroup.gzoom.service.ConfigurationImpl;
import it.mapsgroup.gzoom.service.QueryExecutorService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Leonardo Minaudo
 */
@Component
public class SendQueryConfigByEmailJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(SendQueryConfigByEmailJob.class);

    @Autowired
    private EmailService emailService;
    @Autowired
    private QueryExecutorService queryExecutorService;
    @Autowired
    private QueryConfigDao queryConfigDao;
    @Autowired
    private ConfigurationImpl config;

    private String[] to;
    private String queryId;
    private String userLoginId;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        JobKey key = context.getJobDetail().getKey();
        LOG.info("Execution service: SendQueryConfigByEmailJob for job: " + key);
        LOG.info("PARAMS: queryConfigId = " + this.queryId);

        /*QueryConfig queryConfig = this.queryConfigDao.getQueryConfig(this.queryId);

        if(this.config.getDocumentPath() != null) {
            try {
                UserLogin userLogin = new UserLogin();
                userLogin.setUserLoginId(this.userLoginId);
                String outputpath = this.queryExecutorService.execQuery(queryConfig, userLogin, this.config.getDocumentPath() + "/scheduler");
                LOG.info("OutputPath '{}'", outputpath);
                this.emailService.sendMessageWithAttachment("", "PROVA SPRING", "Questa è una prova Gzoom2", outputpath);
                File myObj = new File(outputpath);
                if (myObj.delete()) {
                    LOG.info("Deleted the file: " + myObj.getName());
                } else {
                    LOG.warn("Failed to delete the file: {}", outputpath);
                }
            } catch (Exception e) {
                LOG.error("Error SendEmailWithAttachmentJob: {}", e);
            }
        }
        else {
            LOG.error("Document path missed");
        }*/
    }


    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public void setTo(String[] to) {
        this.to = to;
    }
}

/*
    CONFIGURAZIONE SERVIZIO FILE services.json
    {
      "key": "SendQueryConfigByEmailJob",
      "name": "Estrattore per email",
      "className": "it.mapsgroup.gzoom.quartz.scheduler.jobs.SendQueryConfigByEmailJob",
      "parameters": [
        {
          "key": "to",
          "name": "Email destinatari",
          "type": "String[]",
          "required": true
        },
        {
          "key": "queryId",
          "name": "Esecutore query da eseguire",
          "type": "String",
          "defaultValue": "10190",
          "required": true
        }
      ]
    }
 */