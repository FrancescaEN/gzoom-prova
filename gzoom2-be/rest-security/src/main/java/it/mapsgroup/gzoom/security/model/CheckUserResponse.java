package it.mapsgroup.gzoom.security.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Fabio G. Strozzi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckUserResponse {
    private String userPrefValue;

    public CheckUserResponse() {}

    public CheckUserResponse(String userPrefValue) {
        this.userPrefValue = userPrefValue;
    }

    public void setUserPrefValue(String userPrefValue) {
        this.userPrefValue = userPrefValue;
    }

    public String getUserPrefValue() {
        return userPrefValue;
    }
}
