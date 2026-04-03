package it.mapsgroup.gzoom.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AntimafiaProcess extends Identifiable {

    private Company company;
    private Identifiable prefecture;
    private EntityState state;
    private Lot lot;
    private EntityState causal;
    private Date expiryDate;
    private String note;
    private List<AntimafiaProcessPhase> phases;
    private Date created;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Identifiable getPrefecture() {
        return prefecture;
    }

    public void setPrefecture(Identifiable prefecture) {
        this.prefecture = prefecture;
    }

    public EntityState getState() {
        return state;
    }

    public void setState(EntityState state) {
        this.state = state;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public EntityState getCausal() {
        return causal;
    }

    public void setCausal(EntityState causal) {
        this.causal = causal;
    }

    @JsonProperty("validThrough")
    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @JsonProperty("notes")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<AntimafiaProcessPhase> getPhases() {
        return phases;
    }

    public void setPhases(List<AntimafiaProcessPhase> phases) {
        this.phases = phases;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
