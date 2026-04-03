package it.mapsgroup.gzoom.security;

import it.mapsgroup.gzoom.security.model.Messages;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Fabio G. Strozzi
 */
public class Tokens {
    private static final String QUERY_PARAM = "token";
    private static final String BEARER = "Bearer ";

    /**
     * Extracts the JWT token from the header or from a {@code token} query parameter.
     *
     * @param req The HTTP request
     * @return The JWT token, with the prefix 'Bearer ' cleaned off.
     * @throws BadCredentialsException if Authorization header is not found or schema is not Bearer
     */
    public static String token(HttpServletRequest req) {
        String hdrToken = req.getHeader(HttpHeaders.AUTHORIZATION);
        String queryToken = req.getParameter(QUERY_PARAM);

        if (hdrToken == null && queryToken == null)
            throw new BadCredentialsException(Messages.NO_AUTHORIZATION_HEADER);

        if (queryToken != null)
            return queryToken;

        // remove schema from token
        if (!hdrToken.startsWith(BEARER))
            throw new BadCredentialsException(Messages.INVALID_AUTHORIZATION_SCHEMA);

        return hdrToken.substring(BEARER.length());
    }
}
