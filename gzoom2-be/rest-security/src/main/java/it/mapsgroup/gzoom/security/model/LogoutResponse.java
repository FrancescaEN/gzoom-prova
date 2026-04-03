package it.mapsgroup.gzoom.security.model;

/**
 * @author Andrea Fossi.
 */
public class LogoutResponse {
    private int result;

    public LogoutResponse() {
    }

    public LogoutResponse(int i) {
        result = i;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}

