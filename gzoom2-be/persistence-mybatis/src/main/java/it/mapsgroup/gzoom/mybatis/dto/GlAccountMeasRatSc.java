package it.mapsgroup.gzoom.mybatis.dto;

import it.mapsgroup.gzoom.mybatis.AbstractIdentity;

import java.time.Instant;

public class GlAccountMeasRatSc implements AbstractIdentity {
    private String glAccountId;

    private String uomId;

    private Double uomRatingValue;

    private String uomCode;

    private String uomDescr;

    private String uomCodeLang;

    private String uomDescrLang;

    private String lastModifiedByUserLogin;

    private String createdByUserLogin;

    private Instant lastUpdatedStamp;

    private Instant lastUpdatedTxStamp;

    private Instant createdStamp;

    private Instant createdTxStamp;

    public String getGlAccountId() {
        return glAccountId;
    }

    public void setGlAccountId(String glAccountId) {
        this.glAccountId = glAccountId;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public Double getUomRatingValue() {
        return uomRatingValue;
    }

    public void setUomRatingValue(Double uomRatingValue) {
        this.uomRatingValue = uomRatingValue;
    }

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public String getUomDescr() {
        return uomDescr;
    }

    public void setUomDescr(String uomDescr) {
        this.uomDescr = uomDescr;
    }

    public String getUomCodeLang() {
        return uomCodeLang;
    }

    public void setUomCodeLang(String uomCodeLang) {
        this.uomCodeLang = uomCodeLang;
    }

    public String getUomDescrLang() {
        return uomDescrLang;
    }

    public void setUomDescrLang(String uomDescrLang) {
        this.uomDescrLang = uomDescrLang;
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
}