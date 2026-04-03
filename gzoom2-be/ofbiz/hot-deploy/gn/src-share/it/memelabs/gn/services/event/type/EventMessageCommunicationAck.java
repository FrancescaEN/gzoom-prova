package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

/**
 * 28/01/14
 *
 * @author Elisa Spada
 */
public class EventMessageCommunicationAck extends EventMessage {

    public void setCommunicationEventId(String communicationEventId) {
        this.put("communicationEventId", communicationEventId);
    }

    public String getCommunicationEventId() {
        return (String) this.get("communicationEventId");
    }

    public void setDelivered(String delivered) {
        this.put("delivered", delivered);
    }

    public void setDelivered(boolean delivered) {
        if (delivered)
            setDelivered("Y");
        else
            setDelivered("N");
    }

    public String getDelivered() {
        return (String) this.get("delivered");
    }

    public boolean isDelivered() {
        return (getDelivered().equals("Y"));
    }

    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.COMMUNICATION_ACK;
    }

}
