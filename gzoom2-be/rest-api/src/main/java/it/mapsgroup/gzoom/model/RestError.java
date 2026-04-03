package it.mapsgroup.gzoom.model;

/**
 * Rest error message.
 *
 * @author Fabio G. Strozzi
 */
public class RestError {
    /**
     * Internal server error.
     */
    public static final int INTERNAL = 1;
    /**
     * Request is not well formatted.
     */
    public static final int BAD_REQUEST = 2;
    /**
     * No resource was found.
     */
    public static final int NOTHING_FOUND = 3;
    /**
     * Uploaded file exceeded the maximum file size.
     */
    public static final int MAX_FILE_EXCEEDED = 4;
    /**
     * Request is forbidden (due to illegal state issues mainly).
     */
    public static final int FORBIDDEN = 5;

    private final int code;
    private final String message;
    private final String[] params;

    public RestError(int code, String message) {
        this.code = code;
        this.message = message;
        this.params = new String[0];
    }

    public RestError(int code, String message, String[] params) {
        this.code = code;
        this.message = message;
        this.params = params;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String[] getParams() {
        return params;
    }

    public static RestError of(int code, String message) {
        return new RestError(code, message);
    }

    public static RestError internal() {
        return new RestError(INTERNAL, "Internal server error");
    }
}
