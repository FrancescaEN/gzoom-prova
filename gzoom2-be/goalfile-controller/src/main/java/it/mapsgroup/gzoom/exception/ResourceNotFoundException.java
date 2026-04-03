package it.mapsgroup.gzoom.exception;

import it.mapsgroup.gzoom.common.NotFoundException;

public class ResourceNotFoundException extends NotFoundException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
