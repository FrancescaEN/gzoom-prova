package it.mapsgroup.gzoom.security;

import it.mapsgroup.gzoom.mybatis.dto.Party;
import it.mapsgroup.gzoom.mybatis.dto.Person;
import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Principal utilities.
 *
 * @author Fabio G. Strozzi
 */
public class Principals {

    /**
     * Retrieves the current principal.
     *
     * @return the current principal.
     */
    public static UserLogin principal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserLogin) {
            return (UserLogin) principal;
        }

        if (principal instanceof Jwt jwt) {
            UserLogin u = new UserLogin();

            u.setUserLoginId(jwt.getClaim("username"));
            u.setExternalLoginKey(jwt.getClaim("externalLoginKey"));

            if (jwt.hasClaim("id")) {
                u.setPartyId(jwt.getClaim("id"));
            }

            if (jwt.hasClaim("firstName")) {
                Person p = new Person();
                p.setFirstName(jwt.getClaim("firstName"));
                p.setLastName(jwt.getClaim("lastName"));
                u.setPerson(p);
            }

            return u;
        }

        throw new IllegalStateException("Unknown principal type: " + principal.getClass());

    }

    /**
     * Retrieves the current principal username.
     *
     * @return the current principal username.
     */
    public static String username() {
        UserLogin user = principal();
        return user != null ? user.getUsername() : null;
    }
}
