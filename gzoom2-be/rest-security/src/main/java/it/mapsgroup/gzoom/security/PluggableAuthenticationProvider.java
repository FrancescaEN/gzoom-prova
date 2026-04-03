package it.mapsgroup.gzoom.security;

/**
 * A plugglable provider can be enabled or disabled.
 *
 * @author Fabio G. Strozzi
 */
public interface PluggableAuthenticationProvider {

    /**
     * Returns true if this authentication provider is active, false otherwise.
     *
     * @return true if this authentication provider is active, false otherwise.
     */
    boolean isEnabled();
}
