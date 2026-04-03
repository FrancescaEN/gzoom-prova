package it.mapsgroup.gzoom.mybatis.util;

public enum Permission {
    ADMIN("_ADMIN"),
    RESP("_RESP"),
    VIEW("_VIEW");

    private final String code;
    Permission(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
