package it.mapsgroup.gzoom.mybatis.dto;

public class WorkEffortPurposeTypeEx{
    private WorkEffortPurposeAccount workEffortPurposeAccount;
    private WorkEffortPurposeType workEffortPurposeType;
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

    public WorkEffortPurposeType getWorkEffortPurposeType() {
        return workEffortPurposeType;
    }

    public void setWorkEffortPurposeType(WorkEffortPurposeType workEffortPurposeType) {
        this.workEffortPurposeType = workEffortPurposeType;
    }

    public WorkEffortPurposeAccount getWorkEffortPurposeAccount() {
        return workEffortPurposeAccount;
    }

    public void setWorkEffortPurposeAccount(WorkEffortPurposeAccount workEffortPurposeAccount) {
        this.workEffortPurposeAccount = workEffortPurposeAccount;
    }
}
