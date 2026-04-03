package it.mapsgroup.gzoom.mybatis.dto;

public class PersonEx extends Person {
    private Party party;
    private PartyParentRole partyParentRole;
    private StatusItem statusItem;
    private EmplPositionType emplPositionType;
    private ContactMech contactMech;

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

    public StatusItem getStatusItem() {
        return statusItem;
    }

    public void setStatusItem(StatusItem statusItem) {
        this.statusItem = statusItem;
    }

    public EmplPositionType getEmplPositionType() {
        return emplPositionType;
    }

    public void setEmplPositionType(EmplPositionType emplPositionType) {
        this.emplPositionType = emplPositionType;
    }

    public ContactMech getContactMech() {
        return contactMech;
    }

    public void setContactMech(ContactMech contactMech) {
        this.contactMech = contactMech;
    }
}
