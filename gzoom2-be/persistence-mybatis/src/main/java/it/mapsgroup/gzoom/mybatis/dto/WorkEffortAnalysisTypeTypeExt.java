package it.mapsgroup.gzoom.mybatis.dto;

public class WorkEffortAnalysisTypeTypeExt extends WorkEffortTypeType{
    private  WorkEffortAnalysis workEffortAnalysis;
    private WorkEffort workEffort;
    private Party party;
    private PartyParentRole partyParentRole;


    public WorkEffortAnalysis getWorkEffortAnalysis() {
        return workEffortAnalysis;
    }

    public void setWorkEffortAnalysis(WorkEffortAnalysis workEffortAnalysis) {
        this.workEffortAnalysis = workEffortAnalysis;
    }

    public WorkEffort getWorkEffort() {
        return workEffort;
    }

    public void setWorkEffort(WorkEffort workEffort) {
        this.workEffort = workEffort;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public PartyParentRole getPartyParentRole() {
        return partyParentRole;
    }

    public void setPartyParentRole(PartyParentRole partyParentRole) {
        this.partyParentRole = partyParentRole;
    }
}
