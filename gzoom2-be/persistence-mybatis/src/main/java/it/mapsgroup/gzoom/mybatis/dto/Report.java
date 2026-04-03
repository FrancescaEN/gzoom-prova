package it.mapsgroup.gzoom.mybatis.dto;


import java.util.List;

public class Report extends Content {
    private WorkEffortType workEffortType;
    private WorkEffortTypeContent workEffortTypeContent;
    private WorkEffortAnalysis workEffortAnalysis;
    private DataResource dataResource;

    private List<ReportParams> params;
    private List<ReportType> outputFormats;


    public WorkEffortType getWorkEffortType() {
        return workEffortType;
    }

    public void setWorkEffortType(WorkEffortType workEffortType) {
        this.workEffortType = workEffortType;
    }

    public WorkEffortTypeContent getWorkEffortTypeContent() {
        return workEffortTypeContent;
    }

    public void setWorkEffortTypeContent(WorkEffortTypeContent workEffortTypeContent) {
        this.workEffortTypeContent = workEffortTypeContent;
    }

    public WorkEffortAnalysis getWorkEffortAnalysis() {
        return workEffortAnalysis;
    }

    public void setWorkEffortAnalysis(WorkEffortAnalysis workEffortAnalysis) {
        this.workEffortAnalysis = workEffortAnalysis;
    }

    public DataResource getDataResource() {
        return dataResource;
    }

    public void setDataResource(DataResource dataResource) {
        this.dataResource = dataResource;
    }

    /**
     * @return the params
     */
    public List<ReportParams> getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(List<ReportParams> params) {
        this.params = params;
    }

    /**
     * @return the outputFormats
     */
    public List<ReportType> getOutputFormats() {
        return outputFormats;
    }

    /**
     * @param outputFormats the outputFormats to set
     */
    public void setOutputFormats(List<ReportType> outputFormats) {
        this.outputFormats = outputFormats;
    }
}
