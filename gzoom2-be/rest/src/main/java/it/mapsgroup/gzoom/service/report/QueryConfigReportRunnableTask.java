package it.mapsgroup.gzoom.service.report;

import it.mapsgroup.gzoom.mybatis.dao.ReportActivityDao;
import it.mapsgroup.gzoom.mybatis.dto.report.ReportQueryConfigTaskInfo;
import it.mapsgroup.gzoom.persistence.common.dto.enumeration.ReportActivityStatus;
import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import it.mapsgroup.gzoom.service.QueryExecutorService;
import it.mapsgroup.gzoom.mybatis.dto.ReportActivity;
import org.slf4j.Logger;

import java.util.function.Consumer;

import static org.slf4j.LoggerFactory.getLogger;

public class QueryConfigReportRunnableTask implements Runnable {
    private static final Logger LOG = getLogger(QueryConfigReportRunnableTask.class);

    private final ReportQueryConfigTaskInfo reportTaskInfo;
    private final ReportActivityDao reportActivityDao;
    private final QueryExecutorService queryExecutorService;
    private final Consumer<ReportQueryConfigTaskInfo> whenDone;
    private UserLogin userLogin;

    public QueryConfigReportRunnableTask(ReportQueryConfigTaskInfo reportTaskInfo, ReportActivityDao reportActivityDao, QueryExecutorService queryExecutorService, UserLogin userlogin, Consumer<ReportQueryConfigTaskInfo> whenDone) {
        this.reportTaskInfo = reportTaskInfo;
        this.reportActivityDao = reportActivityDao;
        this.queryExecutorService = queryExecutorService;
        this.whenDone = whenDone;
        this.userLogin = userlogin;
    }

    @Override
    public void run() {
        LOG.info("Executing {}", reportTaskInfo.getId());

        ReportActivity record = reportActivityDao.get(reportTaskInfo.getId());
        Boolean inCharge = reportActivityDao.updateState(record.getActivityId(),
                ReportActivityStatus.QUEUED,
                ReportActivityStatus.RUNNING);

        if (inCharge) {
            try {
                String objectInfo = this.queryExecutorService.execQuery(reportTaskInfo.getQuery(), reportTaskInfo.getParams(), null, reportTaskInfo.getResponse(), this.userLogin, true, null, false);

                reportActivityDao.updateState(record.getActivityId(),
                        ReportActivityStatus.RUNNING,
                        ReportActivityStatus.DONE,
                        null,
                        objectInfo);
                LOG.info("Done {}", reportTaskInfo.getId());
            }catch (Exception e) {
                LOG.error("Failed: cannot generate report", e);
                reportActivityDao.updateState(record.getActivityId(),
                        ReportActivityStatus.RUNNING,
                        ReportActivityStatus.FAILED,
                        e.toString(),
                        null);
            } finally {
                //tasks.remove(reportTask.getId());
                whenDone.accept(reportTaskInfo);
            }
        } else {
            LOG.info("Skipped {}", reportTaskInfo.getId());
        }
    }
}
