package it.mapsgroup.gzoom.security;

import it.mapsgroup.gzoom.security.model.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In memory storage of permits.
 *
 * @author Fabio G. Strozzi
 */
@Service("permitsStorage")
public class PermitsStorage {
    private final ConcurrentMap<String, Permit> permits;
    private final SecurityConfiguration config;

    @Autowired
    public PermitsStorage(SecurityConfiguration config) {
        this.config = config;
        this.permits = new ConcurrentHashMap<>();
    }

    /**
     * Saves the JWT and other user details.
     *
     * @param jwt      The JWT
     * @param userName Username
     */
    public void save(String jwt, String userName) {
        int min = config.getTokenExpiryMinutes();
        if (min <= 0) {
            min = 60 * 24 * 100; // minutes in 100 days
        }
        LocalDateTime exp = LocalDateTime.now().plus(min, ChronoUnit.MINUTES);
        permits.put(jwt, new Permit(jwt, userName, exp));
    }

    /**
     * Removes a permit associated to a certain JWT.
     *
     * @param jwt The JWT
     * @return The pruned permit or null if no one is associated to the JWT
     */
    public Permit prune(String jwt) {
        return permits.remove(jwt);
    }

    /**
     * Loads a permit associated to a certain JWT.
     *
     * @param jwt The JWT
     * @return The permit
     */
    public Permit load(String jwt) {
        return permits.get(jwt);
    }

    /**
     * Loads and validates a permit associated to a certain JWT.
     *
     * @param jwt The JWT
     * @return The permit
     */
    public boolean isValid(String jwt) {
        Permit permit = permits.get(jwt);
        // makes sure permit expiration is after the current date
        return permit != null && permit.expiration.isAfter(LocalDateTime.now());
    }

    /**
     * Graces the current permit of a specified amount of time.
     *
     * @param jwt    The JWT
     * @param amount The amount of time of the grace
     * @param unit   The unit of time to be added to the expiration date
     */
    public void grace(String jwt, int amount, ChronoUnit unit) {
        permits.computeIfPresent(jwt, (t, permit) -> {
            LocalDateTime e = permit.expiration.plus(amount, unit);
            return new Permit(permit.token, permit.username, e);
        });
    }

    public class Permit {
        public final String token;
        public final String username;
        public final LocalDateTime expiration;

        public Permit(String token, String username, LocalDateTime expiration) {
            this.token = token;
            this.username = username;
            this.expiration = expiration;
        }
    }
}
