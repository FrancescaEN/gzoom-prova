package it.mapsgroup.gzoom.mybatis.dto;

public class WorkEffortTypeExt extends WorkEffortType {
    private WorkEffortTypeContent workEffortTypeContent;
    private WorkEffortAnalysis workEffortAnalysis;


    /**
     * @return the workEffortAnalysis
     */
    public WorkEffortAnalysis getWorkEffortAnalysis() {
        return workEffortAnalysis;
    }

    /**
     * @param workEffortAnalysis the workEffortAnalysis to set
     */
    public void setWorkEffortAnalysis(WorkEffortAnalysis workEffortAnalysis) {
        this.workEffortAnalysis = workEffortAnalysis;
    }

    /**
     * @return the workEffortTypeContent
     */
    public WorkEffortTypeContent getWorkEffortTypeContent() {
        return workEffortTypeContent;
    }

    /**
     * @param workEffortTypeContent the workEffortTypeContent to set
     */
    public void setWorkEffortTypeContent(WorkEffortTypeContent workEffortTypeContent) {
        this.workEffortTypeContent = workEffortTypeContent;
    }

    /***
     * Riporto il nome in base se è un'analisi o un report legato al tipo obiettivo
     * @return
     */
    public String getWorkEffortTypeName() {
        if (workEffortAnalysis != null) {
            return workEffortAnalysis.getDescription();
        } else {
            return getDescription();
        }
    }
}
