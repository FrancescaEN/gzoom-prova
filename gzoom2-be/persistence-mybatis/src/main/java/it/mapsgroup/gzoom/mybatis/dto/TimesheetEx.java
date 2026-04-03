package it.mapsgroup.gzoom.mybatis.dto;

public class TimesheetEx extends Timesheet {
    private Party party;

    private Party partyStructure;

    private int updatable;

    private PartyHistoryView partyHistoryView;

    private WorkEffortTypeContent workEffortTypeContent;

    private StatusItem statusItem;

    private WorkEffortTypePeriod workEffortTypePeriod;

    private PartyParentRole partyParentRole;

    private PartyParentRole partyParentRoleStructure;

    private PartyParentRole partyParentRoleUser;

    private Uom uom;

    private NoteData noteData;

    private TsByUserLogin tsByUserLogin;

    private CustomTimePeriod customTimePeriod;

    private int totalRow;

    public int getUpdatable() {
        return updatable;
    }

    public void setUpdatable(int updatable) {
        this.updatable = updatable;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public PartyHistoryView getPartyHistoryView() {
        return partyHistoryView;
    }

    public void setPartyHistoryView(PartyHistoryView partyHistoryView) {
        this.partyHistoryView = partyHistoryView;
    }

    public PartyParentRole getPartyParentRole() {
        return partyParentRole;
    }

    public void setPartyParentRole(PartyParentRole partyParentRole) {
        this.partyParentRole = partyParentRole;
    }

    public PartyParentRole getPartyParentRoleStructure() { return partyParentRoleStructure; }

    public void setPartyParentRoleStructure(PartyParentRole partyParentRoleStructure) { this.partyParentRoleStructure = partyParentRoleStructure; }

    public Uom getUom() {
        return uom;
    }

    public void setUom(Uom uom) {
        this.uom = uom;
    }

    public NoteData getNoteData() {
        return noteData;
    }

    public void setNoteData(NoteData noteData) {
        this.noteData = noteData;
    }

    public TsByUserLogin getTsByUserLogin() {
        return tsByUserLogin;
    }

    public void setTsByUserLogin(TsByUserLogin tsByUserLogin) {
        this.tsByUserLogin = tsByUserLogin;
    }

    public Party getPartyStructure() {
        return partyStructure;
    }

    public void setPartyStructure(Party partyStructure) {
        this.partyStructure = partyStructure;
    }

    public WorkEffortTypeContent getWorkEffortTypeContent() {
        return workEffortTypeContent;
    }

    public void setWorkEffortTypeContent(WorkEffortTypeContent workEffortTypeContent) {
        this.workEffortTypeContent = workEffortTypeContent;
    }

    public StatusItem getStatusItem() {
        return statusItem;
    }

    public void setStatusItem(StatusItem statusItem) {
        this.statusItem = statusItem;
    }

    public WorkEffortTypePeriod getWorkEffortTypePeriod() {
        return workEffortTypePeriod;
    }

    public void setWorkEffortTypePeriod(WorkEffortTypePeriod workEffortTypePeriod) {
        this.workEffortTypePeriod = workEffortTypePeriod;
    }

    public PartyParentRole getPartyParentRoleUser() {
        return partyParentRoleUser;
    }

    public void setPartyParentRoleUser(PartyParentRole partyParentRoleUser) {
        this.partyParentRoleUser = partyParentRoleUser;
    }

    public CustomTimePeriod getCustomTimePeriod() {
        return customTimePeriod;
    }

    public void setCustomTimePeriod(CustomTimePeriod customTimePeriod) {
        this.customTimePeriod = customTimePeriod;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }
}
