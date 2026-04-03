package it.mapsgroup.gzoom.mybatis.dto;

public class TimeEntryEx extends TimeEntry {

    private WorkEffort workEffort;

    private RateType rateType;

    public WorkEffort getWorkEffort() {
        return workEffort;
    }

    public void setWorkEffort(WorkEffort workEffort) {
        this.workEffort = workEffort;
    }

    public RateType getRateType() {
        return rateType;
    }

    public void setRateType(RateType rateType) {
        this.rateType = rateType;
    }
}
