package it.mapsgroup.gzoom.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.mapsgroup.gzoom.birt.BirtConfig;
import it.mapsgroup.gzoom.dto.JsonTypeMap;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.persistence.common.dto.enumeration.ReportActivityStatus;
import it.mapsgroup.gzoom.mybatis.dto.ReportParams;
import it.mapsgroup.gzoom.mybatis.dao.ReportActivityDao;
import it.mapsgroup.gzoom.report.report.dto.CreateReport;
import it.mapsgroup.gzoom.report.report.dto.ReportStatus;
import it.mapsgroup.gzoom.rest.ValidationException;
import it.mapsgroup.gzoom.mybatis.dto.ReportActivity;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.List;


import static org.slf4j.LoggerFactory.getLogger;

/**
 * Profile service.
 */
@Service
public class ReportJobService {
    private static final Logger LOG = getLogger(ReportJobService.class);

    private final ReportActivityDao reportDao;
    private final ObjectMapper objectMapper;
    private final ReportTaskService taskService;
    private final JasperReportTaskService jasperTaskService;
    private final BirtConfig config;

    @Autowired
    public ReportJobService(ReportActivityDao reportDao, ObjectMapper objectMapper, ReportTaskService taskService, JasperReportTaskService jasperTaskService, BirtConfig config) {
        this.reportDao = reportDao;
        this.objectMapper = objectMapper;
        this.taskService = taskService;
        this.jasperTaskService = jasperTaskService;
        this.config = config;
    }


    public Result<ReportActivity> getActivity(String userLoginId) {
    	List<ReportActivity> ret = reportDao.getActivities(userLoginId);
        return new Result<>(ret, ret.size());
    }

    public String add(CreateReport report) {
    	//TODO aggiugo oda
    	report.getParams().put("odaDialect", config.getOdaDialect());
        ReportActivity record = save(report);
        boolean isJasperReport = false;
        if(report.getContentTypeId().equalsIgnoreCase("JREPORT")){
            isJasperReport = true;
        }
        if(isJasperReport){
            jasperTaskService.addToQueue(new JasperReportTaskInfo(record.getActivityId()));
        }
        else{
            taskService.addToQueue(new ReportTaskInfo(record.getActivityId()));
        }
        return record.getActivityId();
    }

    @Transactional
    public ReportActivity save(CreateReport report) {
        ReportActivity record = new ReportActivity();

        Locale locale = null;
        try {
            if (StringUtils.isNotEmpty(report.getReportLocale()))
                locale = LocaleUtils.toLocale(report.getReportLocale());
        } catch (Exception e) {
            LOG.error("Cannot parse locale", e);
        }
        Validators.assertNotNull(locale, "Locale cannot be null");

        record.setStatus(ReportActivityStatus.QUEUED.toString());
        //Nome dell rptdesign e della cartella
        record.setTemplateName(report.getResourceName());
        //Nome mostrato all'utente
        record.setReportName(report.getReportName());
        record.setReportLocale(report.getReportLocale());
        record.setCreatedByUserLogin(report.getCreatedByUserLogin());
        record.setLastModifiedByUserLogin(report.getModifiedByUserLogin());
        //Nome del file JSON con i parametri
        record.setContentName(report.getContentName());
        record.setMimeTypeId(report.getMimeTypeId());

        try {
            if (report.getParams() != null)
                record.setReportData(objectMapper.writeValueAsString(new JsonTypeMap<>(report.getParams())));
            else
                record.setReportData(objectMapper.writeValueAsString(new JsonTypeMap<>(new HashMap<>())));
        } catch (JsonProcessingException e) {
            throw new ValidationException("Cannot serialize params");
        }
        record.setCompletedStamp(Instant.now());
        reportDao.create(record);
        return record;
    }

    public String cancel(String id, String reason) {
        return taskService.cancel(id, reason) + "";
    }

    public ReportStatus getStatus(String id) {
        ReportStatus to = new ReportStatus();
        it.mapsgroup.gzoom.dto.ReportStatus from = taskService.getStatus(id);
        to.setPageCount(from.getPageCount());
        to.setQueryCount(from.getPageCount());
        to.setStatus(from.getStatus());
        to.setTask(from.getTask());
        to.setActivityStatus(from.getActivityStatus() != null ? from.getActivityStatus().toString() : null);
        return to;
    }

    //TODO FINIRE

    /**
     * Dato il nome del report vado aprender la lista dei parametro
     * nella cartella del report,
     * se non esiste prendo i valori dal report
     * (i vecchi report i valori non sono settati in modo corretto ed è meglio metterlil
     * in file di configurazione, mentre si può pensare nei nuovi di andarlia pescare dal report)
     *
     * @param contentName
     * @return
     */
    public ReportParams params(String parentTypeId,String resourceName, String contentName) {
        ReportParams params = new ReportParams();

        Path path = getReportParamsPath(parentTypeId,resourceName, contentName);
        if (Files.isReadable(path)) {
            return getParamsToFile(path.toFile());
        } else {
            //vado a prendere i parametri dal report
            //getReportParams(reportName); TODO
    		/*List<ReportParam> list = new ArrayList<ReportParam>();
    		ReportParam param = new ReportParam();
        	param.setParamType("LIST");
        	param.setMandatory(false);
        	param.setParamName("workEffortId");
        	list.add(param);
        	params.setParams(list);*/
        }
        return params;
    }

    private ReportParams getParamsToFile(File file) {
        LOG.info("getParamsToFile="+file.getPath());
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(file, ReportParams.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ReportParams();
    }

    private Path getReportParamsPath(String parentTypeId, String resourceName, String contentName) throws RuntimeException {
        String reportDirectory = config.getBirtReportInputDir();
        //return Paths.get(reportDirectory + File.separator + reportName + File.separator + reportName + ".json");
        Path path;
        if(config.getDeveloperBirtPath()!=null && !config.getDeveloperBirtPath().equals(""))
            path = Paths.get(reportDirectory + File.separator + "project" + File.separator + config.getDeveloperBirtPath() + File.separator + resourceName + File.separator + contentName + ".json");
        else
            path = Paths.get(reportDirectory + File.separator + "custom" + File.separator + resourceName + File.separator + contentName + ".json");

        if (!Files.isReadable(path)) {
            LOG.info("Cannot loading json for project/custom rptdesign: "+path);
            path = Paths.get(reportDirectory + File.separator + parentTypeId + File.separator + resourceName + File.separator + contentName + ".json");
            if (!Files.isReadable(path)) {
                LOG.info("Cannot loading json 2nd step: "+path);
                path = Paths.get(reportDirectory + File.separator + resourceName + File.separator + contentName + ".json");
                if(!Files.isReadable(path))
                    LOG.info("Cannot loading json 3th step: "+path);
            }
        }
        return path;
    }


    /**
     * Get ReportActivity
     *
     * @param reportActivityId
     * @return
     */
    public ReportActivity get(String reportActivityId) {
        return reportDao.get(reportActivityId);
    }

}
