package it.mapsgroup.gzoom.mybatis.dto;

public class PartyEx extends Party {

    private PartyParentRole partyParentRole;
    private PartyRole partyRole;

    /**
     * @return the partyParentRole
     */
    public PartyParentRole getPartyParentRole() {
        return partyParentRole;
    }

    /**
     * @param partyParentRole the partyParentRole to set
     */
    public void setPartyParentRole(PartyParentRole partyParentRole) {
        this.partyParentRole = partyParentRole;
    }

    public PartyRole getPartyRole() {
        return partyRole;
    }

    public void setPartyRole(PartyRole partyRole) {
        this.partyRole = partyRole;
    }
}