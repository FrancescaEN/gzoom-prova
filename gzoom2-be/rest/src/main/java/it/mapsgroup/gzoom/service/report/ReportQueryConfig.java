package it.mapsgroup.gzoom.service.report;

import it.mapsgroup.gzoom.mybatis.dto.QueryConfig;
import it.mapsgroup.gzoom.model.Report;

public class ReportQueryConfig {
    private Report report;
    private QueryConfig queryConfig;

    public QueryConfig getQueryConfig() {
        return queryConfig;
    }

    public void setQueryConfig(QueryConfig queryConfig) {
        this.queryConfig = queryConfig;
    }

    public  Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
