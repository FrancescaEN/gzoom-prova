package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.jasper.Report;


public class JasperReportTaskInfo {
    private final String id;
    private Report report;


    public JasperReportTaskInfo(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
