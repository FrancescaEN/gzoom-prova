package it.mapsgroup.gzoom.mybatis.dto;

public class WorkEffortTypeContentExt extends  WorkEffortTypeContent{
    private Content content;
    private WorkEffortType workEffortType;
    private WorkEffortTypePeriod workEffortTypePeriod;

    /**
     * @return the content
     */
    public Content getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(Content content) {
        this.content = content;
    }

    /**
     * @return the workEffortType
     */
    public WorkEffortType getWorkEffortType() {
        return workEffortType;
    }

    /**
     * @param workEffortType the workEffortType to set
     */
    public void setWorkEffortType(WorkEffortType workEffortType) {
        this.workEffortType = workEffortType;
    }

    public WorkEffortTypePeriod getWorkEffortTypePeriod() {
        return workEffortTypePeriod;
    }

    public void setWorkEffortTypePeriod(WorkEffortTypePeriod workEffortTypePeriod) {
        this.workEffortTypePeriod = workEffortTypePeriod;
    }
}
