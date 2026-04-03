package it.mapsgroup.gzoom.mybatis.dto.report;


import it.mapsgroup.gzoom.mybatis.dto.QueryConfig;
import it.mapsgroup.gzoom.mybatis.dto.report.Report;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class ReportQueryConfigTaskInfo {
    private final String id;
    private QueryConfig query;
    private Report report;
    private HttpServletResponse response;
    private Map<String, Object> params;


    public ReportQueryConfigTaskInfo(String id, QueryConfig queryConfig, Map<String, Object> params, HttpServletResponse response) {
        this.id = id;
        this.query = queryConfig;
        this.response = response;
        this.params = params;
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

    public QueryConfig getQuery() {
        return query;
    }

    public void setQuery(QueryConfig query) {
        this.query = query;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
