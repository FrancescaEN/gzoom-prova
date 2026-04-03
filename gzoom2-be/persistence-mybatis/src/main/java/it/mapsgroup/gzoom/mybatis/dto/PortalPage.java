package it.mapsgroup.gzoom.mybatis.dto;

import it.mapsgroup.gzoom.mybatis.AbstractIdentity;

import java.math.BigDecimal;
import java.time.Instant;

public class PortalPage implements AbstractIdentity {
    private String portalPageId;

    private String portalPageName;

    private String description;

    private String ownerUserLoginId;

    private String originalPortalPageId;

    private String parentPortalPageId;

    private BigDecimal sequenceNum;

    private String securityGroupId;

    private Instant lastUpdatedStamp;

    private Instant lastUpdatedTxStamp;

    private Instant createdStamp;

    private Instant createdTxStamp;

    private String helpContentId;

    public String getPortalPageId() {
        return portalPageId;
    }

    public void setPortalPageId(String portalPageId) {
        this.portalPageId = portalPageId;
    }

    public String getPortalPageName() {
        return portalPageName;
    }

    public void setPortalPageName(String portalPageName) {
        this.portalPageName = portalPageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerUserLoginId() {
        return ownerUserLoginId;
    }

    public void setOwnerUserLoginId(String ownerUserLoginId) {
        this.ownerUserLoginId = ownerUserLoginId;
    }

    public String getOriginalPortalPageId() {
        return originalPortalPageId;
    }

    public void setOriginalPortalPageId(String originalPortalPageId) {
        this.originalPortalPageId = originalPortalPageId;
    }

    public String getParentPortalPageId() {
        return parentPortalPageId;
    }

    public void setParentPortalPageId(String parentPortalPageId) {
        this.parentPortalPageId = parentPortalPageId;
    }

    public BigDecimal getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(BigDecimal sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
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

    public String getHelpContentId() {
        return helpContentId;
    }

    public void setHelpContentId(String helpContentId) {
        this.helpContentId = helpContentId;
    }
}