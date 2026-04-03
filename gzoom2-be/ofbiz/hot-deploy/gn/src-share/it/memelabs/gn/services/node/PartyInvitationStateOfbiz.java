package it.memelabs.gn.services.node;

/**
 * 09/05/13
 *
 * @author Andrea Fossi
 */
public enum PartyInvitationStateOfbiz {
    PARTYINV_SENT, //SENT
    PARTYINV_UNSENT,//UNSENT
    PARTYINV_PENDING,//TO SEND
    PARTYINV_ACCEPTED,//CONSUMED
    //PARTYINV_DECLINED, not used
    PARTYINV_CANCELLED, // EXPIRED
    PARTYINV_INVAGAIN, // ALREADY INVITED by another
}
