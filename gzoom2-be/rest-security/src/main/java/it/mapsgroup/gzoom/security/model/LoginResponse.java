package it.mapsgroup.gzoom.security.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Fabio G. Strozzi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    private String token;

    public LoginResponse() {}

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
