package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

/**
 * 19/02/14
 *
 * @author Elisa Spada
 */
public class EventMessageAuthMerge extends EventMessageAuthPropagate {

    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.AUTH_MERGE;
    }

    public void setConflictingAuthorizationKey(String conflictingAuthorizationKey) {
        this.put("conflictingAuthorizationKey", conflictingAuthorizationKey);
    }

    public String getConflictingAuthorizationKey() {
        return (String) this.get("conflictingAuthorizationKey");
    }

}
