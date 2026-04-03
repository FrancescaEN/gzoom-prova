package it.mapsgroup.gzoom.mybatis.dto;

import it.mapsgroup.gzoom.mybatis.AbstractIdentity;

import java.math.BigDecimal;
import java.time.Instant;

public class GlAccountOrganization implements AbstractIdentity {
    private String glAccountId;

    private String organizationPartyId;

    private String roleTypeId;

    private Instant fromDate;

    private Instant thruDate;

    private BigDecimal postedBalance;

    private Instant lastUpdatedStamp;

    private Instant lastUpdatedTxStamp;

    private Instant createdStamp;

    private Instant createdTxStamp;

    private String lastModifiedByUserLogin;

    private String createdByUserLogin;

    public String getGlAccountId() {
        return glAccountId;
    }

    public void setGlAccountId(String glAccountId) {
        this.glAccountId = glAccountId;
    }

    public String getOrganizationPartyId() {
        return organizationPartyId;
    }

    public void setOrganizationPartyId(String organizationPartyId) {
        this.organizationPartyId = organizationPartyId;
    }

    public String getRoleTypeId() {
        return roleTypeId;
    }

    public void setRoleTypeId(String roleTypeId) {
        this.roleTypeId = roleTypeId;
    }

    public Instant getFromDate() {
        return fromDate;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getThruDate() {
        return thruDate;
    }

    public void setThruDate(Instant thruDate) {
        this.thruDate = thruDate;
    }

    public BigDecimal getPostedBalance() {
        return postedBalance;
    }

    public void setPostedBalance(BigDecimal postedBalance) {
        this.postedBalance = postedBalance;
    }

    public Instant getLastUpdatedStamp() {
        return lastUpdatedStamp;
    }

    public void setLastUpdatedStamp(Instant lastUpdatedStamp) {
        this.lastUpdatedStamp = lastUpdatedStamp;
    }

    public Instant getLastUpdatedTxStamp() {
        return lastUpdatedTxStamp;
    }

    public void setLastUpdatedTxStamp(Instant lastUpdatedTxStamp) {
        this.lastUpdatedTxStamp = lastUpdatedTxStamp;
    }

    public Instant getCreatedStamp() {
        return createdStamp;
    }

    public void setCreatedStamp(Instant createdStamp) {
        this.createdStamp = createdStamp;
    }

    public Instant getCreatedTxStamp() {
        return createdTxStamp;
    }

    public void setCreatedTxStamp(Instant createdTxStamp) {
        this.createdTxStamp = createdTxStamp;
    }

    public String getLastModifiedByUserLogin() {
        return lastModifiedByUserLogin;
    }

    public void setLastModifiedByUserLogin(String lastModifiedByUserLogin) {
        this.lastModifiedByUserLogin = lastModifiedByUserLogin;
    }

    public String getCreatedByUserLogin() {
        return createdByUserLogin;
    }

    public void setCreatedByUserLogin(String createdByUserLogin) {
        this.createdByUserLogin = createdByUserLogin;
    }
}