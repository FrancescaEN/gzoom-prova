package it.mapsgroup.gzoom.mybatis.dto;

public class WorkEffortPartyAssignmentEx extends WorkEffortPartyAssignment {

    private WorkEffortView workEffortView;

    private RoleType roleType;

    private PartyRoleView partyRoleView;

    public WorkEffortView getWorkEffortView() {
        return workEffortView;
    }

    public void setWorkEffortView(WorkEffortView workEffortView) {
        this.workEffortView = workEffortView;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public PartyRoleView getPartyRoleView() {
        return partyRoleView;
    }

    public void setPartyRoleView(PartyRoleView partyRoleView) {
        this.partyRoleView = partyRoleView;
    }
}
