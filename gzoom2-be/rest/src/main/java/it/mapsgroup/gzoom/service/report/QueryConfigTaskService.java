package it.mapsgroup.gzoom.service.report;

import it.mapsgroup.gzoom.mybatis.dao.ReportActivityDao;
import it.mapsgroup.gzoom.mybatis.dto.report.ReportQueryConfigTaskInfo;
import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import it.mapsgroup.gzoom.security.Principals;
import it.mapsgroup.gzoom.service.QueryExecutorService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class QueryConfigTaskService {
    private static final Logger LOG = getLogger(QueryConfigTaskService.class);

    private final TaskExecutor taskExecutor;
    private final ReportActivityDao reportActivityDao;
    private final QueryExecutorService queryExecutorService;
    private UserLogin principal;

    private final ConcurrentHashMap<String, ReportQueryConfigTaskInfo> tasks;

    public QueryConfigTaskService(@Qualifier("queryConfigReportTaskExecutor") TaskExecutor taskExecutor, ReportActivityDao reportActivityDao, QueryExecutorService queryExecutorService) {
        this.taskExecutor = taskExecutor;
        this.reportActivityDao = reportActivityDao;
        this.queryExecutorService = queryExecutorService;
        this.tasks =  new ConcurrentHashMap<>();
    }

    public void addToQueue(ReportQueryConfigTaskInfo reportTask) {
        this.principal = Principals.principal();
        tasks.put(reportTask.getId(), reportTask);
        taskExecutor.execute(new QueryConfigReportRunnableTask(reportTask, reportActivityDao, queryExecutorService, this.principal, reportTaskInfo -> {
            tasks.remove(reportTaskInfo.getId());
        }));

    }

}
