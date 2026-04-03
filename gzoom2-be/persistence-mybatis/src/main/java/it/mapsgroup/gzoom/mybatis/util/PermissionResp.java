package it.mapsgroup.gzoom.mybatis.util;

public enum PermissionResp {
    IS_ORG("IS_ORG"),
    IS_SUP("IS_SUP"),
    IS_TOP("IS_TOP"),
    IS_ROLE("IS_ROLE");

    private final String code;

    PermissionResp(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
