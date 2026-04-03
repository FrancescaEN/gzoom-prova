package it.memelabs.gn.services.authorization;

/**
 * 16/04/13
 *
 * @author Andrea Fossi
 */
public enum AuthorizationStatusOfbiz {
    GN_AUTH_DRAFT,
    GN_AUTH_PUBLISHED,
    GN_AUTH_CANCELLED,
    GN_AUTH_DELETED,
    /**
     * GN_AUTH_TO_BE_VALIDATED
     */
    GN_AUTH_TO_BE_VALID,
    /**
     * NOT_VALID
     */
    GN_AUTH_NOT_VALID,
    GN_AUTH_CONFLICTING,
}
