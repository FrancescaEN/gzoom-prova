package it.mapsgroup.gzoom.mybatis.dto;

import it.mapsgroup.gzoom.mybatis.AbstractIdentity;

import java.time.Instant;

public class GlAccountInputCalc implements AbstractIdentity {
    private String glAccountInputCalcId;

    private String glAccountId;
    private GlAccount glAccount;

    private String inputSequenceNum;

    private String glAccountIdRef;
    private GlAccount glAccountRef;

    private String factorCalculator;

    private String lastModifiedByUserLogin;

    private String createdByUserLogin;

    private Instant lastUpdatedStamp;

    private Instant lastUpdatedTxStamp;

    private Instant createdStamp;

    private Instant createdTxStamp;

    private String glFiscalTypeId;
    private GlFiscalType glFiscalType;

    public String getGlAccountInputCalcId() {
        return glAccountInputCalcId;
    }

    public void setGlAccountInputCalcId(String glAccountInputCalcId) {
        this.glAccountInputCalcId = glAccountInputCalcId;
    }

    public String getGlAccountId() {
        return glAccountId;
    }

    public void setGlAccountId(String glAccountId) {
        this.glAccountId = glAccountId;
    }

    public String getInputSequenceNum() {
        return inputSequenceNum;
    }

    public void setInputSequenceNum(String inputSequenceNum) {
        this.inputSequenceNum = inputSequenceNum;
    }

    public String getGlAccountIdRef() {
        return glAccountIdRef;
    }

    public void setGlAccountIdRef(String glAccountIdRef) {
        this.glAccountIdRef = glAccountIdRef;
    }

    public String getFactorCalculator() {
        return factorCalculator;
    }

    public void setFactorCalculator(String factorCalculator) {
        this.factorCalculator = factorCalculator;
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

    public String getGlFiscalTypeId() {
        return glFiscalTypeId;
    }

    public void setGlFiscalTypeId(String glFiscalTypeId) {
        this.glFiscalTypeId = glFiscalTypeId;
    }

    public GlAccount getGlAccountRef() {
        return glAccountRef;
    }

    public void setGlAccountRef(GlAccount glAccountRef) {
        this.glAccountRef = glAccountRef;
    }

    public GlFiscalType getGlFiscalType() {
        return glFiscalType;
    }

    public void setGlFiscalType(GlFiscalType glFiscalType) {
        this.glFiscalType = glFiscalType;
    }

    public GlAccount getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(GlAccount glAccount) {
        this.glAccount = glAccount;
    }
}