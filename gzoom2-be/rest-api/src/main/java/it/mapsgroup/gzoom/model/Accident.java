package it.mapsgroup.gzoom.model;

import java.math.BigDecimal;
import java.util.Date;

public class Accident extends Identifiable {
    private Date date;
    private ConstructionSite constructionSite;
    private PersonEmployment personEmployment;
    private BigDecimal duration;
    private String note;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ConstructionSite getConstructionSite() {
        return constructionSite;
    }

    public void setConstructionSite(ConstructionSite constructionSite) {
        this.constructionSite = constructionSite;
    }

    public PersonEmployment getPersonEmployment() {
        return personEmployment;
    }

    public void setPersonEmployment(PersonEmployment personEmployment) {
        this.personEmployment = personEmployment;
    }

    public BigDecimal getDuration() {
        return duration;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
