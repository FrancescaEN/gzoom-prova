package it.mapsgroup.gzoom.mybatis.dto;

public class Activity extends Timesheet {
    private WorkEffort workEffort1;
    private WorkEffort workEffort2;
    private WorkEffort workEffort3;

    public WorkEffort getWorkEffort1() {
        return workEffort1;
    }

    public void setWorkEffort1(WorkEffort workEffort1) {
        this.workEffort1 = workEffort1;
    }

    public WorkEffort getWorkEffort2() {
        return workEffort2;
    }

    public void setWorkEffort2(WorkEffort workEffort2) {
        this.workEffort2 = workEffort2;
    }

    public WorkEffort getWorkEffort3() {
        return workEffort3;
    }

    public void setWorkEffort3(WorkEffort workEffort3) {
        this.workEffort3 = workEffort3;
    }
}
