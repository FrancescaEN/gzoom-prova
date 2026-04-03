package it.mapsgroup.gzoom.mybatis.dto;

public class PartyRoleEx extends PartyRole {

    private Party party;
    private PartyParentRole partyParentRole;
    private RoleType roleType;

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

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}
