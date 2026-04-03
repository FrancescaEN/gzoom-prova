package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

import java.util.Map;

/**
 * 15/01/14
 *
 * @author Andrea Fossi
 */
public class EventMessageAuthArchived extends EventMessageNode {

    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.AUTH_ARCHIVED;
    }

    public void setAuthorization(Map<String, Object> authorization) {
        this.put("authorization", authorization);
    }

    public Map<String, Object> getAuthorization() {
        return getMapFromMap(this, "authorization");
    }

}
