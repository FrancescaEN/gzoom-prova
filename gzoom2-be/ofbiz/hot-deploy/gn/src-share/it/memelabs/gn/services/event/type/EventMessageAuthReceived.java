package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

/**
 * 15/01/14
 *
 * @author Andrea Fossi
 */
public class EventMessageAuthReceived extends EventMessageAuthPropagate {
    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.AUTH_RECEIVED;
    }

    public void setParentAuthorizationKey(String parentAuthorizationKey) {
        this.put("parentAuthorizationKey", parentAuthorizationKey);
    }

    public String getParentAuthorizationKey() {
        return (String) this.get("parentAuthorizationKey");
    }

}
