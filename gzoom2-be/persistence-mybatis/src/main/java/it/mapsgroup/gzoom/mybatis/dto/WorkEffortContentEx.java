package it.mapsgroup.gzoom.mybatis.dto;

public class WorkEffortContentEx extends WorkEffortContent {

    private Content content;
    private DataResource dataResource;
    private WorkEffortContentType workEffortContentType;
    private WorkEffortView workEffortView;

    public WorkEffortView getWorkEffortView() {
        return workEffortView;
    }

    public void setWorkEffortView(WorkEffortView workEffortView) {
        this.workEffortView = workEffortView;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public DataResource getDataResource() {
        return dataResource;
    }

    public void setDataResource(DataResource dataResource) {
        this.dataResource = dataResource;
    }

    public WorkEffortContentType getWorkEffortContentType() {
        return workEffortContentType;
    }

    public void setWorkEffortContentType(WorkEffortContentType workEffortContentType) {
        this.workEffortContentType = workEffortContentType;
    }
}
