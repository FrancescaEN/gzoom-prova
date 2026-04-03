package it.mapsgroup.gzoom.common;

import it.mapsgroup.gzoom.rest.ValidationException;
import it.mapsgroup.gzoom.model.RestError;
import it.mapsgroup.gzoom.rest.InternalServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;

import static it.mapsgroup.gzoom.model.RestError.*;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

/**
 * This controller suggests how exception should be treated among all rest controllers.
 *
 * @author Fabio G. Strozzi
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * This constant represents what the {@link IllegalArgumentException} message contains when the maximum file size
     * is reached.
     */
    private static final String MULTIPART_SIZE_EXCEPTION_MSG = "Multipart Mime part file exceeds max file size";

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseEntity<RestError> onException(HttpServletRequest req, Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it
        if (findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;

        // in case of max size exceeded
        if (isMaxSizeExceeded(e)) {
            LOG.error("Attempt to upload a file that exceeds the maximum file size [url={}]", req.getRequestURL());
            RestError err = new RestError(MAX_FILE_EXCEEDED, "Maximum upload file size reached");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        throw e;
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseBody
    public ResponseEntity<RestError> onValidationException(AccessDeniedException e) {
        RestError err = new RestError(FORBIDDEN, e.getMessage());
        return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseBody
    public ResponseEntity<RestError> onValidationException(ValidationException e) {
        RestError err = new RestError(BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseBody
    public ResponseEntity<RestError> onNotFoundException(NotFoundException e) {
        RestError err = new RestError(NOTHING_FOUND, e.getMessage());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InternalServerException.class})
    @ResponseBody
    public ResponseEntity<RestError> onGenericException(InternalServerException e) {
        RestError err = new RestError(INTERNAL, e.getMessage());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({DataAccessException.class})
    @ResponseBody
    public ResponseEntity<RestError> onDataAccessException(DataAccessException e) {
        RestError err = new RestError(INTERNAL, e.getMessage());
        LOG.error("Error querying database", e);
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean isMaxSizeExceeded(Exception ex) {
        Throwable cause = ex.getCause();
        return cause != null &&
                cause instanceof IllegalStateException &&
                MULTIPART_SIZE_EXCEPTION_MSG.equals(cause.getMessage());
    }
}
