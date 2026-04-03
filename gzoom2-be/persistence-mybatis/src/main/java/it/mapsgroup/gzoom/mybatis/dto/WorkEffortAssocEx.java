package it.mapsgroup.gzoom.mybatis.dto;

public class WorkEffortAssocEx extends WorkEffortAssoc {

    private WorkEffortMeasure workEffortMeasure;
    private WorkEffortAssocType workEffortAssocType;
    private WorkEffort workEffort;
    private WorkEffort workEffort2;
    private int totalRow;

    public WorkEffortMeasure getWorkEffortMeasure() {
        return workEffortMeasure;
    }

    public void setWorkEffortMeasure(WorkEffortMeasure workEffortMeasure) {
        this.workEffortMeasure = workEffortMeasure;
    }

    public WorkEffortAssocType getWorkEffortAssocType() {
        return workEffortAssocType;
    }

    public void setWorkEffortAssocType(WorkEffortAssocType workEffortAssocType) {
        this.workEffortAssocType = workEffortAssocType;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public WorkEffort getWorkEffort() {
        return workEffort;
    }

    public void setWorkEffort(WorkEffort workEffort) {
        this.workEffort = workEffort;
    }

    public WorkEffort getWorkEffort2() {
        return workEffort2;
    }

    public void setWorkEffort2(WorkEffort workEffort2) {
        this.workEffort2 = workEffort2;
    }
}
