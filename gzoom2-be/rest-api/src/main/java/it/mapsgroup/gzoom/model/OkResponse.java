package it.mapsgroup.gzoom.model;

/**
 * @author Fabio G. Strozzi
 */
public class OkResponse {
    public static final OkResponse OK = new OkResponse("OK");
    private String message;

    public OkResponse() {}

    public OkResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
