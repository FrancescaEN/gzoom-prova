package it.mapsgroup.gzoom.model;

import java.util.Date;

public class ConstructionSiteLog extends Identifiable {

    private String note;
    private Date logDate;
    private EntityState state;
    private ConstructionSite constructionSite;
    //statistics
    private Integer companies;
    private Integer persons;
    private Integer equipments;
    private Integer activities;
    private Integer accidents;

    public ConstructionSite getConstructionSite() {
        return constructionSite;
    }

    public void setConstructionSite(ConstructionSite constructionSite) {
        this.constructionSite = constructionSite;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public EntityState getState() {
        return state;
    }

    public void setState(EntityState state) {
        this.state = state;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public Integer getCompanies() {
        return companies;
    }

    public void setCompanies(Integer companies) {
        this.companies = companies;
    }

    public Integer getPersons() {
        return persons;
    }

    public void setPersons(Integer persons) {
        this.persons = persons;
    }

    public Integer getEquipments() {
        return equipments;
    }

    public void setEquipments(Integer equipments) {
        this.equipments = equipments;
    }

    public Integer getActivities() {
        return activities;
    }

    public void setActivities(Integer activities) {
        this.activities = activities;
    }

    public Integer getAccidents() {
        return accidents;
    }

    public void setAccidents(Integer accidents) {
        this.accidents = accidents;
    }
}
