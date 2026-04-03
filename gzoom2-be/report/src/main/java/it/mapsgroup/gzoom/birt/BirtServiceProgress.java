package it.mapsgroup.gzoom.birt;

import it.mapsgroup.gzoom.commons.collect.Tuple2;
import it.mapsgroup.gzoom.dto.ReportStatus;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IProgressMonitor;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.slf4j.LoggerFactory.getLogger;

public class BirtServiceProgress implements IProgressMonitor {
    private static final Logger LOG = getLogger(BirtServiceProgress.class);


    //private final BirtService birtService;
    private final AtomicReference<IEngineTask> task;
    private final AtomicInteger queryCount;
    private final AtomicInteger pageCount;
    private final AtomicReference<Tuple2<Boolean, String>> cancelled;

    public BirtServiceProgress() {
        //  this.birtService = birtService;
        task = new AtomicReference<IEngineTask>(null);
        queryCount = new AtomicInteger(0);
        pageCount = new AtomicInteger(0);
        cancelled = new AtomicReference<Tuple2<Boolean, String>>(new Tuple2<>(false, null));
    }

    public IEngineTask getTask() {
        return task.get();
    }

    public void setTask(IEngineTask task) {
        this.task.set(task);
        queryCount.set(0);
        pageCount.set(0);
    }

    /*
      //  @Override
        public void onAsyncJobProgress() {
            IEngineTask task = this.task.get();
            if (task != null) {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("queryCount", queryCount);
                result.put("pageCount", pageCount);
                String progressMessage = getBirtTaskMessage(task, result);
                result.put("progressMessage", progressMessage);
              //  checkInterrupted(task, !birtService.getJob().updateResult(result), progressMessage);
            }
        }
    */
    @Override
    public void onProgress(int type, int value) {
        //fixme remove (SLOW REPORT)
       /* try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        switch (type) {
            case START_QUERY:
                if (LOG.isTraceEnabled()) {
                    LOG.trace("START_QUERY " + value);
                }
                break;
            case END_QUERY:
                if (LOG.isTraceEnabled()) {
                    LOG.trace("END_QUERY " + value);
                }
                queryCount.incrementAndGet();
                checkInterrupted(task.get(), cancelled.get(), getBirtTaskMessage(task.get()));
                break;
            case START_PAGE:
                if (LOG.isTraceEnabled()) {
                    LOG.trace("START_PAGE " + value);
                }
                break;
            case END_PAGE:
                if (LOG.isTraceEnabled()) {
                    LOG.trace("END_PAGE " + value);
                }
                pageCount.incrementAndGet();
                checkInterrupted(task.get(), cancelled.get(), getBirtTaskMessage(task.get()));
                break;
        }
    }

    private String getBirtTaskMessage(IEngineTask task) {
        if (task != null) {
            switch (task.getTaskType()) {
                case IEngineTask.TASK_UNKNOWN:
                    return "TASK_UNKNOWN";
                case IEngineTask.TASK_GETPARAMETERDEFINITION:
                    return "TASK_GETPARAMETERDEFINITION";
                case IEngineTask.TASK_RUN:
                    return "TASK_RUN";
                case IEngineTask.TASK_RENDER:
                    return "TASK_RENDER";
                case IEngineTask.TASK_RUNANDRENDER:
                    return "TASK_RUNANDRENDER";
                case IEngineTask.TASK_DATAEXTRACTION:
                    return "TASK_DATAEXTRACTION";
                case IEngineTask.TASK_DATASETPREVIEW:
                    return "TASK_DATASETPREVIEW";
            }
        }
        return null;
    }

    private String getBirtTaskStatus(IEngineTask task) {
        if (task != null) {
            switch (task.getTaskType()) {
                case IEngineTask.STATUS_NOT_STARTED:
                    return "STATUS_NOT_STARTED";
                case IEngineTask.STATUS_RUNNING:
                    return "STATUS_RUNNING";
                case IEngineTask.STATUS_SUCCEEDED:
                    return "STATUS_SUCCEEDED";
                case IEngineTask.STATUS_FAILED:
                    return "STATUS_FAILED";
                case IEngineTask.STATUS_CANCELLED:
                    return "STATUS_CANCELLED";
            }
        }
        return null;
    }

    /**
     * Cancel current report
     *
     * @param reason reason of cancellation
     */
    public void cancel(String reason) {
        cancelled.set(new Tuple2<>(true, reason));
    }

    private void checkInterrupted(IEngineTask task, Tuple2<Boolean, String> interrupted, String progressMessage) {
        if (interrupted.first() && task != null && !task.getCancelFlag()) {
            LOG.info("Cancel task, progressMessage: {}", progressMessage);
            task.cancel(interrupted.second());
        }
    }


    public ReportStatus getStatus() {
        ReportStatus reportStatus = new ReportStatus();
        reportStatus.setPageCount(pageCount.get());
        reportStatus.setQueryCount(queryCount.get());
        reportStatus.setTask(getBirtTaskMessage(task.get()));
        reportStatus.setStatus(getBirtTaskStatus(task.get()));
        return reportStatus;
    }

}
