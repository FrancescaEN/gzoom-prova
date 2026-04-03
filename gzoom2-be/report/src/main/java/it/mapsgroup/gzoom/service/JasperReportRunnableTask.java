package it.mapsgroup.gzoom.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.mapsgroup.gzoom.jasper.Report;
import it.mapsgroup.gzoom.dto.JsonTypeMap;
import it.mapsgroup.gzoom.jasper.JasperService;
import it.mapsgroup.gzoom.persistence.common.dto.enumeration.ReportActivityStatus;
import it.mapsgroup.gzoom.mybatis.dao.ReportActivityDao;
import it.mapsgroup.gzoom.mybatis.dto.ReportActivity;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.Locale;
import java.util.function.Consumer;

import static org.slf4j.LoggerFactory.getLogger;


public class JasperReportRunnableTask implements Runnable {
    private static final Logger LOG = getLogger(JasperReportRunnableTask.class);

    private final JasperReportTaskInfo reportTask;
    private final ReportActivityDao reportDao;
    private final JasperService jasperService;
    private final ObjectMapper objectMapper;
    private final Consumer<JasperReportTaskInfo> whenDone;

    public JasperReportRunnableTask(JasperReportTaskInfo reportTask,
                                    ReportActivityDao reportDao,
                                    JasperService jasperService,
                                    ObjectMapper objectMapper,
                                    Consumer<JasperReportTaskInfo> doneCallback) {
        this.reportTask = reportTask;
        this.reportDao = reportDao;
        this.jasperService = jasperService;
        this.objectMapper = objectMapper;
        this.whenDone = doneCallback;
    }

    @Override
    public void run() {
        LOG.info("Executing {}", reportTask.getId());

        ReportActivity record = reportDao.get(reportTask.getId());
        Boolean inCharge = reportDao.updateState(record.getActivityId(),
                ReportActivityStatus.QUEUED,
                ReportActivityStatus.RUNNING);
        if (inCharge) {
            try {
                JsonTypeMap<String, Object> params =
                        objectMapper.readValue(record.getReportData(), new TypeReference<JsonTypeMap<String, Object>>() {
                        });
                Locale locale = null;
                try {
                    if (StringUtils.isNotEmpty(record.getReportLocale()))
                        locale = LocaleUtils.toLocale(record.getReportLocale());
                } catch (Exception e) {
                    LOG.error("Cannot parse locale", e);
                }
                if (locale == null) {
                    LOG.warn("invalid report locale, setting default");
                    locale = Locale.ITALIAN;
                }
                Report report = jasperService.build(record.getActivityId(),
                        record.getTemplateName(),
                        (String) params.get().get("parentTypeId"),
                        params.get(),
                        locale);
                reportTask.setReport(report);
                String objectInfo = jasperService.run("report_" + record.getActivityId(), report);
                reportDao.updateState(record.getActivityId(),
                        ReportActivityStatus.RUNNING,
                        ReportActivityStatus.DONE,
                        null,
                        objectInfo);
                LOG.info("Done {}", reportTask.getId());
            } catch (Exception e) {
                LOG.error("Failed: cannot generate report", e);
                reportDao.updateState(record.getActivityId(),
                        ReportActivityStatus.RUNNING,
                        ReportActivityStatus.FAILED,
                        e.toString(),
                        null);
            } finally {
                //tasks.remove(reportTask.getId());
                whenDone.accept(reportTask);
            }
        } else {
            LOG.info("Skipped {}", reportTask.getId());
        }
    }
}
