package it.memelabs.gn.services.system;

/**
 * 22/10/13
 *
 * @author Andrea Fossi
 */
public enum PropertyEnumOfbiz {
    MAX_INVOCATION_LOG_ITEMS("max.invocation.log.items"),//max invocation items for each pk
    SEARCH_NEWEST_AUTH_TOLERANCE_TIME("search.newest.auth.tolerance.time"),
    SEARCH_NEWEST_AUTH_DROP_CALL_TIME("search.newest.auth.drop.call.time"),

    PARTY_INVITATION_EXPIRATION_NOTIFICATION_PERIODIC_INTERVAL("party.invitation.expiration.notification.periodic.interval"),
    PARTY_INVITATION_EXPIRATION_NOTIFICATION_LAST_COMMUNICATION("party.invitation.expiration.notification.last.communication"),
    PARTY_INVITATION_EXPIRATION("party.invitation.expiration"),
    INVITED_COMPANY_PUBLISH_AUTHORIZATION_REMINDER("invited.company.publish.authorization.reminder");

    private PropertyEnumOfbiz(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
