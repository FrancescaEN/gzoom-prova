package it.mapsgroup.gzoom.security;


import it.mapsgroup.gzoom.mybatis.dto.UserLogin;

import java.util.Map;

/**
 * Maps a user profile into a {@link Map} of properties that are suitable to be included within a JWT token as the
 * payload.
 *
 * @author Fabio G. Strozzi
 */
public interface JwtPayloadMapper {

    /**
     * Converts the user profile into a {@link Map} from strings to objects.
     *
     * @param user The user profile
     * @return A map that represents the given user and that is suitable to be treated as JWT payload.
     */
    Map<String, Object> map(UserLogin user);

    /**
     * Extracts the username from the specified payload.
     *
     * @param payload The properties contained in a JWT payload.
     * @return The username if found, null otherwise.
     */
    String getUsername(Map<String, Object> payload);

    /**
     * Extracts the activeNodeId from the specified payload.
     *
     * @param payload The properties contained in a JWT payload.
     * @return The username if found, null otherwise.
     */
    Long getActiveNodeId(Map<String, Object> payload);

}
