package it.mapsgroup.gzoom.quartz.scheduler.jobs;

import it.mapsgroup.gzoom.mybatis.dao.*;
import it.mapsgroup.gzoom.mybatis.dto.*;
import it.mapsgroup.gzoom.service.ConfigurationImpl;
import it.mapsgroup.gzoom.service.QueryExecutorService;
import it.mapsgroup.gzoom.util.MimeTypeEnum;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servizio che si appoggia al servizio sendEmail del Gzoom legacy
 */
@Component
public class DistQueryConfigToEmailLegacyJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(DistQueryConfigToEmailLegacyJob.class);
    @Autowired
    private QueryExecutorService queryExecutorService;
    @Autowired
    private QueryConfigDao queryConfigDao;
    @Autowired
    private DistributionDao distributionDao;
    @Autowired
    private CommunicationEventDao communicationEventDao;
    @Autowired
    private CommunicationEventRoleDao communicationEventRoleDao;
    @Autowired
    private DataResourceDao dataResourceDao;
    @Autowired
    private ContentDao contentDao;
    @Autowired
    private CommEventContentAssocDao commEventContentAssocDao;
    @Autowired
    private ConfigurationImpl config;

    private String userLoginId;
    private String distributionQueryId;

    @Override
    @Transactional
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
                    if (item.getQueryId() != null
                            && item.getFileName() != null
                            && item.getEmailFrom() != null
                            && item.getEmailTo() != null
                            && item.getEmailSubject() != null
                            && item.getEmailBody() != null ) {
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
                        File file = new File(outputpath);

                        CommunicationEvent ce = new CommunicationEvent();
                        ce.setSubject(item.getEmailSubject());
                        ce.setContent(item.getEmailBody());
                        ce.setContactMechIdFrom(item.getEmailFrom());
                        ce.setContactMechIdTo(item.getEmailTo());
                        ce.setPartyIdTo(item.getPartyIdTo());
                        ce.setStatusId("COM_IN_PROGRESS");
                        ce.setCommunicationEventTypeId("AUTO_EMAIL_COMM");
                        ce.setEntryDate(Instant.now());
                        if (item.getBodyMimeType() != null){
                            ce.setContentMimeTypeId(item.getBodyMimeType());
                        }
                        else {
                            ce.setContentMimeTypeId("text/plain");
                        }
                        String ceId = this.communicationEventDao.create(ce);

                        CommunicationEventRole cer = new CommunicationEventRole();
                        cer.setCommunicationEventId(ceId);
                        cer.setContactMechId(item.getEmailTo());
                        cer.setPartyId(item.getPartyIdTo());
                        cer.setRoleTypeId("ADDRESSEE");
                        cer.setStatusId("COM_ROLE_CREATED");
                        this.communicationEventRoleDao.create(cer);

                        DataResource dr = new DataResource();
                        dr.setDataResourceId("CE_" + ceId);
                        dr.setDataResourceTypeId("LOCAL_FILE");
                        dr.setDataResourceName(file.getName());
                        dr.setStatusId("CTNT_IN_PROGRESS");

                        String exportMimeType = queryConfig.getExportMimeType();
                        String drMimeType = "text/plain";
                        if (exportMimeType.equals(MimeTypeEnum.CSV.getExportMimeType()))
                            drMimeType = MimeTypeEnum.CSV.getExportMimeType();
                        if (exportMimeType.equals(MimeTypeEnum.PDF.getExportMimeType()))
                            drMimeType = MimeTypeEnum.PDF.getExportMimeType();
                        if (exportMimeType.equals(MimeTypeEnum.XLSX.getExportMimeType()))
                            drMimeType = MimeTypeEnum.XLSX.getExportMimeType();
                        if (exportMimeType.equals(MimeTypeEnum.HTML.getExportMimeType()))
                            drMimeType = MimeTypeEnum.PDF.getExportMimeType();

                        dr.setMimeTypeId(drMimeType);
                        dr.setObjectInfo(file.getAbsolutePath());
                        dr.setIsPublic("N");
                        this.dataResourceDao.createWithId(dr);

                        Content c = new Content();
                        c.setContentId(dr.getDataResourceId());
                        c.setContentTypeId("TMP_ENCLOSE");
                        c.setDataResourceId(dr.getDataResourceId());
                        c.setContentName(item.getFileName());
                        c.setMimeTypeId(drMimeType);
                        this.contentDao.createWithId(c);

                        CommEventContentAssoc ceca = new CommEventContentAssoc();
                        ceca.setContentId(c.getContentId());
                        ceca.setCommunicationEventId(ceId);
                        ceca.setSequenceNum(BigDecimal.valueOf(1));
                        this.commEventContentAssocDao.create(ceca);
                    }
                    else {
                        LOG.error("Parameter required missed");
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
