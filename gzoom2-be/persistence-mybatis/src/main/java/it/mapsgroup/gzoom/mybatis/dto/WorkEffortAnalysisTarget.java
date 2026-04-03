package it.mapsgroup.gzoom.mybatis.dto;

import java.math.BigDecimal;

public class WorkEffortAnalysisTarget {

    private String workEffortId;
    private String workEffortEtch;
    private String workEffortName;
    private String workEffortNameLang;
    private String parentRoleCode;
    private String partyName;
    private String partyNameLang;
    private String workEffortAnalysisId;
    private BigDecimal scAmount;
    private String rvcIconContentId;
    private String drcObjectInfo;
    private BigDecimal stAmount;
    private String rvtIconContentId;
    private String drtObjectInfo;
    private BigDecimal sc2Amount;
    private String rvc2IconContentId;
    private String drc2ObjectInfo;
    private BigDecimal st2Amount;
    private String rvt2IconContentId;
    private String drt2ObjectInfo;


    public String getWorkEffortId() {
        return workEffortId;
    }

    public void setWorkEffortId(String workEffortId) {
        this.workEffortId = workEffortId;
    }

    public String getWorkEffortNameLang() {
        return workEffortNameLang;
    }

    public void setWorkEffortNameLang(String workEffortNameLang) {
        this.workEffortNameLang = workEffortNameLang;
    }

    public BigDecimal getScAmount() {
        return scAmount;
    }

    public void setScAmount(BigDecimal scAmount) {
        this.scAmount = scAmount;
    }


    public String getRvcIconContentId() {
        return rvcIconContentId;
    }

    public void setRvcIconContentId(String rvcIconContentId) {
        this.rvcIconContentId = rvcIconContentId;
    }

    public String getDrcObjectInfo() {
        return drcObjectInfo;
    }

    public void setDrcObjectInfo(String drcObjectInfo) {
        this.drcObjectInfo = drcObjectInfo;
    }

    public BigDecimal getStAmount() {
        return stAmount;
    }

    public void setStAmount(BigDecimal stAmount) {
        this.stAmount = stAmount;
    }

    public String getRvtIconContentId() {
        return rvtIconContentId;
    }

    public void setRvtIconContentId(String rvtIconContentId) {
        this.rvtIconContentId = rvtIconContentId;
    }

    public String getDrtObjectInfo() {
        return drtObjectInfo;
    }

    public void setDrtObjectInfo(String drtObjectInfo) {
        this.drtObjectInfo = drtObjectInfo;
    }

    public BigDecimal getSc2Amount() {
        return sc2Amount;
    }

    public void setSc2Amount(BigDecimal sc2Amount) {
        this.sc2Amount = sc2Amount;
    }

    public String getRvc2IconContentId() {
        return rvc2IconContentId;
    }

    public void setRvc2IconContentId(String rvc2IconContentId) {
        this.rvc2IconContentId = rvc2IconContentId;
    }

    public String getDrc2ObjectInfo() {
        return drc2ObjectInfo;
    }

    public void setDrc2ObjectInfo(String drc2ObjectInfo) {
        this.drc2ObjectInfo = drc2ObjectInfo;
    }

    public BigDecimal getSt2Amount() {
        return st2Amount;
    }

    public void setSt2Amount(BigDecimal st2Amount) {
        this.st2Amount = st2Amount;
    }

    public String getRvt2IconContentId() {
        return rvt2IconContentId;
    }

    public void setRvt2IconContentId(String rvt2IconContentId) {
        this.rvt2IconContentId = rvt2IconContentId;
    }

    public String getDrt2ObjectInfo() {
        return drt2ObjectInfo;
    }

    public void setDrt2ObjectInfo(String drt2ObjectInfo) {
        this.drt2ObjectInfo = drt2ObjectInfo;
    }

    @Override
    public String toString() {
        return "WorkEffortAnalysisTargets{" +
                "workEffortId='" + workEffortId + '\'' +
                ", workEffortNameLang='" + workEffortNameLang + '\'' +
                ", scAmount='" + scAmount + '\'' +
                ", rvcIconContentId='" + rvcIconContentId + '\'' +
                ", drcObjectInfo='" + drcObjectInfo + '\'' +
                ", stAmount='" + stAmount + '\'' +
                ", rvtIconContentId='" + rvtIconContentId + '\'' +
                ", drtObjectInfo='" + drtObjectInfo + '\'' +
                ", sc2Amount='" + sc2Amount + '\'' +
                ", rvc2IconContentId='" + rvc2IconContentId + '\'' +
                ", drc2ObjectInfo='" + drc2ObjectInfo + '\'' +
                ", st2Amount='" + st2Amount + '\'' +
                ", rvt2IconContentId='" + rvt2IconContentId + '\'' +
                ", drt2ObjectInfo='" + drt2ObjectInfo + '\'' +
                '}';
    }

    public String getWorkEffortName() {
        return workEffortName;
    }

    public void setWorkEffortName(String workEffortName) {
        this.workEffortName = workEffortName;
    }

    public String getWorkEffortAnalysisId() {
        return workEffortAnalysisId;
    }

    public void setWorkEffortAnalysisId(String workEffortAnalysisId) {
        this.workEffortAnalysisId = workEffortAnalysisId;
    }

    public String getWorkEffortEtch() {
        return workEffortEtch;
    }

    public void setWorkEffortEtch(String workEffortEtch) {
        this.workEffortEtch = workEffortEtch;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyNameLang() {
        return partyNameLang;
    }

    public void setPartyNameLang(String partyNameLang) {
        this.partyNameLang = partyNameLang;
    }

    public String getParentRoleCode() {
        return parentRoleCode;
    }

    public void setParentRoleCode(String parentRoleCode) {
        this.parentRoleCode = parentRoleCode;
    }
}
