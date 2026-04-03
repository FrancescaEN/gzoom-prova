package it.mapsgroup.gzoom.security.dto.models;

public class ExternalLoginKey {
    public String getExternalLoginKey() {
        return externalLoginKey;
    }

    public void setExternalLoginKey(String externalLoginKey) {
        this.externalLoginKey = externalLoginKey;
    }

    private String externalLoginKey;

    public ExternalLoginKey(String externalLoginKey) {
        this.externalLoginKey = externalLoginKey;
    }

    public ExternalLoginKey() {
    }

}
