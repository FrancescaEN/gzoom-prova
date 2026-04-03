package it.memelabs.gn.services.communication;

/**
 * 29/05/13
 *
 * @author Andrea Fossi
 */
public enum EventTypeOfbiz {

    /**
     * EVENT_AUTHORIZATION_TO_BE_VALIDATED
     */
    GN_COM_EV_AUTH_TO_VA(Boolean.TRUE),
    /**
     * EVENT_AUTHORIZATION_REJECTED
     */
    GN_COM_EV_AUTH_REJ(Boolean.TRUE),
    /**
     * EVENT_AUTHORIZATION_PUBLISHED
     */
    GN_COM_EV_AUTH_PUB(Boolean.TRUE),
    /**
     * EVENT_AUTHORIZATION_MODIFIED
     */
    GN_COM_EV_AUTH_MOD(Boolean.TRUE),
    /**
     * EVENT_AUTHORIZATION_CREATED
     */
    GN_COM_EV_AUTH_CRE(Boolean.TRUE),
    /**
     * EVENT_AUTHORIZATION_DRAFT_IMPORTED
     */
    GN_COM_EV_AUTH_IMP(Boolean.TRUE),
    /**
     * EVENT_PASSWORD_RECOVER
     */
    GN_COM_EV_RST_PWD(Boolean.FALSE),
    /**
     * EVENT_USER_REENABLED
     */
    GN_COM_EV_USR_RNBL(Boolean.FALSE),
    /**
     * EVENT_FIRST_LOGIN_ENABLED
     */
    GN_COM_EV_USR_FSTLGN(Boolean.FALSE),
    /**
     * EVENT_ AUTH_NOT_DELETED
     */
    GN_COM_EV_AUTH_NDEL(Boolean.TRUE),

    //job

    /**
     * AUTH_EXPIRING
     */
    GN_COM_EV_AUTH_EXPIR(Boolean.TRUE),
    /**
     * INVITATION_EXPIRING
     */
    GN_COM_EV_INVI_EXP(Boolean.FALSE),
    /**
     * INVITATION_EXPIRED
     */
    GN_COM_EV_INV_EXPD(Boolean.FALSE),
    /**
     * AUTH_VALIDATION_PENDING
     */
    GN_COM_EV_AUTH_V_PND(Boolean.FALSE),
    /**
     * AUTH_PUBLICATION_PENDING
     */
    GN_COM_EV_AUTH_PB_PN(Boolean.FALSE);


    private boolean subscribable; //opposite to internal

    EventTypeOfbiz(boolean subscribable) {
        this.subscribable = subscribable;
    }

    public boolean isSubscribable() {
        return subscribable;
    }

    public void setSubscribable(boolean subscribable) {
        this.subscribable = subscribable;
    }

}
