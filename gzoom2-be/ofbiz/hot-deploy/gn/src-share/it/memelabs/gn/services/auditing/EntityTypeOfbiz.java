package it.memelabs.gn.services.auditing;

/**
 * Auditing entities.
 *
 * @author Fabio Strozzi <fabio.strozzi@mapsengineering.com>
 */
public enum EntityTypeOfbiz {
    COMPANY,
    COMPANY_BASE,
    ORGANIZATION_NODE,
    CONTEXT,
    AUTH_RECEIVES_FROM_REL,
    AUTH_REL_FILTER,
    AUTH_FILTER_CONSTRAINT,
    REL_SLICE,
    REL_SLICE_CONSTRAINT,
    CONTACT_LIST,// NOTIFICATION_GROUP
    USER,
    PROPAGATION_NODE,
    AUTHORIZATION
}
