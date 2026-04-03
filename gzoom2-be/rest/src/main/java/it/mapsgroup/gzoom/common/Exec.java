package it.mapsgroup.gzoom.common;

import it.mapsgroup.gzoom.commons.error.TrySupplier;
import it.mapsgroup.gzoom.rest.InternalServerException;
import it.mapsgroup.gzoom.rest.ValidationException;
import it.mapsgroup.gzoom.security.Principals;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;

import java.security.Principal;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Fabio G. Strozzi
 */
public class Exec {
    private static final Logger LOG = getLogger(Exec.class);

    private Exec() {
    }

    public static <T, E extends Exception> T exec(String phase, TrySupplier<T, E> supplier) {
        try {
            return supplier.get();
        } catch (InternalServerException | ValidationException | AccessDeniedException e) {
            throw e;
        }
        catch (DataAccessException e) {
            Exec.logErrorException(phase, e);
            throw new InternalServerException(e.getCause().getLocalizedMessage());
        }
        catch (Exception e) {
            Exec.logErrorException(phase, e);
            throw new InternalServerException( e.getMessage());
        }
    }

    private static void logErrorException(String phase, Exception e){
        if (Principals.username() != null) {
            LOG.error("Unexpected exception [user={}, phase={}]", Principals.username(), phase, e);
        } else {
            LOG.error("Unexpected exception [user={}, phase={}]", "anonimo", phase, e);
        }
    }
}
