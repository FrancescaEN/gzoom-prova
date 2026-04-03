package it.mapsgroup.gzoom.common;

/**
 * @author Fabio G. Strozzi
 */
public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }
}
