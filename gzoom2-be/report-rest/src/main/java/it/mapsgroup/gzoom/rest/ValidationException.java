package it.mapsgroup.gzoom.rest;

/**
 * Thrown by the validator whenever something fails.
 */
public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = -8207370914153593728L;

    public ValidationException(String message) {
        super(message);
    }
}
