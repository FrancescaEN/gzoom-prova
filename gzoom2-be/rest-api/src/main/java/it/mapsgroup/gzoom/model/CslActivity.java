package it.mapsgroup.gzoom.model;

import java.util.Date;

/**
 * @author Andrea Fossi.
 */
public class CslActivity {
    private Long id;
    private String username;
    private Date timestamp;
    private String note;
    private ConstructionSiteLog constructionSiteLog;
    private Company company;
    private Wbs wbs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ConstructionSiteLog getConstructionSiteLog() {
        return constructionSiteLog;
    }

    public void setConstructionSiteLog(ConstructionSiteLog constructionSiteLog) {
        this.constructionSiteLog = constructionSiteLog;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Wbs getWbs() {
        return wbs;
    }

    public void setWbs(Wbs wbs) {
        this.wbs = wbs;
    }
}
