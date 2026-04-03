package it.mapsgroup.gzoom.mybatis.dto;

public class JobLogEx extends JobLog {

    private JobLogServiceType jobLogServiceType;

    private int totalRow;

    public JobLogServiceType getJobLogServiceType() {
        return jobLogServiceType;
    }

    public void setJobLogServiceType(JobLogServiceType jobLogServiceType) {
        this.jobLogServiceType = jobLogServiceType;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }
}