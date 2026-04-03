package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

/**
 * 15/01/14
 *
 * @author Andrea Fossi
 */
public class EventMessageAuthArchivedAck extends EventMessageNode {
    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.AUTH_ARCHIVED_ACK;
    }

    public void setAuthorizationKey(String authorizationKey) {
        this.put("authorizationKey", authorizationKey);
    }

    public String getAuthorizationKey() {
        return (String) this.get("authorizationKey");
    }

}
