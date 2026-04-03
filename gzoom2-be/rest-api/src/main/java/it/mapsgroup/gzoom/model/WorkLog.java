package it.mapsgroup.gzoom.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WorkLog extends Identity {

    private ConstructionSite constructionSite;
    private Date day;
    private EntityState state;
    private Date created;
    private Date modified;
    private Date completed;
    private int companies;
    private int persons;
    private int equipments;
    private int externalPersonEvents;
    private int externalEquipmentEvents;
    // key = Company.id
    private Map<Long, WorkLogEmployment> employments;

    public ConstructionSite getConstructionSite() {
        return constructionSite;
    }

    public void setConstructionSite(ConstructionSite constructionSite) {
        this.constructionSite = constructionSite;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public EntityState getState() {
        return state;
    }

    public void setState(EntityState state) {
        this.state = state;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getCompleted() {
        return completed;
    }

    public void setCompleted(Date completed) {
        this.completed = completed;
    }

    public int getCompanies() {
        return companies;
    }

    public void setCompanies(int companies) {
        this.companies = companies;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public int getEquipments() {
        return equipments;
    }

    public void setEquipments(int equipments) {
        this.equipments = equipments;
    }

    public Map<Long, WorkLogEmployment> getEmployments() {
        if (employments == null) {
            employments = new HashMap<>();
        }
        return employments;
    }

    public void setEmployments(Map<Long, WorkLogEmployment> employments) {
        this.employments = employments;
    }

    public int getExternalPersonEvents() {
        return externalPersonEvents;
    }

    public void setExternalPersonEvents(int externalPersonEvents) {
        this.externalPersonEvents = externalPersonEvents;
    }

    public int getExternalEquipmentEvents() {
        return externalEquipmentEvents;
    }

    public void setExternalEquipmentEvents(int externalEquipmentEvents) {
        this.externalEquipmentEvents = externalEquipmentEvents;
    }
}
