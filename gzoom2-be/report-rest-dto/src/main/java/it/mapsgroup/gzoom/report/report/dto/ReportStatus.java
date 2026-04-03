package it.mapsgroup.gzoom.report.report.dto;


/**
 * @author Andrea Fossi.
 */
public class ReportStatus {
    private Integer queryCount;
    private Integer pageCount;
    private String status;
    private String task;
    private String activityStatus;


    public Integer getQueryCount() {
        return queryCount;
    }

    public void setQueryCount(Integer queryCount) {
        this.queryCount = queryCount;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    @Override
    public String toString() {
        return "ReportStatus{" +
                "queryCount=" + queryCount +
                ", pageCount=" + pageCount +
                ", status='" + status + '\'' +
                ", task='" + task + '\'' +
                ", activityStatus='" + activityStatus + '\'' +
                '}';
    }
}
