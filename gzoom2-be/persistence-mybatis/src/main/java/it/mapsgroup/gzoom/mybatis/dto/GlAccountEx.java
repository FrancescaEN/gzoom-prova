package it.mapsgroup.gzoom.mybatis.dto;

public class GlAccountEx {
    private GlAccount glAccount;
    private String[] workEffortPurposeTypeId;

    public String[] getWorkEffortPurposeTypeId() {
        return workEffortPurposeTypeId;
    }

    public void setWorkEffortPurposeTypeId(String[] workEffortPurposeTypeId) {
        this.workEffortPurposeTypeId = workEffortPurposeTypeId;
    }

    public GlAccount getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(GlAccount glAccount) {
        this.glAccount = glAccount;
    }
}
