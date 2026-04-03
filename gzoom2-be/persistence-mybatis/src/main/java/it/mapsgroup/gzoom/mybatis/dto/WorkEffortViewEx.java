package it.mapsgroup.gzoom.mybatis.dto;

public class WorkEffortViewEx extends WorkEffortView {

    private Party party;

    private WorkEffortType workEffortType;

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public WorkEffortType getWorkEffortType() {
        return workEffortType;
    }

    public void setWorkEffortType(WorkEffortType workEffortType) {
        this.workEffortType = workEffortType;
    }
}
