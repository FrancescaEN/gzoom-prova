package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

import java.util.Map;

/**
 * 22/01/14
 *
 * @author Andrea Fossi
 */
public class EventMessageCommunication extends EventMessage {

    public void setCommunicationEvent(Map<String, Object> communicationEvent) {
        this.put("communicationEvent", communicationEvent);
    }

    public Map<String, Object> getCommunicationEvent() {
        return getMapFromMap(this, "communicationEvent");
    }

    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.COMMUNICATION_EVENT;
    }
}
