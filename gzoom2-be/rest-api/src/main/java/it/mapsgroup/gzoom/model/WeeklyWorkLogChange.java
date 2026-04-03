package it.mapsgroup.gzoom.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeeklyWorkLogChange extends Identity {

    private Date date;
    private List<WeeklyWorkLogCompanyChange> companies;

    /**
     * 
     * @return date as ISO_8601
     */
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<WeeklyWorkLogCompanyChange> getCompanies() {
        if (companies == null) {
            companies = new ArrayList<>();
        }
        return companies;
    }

    public void setCompanies(List<WeeklyWorkLogCompanyChange> companies) {
        this.companies = companies;
    }
}
