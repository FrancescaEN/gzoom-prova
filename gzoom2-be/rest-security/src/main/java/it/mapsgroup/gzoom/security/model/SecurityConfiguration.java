package it.mapsgroup.gzoom.security.model;

import java.text.MessageFormat;

public interface SecurityConfiguration {

    /**
     * Retrieves the number of minutes after which the token is considered expired.
     * <p>
     * If 0 then token does not expires. Default is 0.
     * </p>
     *
     * @return the number of minutes after which the token is considered expired.
     */
    int getTokenExpiryMinutes();

}
