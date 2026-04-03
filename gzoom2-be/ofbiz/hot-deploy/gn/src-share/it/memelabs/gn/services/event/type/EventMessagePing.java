package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

/**
 * 15/01/14
 *
 * @author Andrea Fossi
 */
public class EventMessagePing extends EventMessage {

    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.PING;
    }

    public void setMessage(String message) {
        this.put("message", message);
    }

    public String getMessage() {
        return (String) this.get("message");
    }
}
