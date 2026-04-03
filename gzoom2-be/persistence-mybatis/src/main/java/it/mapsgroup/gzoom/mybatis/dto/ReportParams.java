package it.mapsgroup.gzoom.mybatis.dto;

import java.util.ArrayList;
import java.util.List;

public class ReportParams {
    private List<ReportParam> params = new ArrayList<ReportParam>();
    private List<ReportParamService> services = new ArrayList<ReportParamService>();

    /**
     * @return the params
     */
    public List<ReportParam> getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(List<ReportParam> params) {
        this.params = params;
    }

    /**
     * @return the services
     */
    public List<ReportParamService> getServices() {
        return services;
    }

    /**
     * @param services the services to set
     */
    public void setServices(List<ReportParamService> services) {
        this.services = services;
    }
}
