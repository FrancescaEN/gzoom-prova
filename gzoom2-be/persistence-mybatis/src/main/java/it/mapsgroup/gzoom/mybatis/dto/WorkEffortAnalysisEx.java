package it.mapsgroup.gzoom.mybatis.dto;

public class WorkEffortAnalysisEx extends WorkEffortAnalysis {
    private WorkEffortType workEffortType;
    private WorkEffortType parentWorkEffortType;

    public WorkEffortType getWorkEffortType() {
        return workEffortType;
    }

    public void setWorkEffortType(WorkEffortType workEffortType) {
        this.workEffortType = workEffortType;
    }

    public WorkEffortType getParentWorkEffortType() {
        return parentWorkEffortType;
    }

    public void setParentWorkEffortType(WorkEffortType parentWorkEffortType) {
        this.parentWorkEffortType = parentWorkEffortType;
    }
}
