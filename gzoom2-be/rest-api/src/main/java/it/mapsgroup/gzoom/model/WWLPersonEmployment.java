package it.mapsgroup.gzoom.model;


import java.util.Date;

public class WWLPersonEmployment extends PersonEmployment {
    private Date timestamp;
    private Date logDate;
    private ConstructionSite constructionSite;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public ConstructionSite getConstructionSite() {
        return constructionSite;
    }

    public void setConstructionSite(ConstructionSite constructionSite) {
        this.constructionSite = constructionSite;
    }
}
