package it.mapsgroup.gzoom.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.mapsgroup.gzoom.birt.BirtService;
import it.mapsgroup.gzoom.birt.Report;
import it.mapsgroup.gzoom.dto.JsonTypeMap;
import it.mapsgroup.gzoom.persistence.common.dto.enumeration.ReportActivityStatus;
import it.mapsgroup.gzoom.mybatis.dao.ReportActivityDao;
import it.mapsgroup.gzoom.mybatis.dto.ReportActivity;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.Locale;
import java.util.function.Consumer;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 */
public class ReportRunnableTask implements Runnable {
    private static final Logger LOG = getLogger(ReportRunnableTask.class);

    private final ReportTaskInfo reportTask;
    private final ReportActivityDao reportDao;
    private final BirtService birtService;
    private final ObjectMapper objectMapper;
    private final Consumer<ReportTaskInfo> whenDone;

    public ReportRunnableTask(ReportTaskInfo reportTask,
                              ReportActivityDao reportDao,
                              BirtService birtService,
                              ObjectMapper objectMapper,
                              Consumer<ReportTaskInfo> doneCallback) {
        this.reportTask = reportTask;
        this.reportDao = reportDao;
        this.birtService = birtService;
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
                Report report = birtService.build(record.getActivityId(),
                        record.getTemplateName(),
                        (String) params.get().get("parentTypeId"),
                        params.get(),
                        locale);
                reportTask.setReport(report);
                String objectInfo = birtService.run("report_" + record.getActivityId(), report);
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
