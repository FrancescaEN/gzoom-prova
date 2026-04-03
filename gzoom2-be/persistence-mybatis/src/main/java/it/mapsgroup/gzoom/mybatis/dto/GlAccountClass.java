package it.mapsgroup.gzoom.mybatis.dto;

import it.mapsgroup.gzoom.mybatis.AbstractIdentity;

import java.time.Instant;

public class GlAccountClass implements AbstractIdentity {
    private String glAccountClassId;

    private String parentClassId;

    private String description;

    private String isAssetClass;

    private Instant lastUpdatedStamp;

    private Instant lastUpdatedTxStamp;

    private Instant createdStamp;

    private Instant createdTxStamp;

    private String accountClassCode;

    private String localDescriptionContentId;

    private String accountTypeEnumId;

    private String childFolderFile;

    private String comments;

    private String lastModifiedByUserLogin;

    private String createdByUserLogin;

    public String getGlAccountClassId() {
        return glAccountClassId;
    }

    public void setGlAccountClassId(String glAccountClassId) {
        this.glAccountClassId = glAccountClassId;
    }

    public String getParentClassId() {
        return parentClassId;
    }

    public void setParentClassId(String parentClassId) {
        this.parentClassId = parentClassId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsAssetClass() {
        return isAssetClass;
    }

    public void setIsAssetClass(String isAssetClass) {
        this.isAssetClass = isAssetClass;
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

    public String getAccountClassCode() {
        return accountClassCode;
    }

    public void setAccountClassCode(String accountClassCode) {
        this.accountClassCode = accountClassCode;
    }

    public String getLocalDescriptionContentId() {
        return localDescriptionContentId;
    }

    public void setLocalDescriptionContentId(String localDescriptionContentId) {
        this.localDescriptionContentId = localDescriptionContentId;
    }

    public String getAccountTypeEnumId() {
        return accountTypeEnumId;
    }

    public void setAccountTypeEnumId(String accountTypeEnumId) {
        this.accountTypeEnumId = accountTypeEnumId;
    }

    public String getChildFolderFile() {
        return childFolderFile;
    }

    public void setChildFolderFile(String childFolderFile) {
        this.childFolderFile = childFolderFile;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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