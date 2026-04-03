package it.mapsgroup.gzoom.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.memelabs.smartnebula.commons.DateUtil;

public class WeeklyWorkLog extends Identity {

    private Boolean dupFromPrev;
    private ConstructionSite constructionSite;
    private Date logDate;
    private Integer year;
    private Integer week;
    private EntityState state;
    private Date created;
    private Date modified;
    private Date completed;
    private int companies;
    private int persons;
    private int equipments;
    // key = Company.id
    private Map<Long, WorkLogEmployment> employments;

    @JsonIgnore
    public Boolean getDupFromPrev() {
        return dupFromPrev;
    }

    @JsonProperty
    public void setDupFromPrev(Boolean dupFromPrev) {
        this.dupFromPrev = dupFromPrev;
    }

    public ConstructionSite getConstructionSite() {
        return constructionSite;
    }

    public void setConstructionSite(ConstructionSite constructionSite) {
        this.constructionSite = constructionSite;
    }

    @JsonIgnore
    public Date getLogDate() {
        if (logDate == null && year != null && week != null) {
            logDate = DateUtil.getFirstDateOfCalendarWeek(week, year);
        }
        return logDate;
    }

    @JsonProperty
    public void setLogDate(Date logDate) {
        this.logDate = logDate;
        this.year = null;
        this.week = null;
    }

    public Integer getYear() {
        if (year == null && logDate != null) {
            year = Integer.valueOf(DateUtil.getYear(logDate));
        }
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
        this.logDate = null;
    }

    public Integer getWeek() {
        if (week == null && logDate != null) {
            week = Integer.valueOf(DateUtil.getWeekNumber(logDate));
        }
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
        this.logDate = null;
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
}
