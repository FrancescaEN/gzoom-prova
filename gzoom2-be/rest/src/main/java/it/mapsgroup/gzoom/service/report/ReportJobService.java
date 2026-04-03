package it.mapsgroup.gzoom.service.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.QueryConfigDao;
import it.mapsgroup.gzoom.mybatis.dao.ReportActivityDao;
import it.mapsgroup.gzoom.mybatis.dto.QueryConfig;
import it.mapsgroup.gzoom.mybatis.dto.report.ReportQueryConfigTaskInfo;
import it.mapsgroup.gzoom.persistence.common.dto.enumeration.ReportActivityStatus;
import it.mapsgroup.gzoom.quartz.oldScheduler.JsonTypeMap;
import it.mapsgroup.gzoom.report.report.dto.CreateReport;
import it.mapsgroup.gzoom.rest.ValidationException;
import it.mapsgroup.gzoom.mybatis.dto.ReportActivity;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ReportJobService {
    private static final Logger LOG = getLogger(ReportJobService.class);

    private final ReportActivityDao reportDaoMB;
    private final ObjectMapper objectMapper;
    private final QueryConfigTaskService queryConfigTaskService;
    private final QueryConfigDao queryConfigDao;

    private static final String CSV = "text/csv";
    private static final String PDF = "application/pdf";
    private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String HTML2PDF = "text/html";

    @Autowired
    public ReportJobService(ReportActivityDao reportDaoMB, ObjectMapper objectMapper, QueryConfigTaskService queryConfigTaskService, QueryConfigDao queryConfigDao) {
        this.reportDaoMB = reportDaoMB;
        this.objectMapper = objectMapper;
        this.queryConfigTaskService = queryConfigTaskService;
        this.queryConfigDao = queryConfigDao;
    }


    public Result<ReportActivity> getActivity(String userLoginId) {
        List<ReportActivity> ret = reportDaoMB.getActivities(userLoginId);
        return new Result<>(ret, ret.size());
    }

    public String add(CreateReport report, QueryConfig query, HttpServletResponse response) {
       it.mapsgroup.gzoom.mybatis.dto.ReportActivity record = save(report);
       if (report.getContentTypeId().equalsIgnoreCase("QUERY_CONFIG")) {
            this.queryConfigTaskService.addToQueue(new ReportQueryConfigTaskInfo(record.getActivityId(), query, report.getParams(), response));
        }

        return record.getActivityId();
    }

    @Transactional
    public it.mapsgroup.gzoom.mybatis.dto.ReportActivity save(CreateReport report) {
        it.mapsgroup.gzoom.mybatis.dto.ReportActivity record = new it.mapsgroup.gzoom.mybatis.dto.ReportActivity();

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
        reportDaoMB.create(record);
        return record;
    }

    /**
     * Get ReportActivity
     *
     * @param reportActivityId
     * @return
     */
    public ReportActivity get(String reportActivityId) {
        return reportDaoMB.get(reportActivityId);
    }

}

