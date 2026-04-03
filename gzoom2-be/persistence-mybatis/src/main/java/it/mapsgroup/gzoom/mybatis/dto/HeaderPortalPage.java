package it.mapsgroup.gzoom.mybatis.dto;

import it.mapsgroup.gzoom.mybatis.AbstractIdentity;

import java.time.Instant;

public class HeaderPortalPage implements AbstractIdentity {

    private String schoolName;
    private String usrName;


    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    @Override
    public Instant getCreatedStamp() {
        return null;
    }

    @Override
    public void setCreatedStamp(Instant createdStamp) {

    }

    @Override
    public Instant getCreatedTxStamp() {
        return null;
    }

    @Override
    public void setCreatedTxStamp(Instant createdTxStamp) {

    }

    @Override
    public Instant getLastUpdatedStamp() {
        return null;
    }

    @Override
    public void setLastUpdatedStamp(Instant lastUpdatedStamp) {

    }

    @Override
    public Instant getLastUpdatedTxStamp() {
        return null;
    }

    @Override
    public void setLastUpdatedTxStamp(Instant lastUpdatedTxStamp) {

    }
}