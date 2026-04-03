package it.mapsgroup.gzoom.security.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Fabio G. Strozzi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
