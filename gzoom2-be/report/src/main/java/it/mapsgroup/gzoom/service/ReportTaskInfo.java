package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.birt.Report;

/**
 * @author Andrea Fossi.
 */
public class ReportTaskInfo {
    private final String id;
    private Report report;


    public ReportTaskInfo(String id) {
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
