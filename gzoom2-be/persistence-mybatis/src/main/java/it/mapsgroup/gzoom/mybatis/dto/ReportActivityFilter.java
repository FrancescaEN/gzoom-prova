package it.mapsgroup.gzoom.mybatis.dto;

import it.mapsgroup.gzoom.persistence.common.dto.enumeration.ReportActivityStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Fossi.
 */
public class ReportActivityFilter {
    List<ReportActivityStatus> states;

    public ReportActivityFilter() {
        states = new ArrayList<>();
    }

    public List<ReportActivityStatus> getStates() {
        return states;
    }

    public void setStates(List<ReportActivityStatus> states) {
        this.states = states;
    }
}
