package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

/**
 * 15/01/14
 *
 * @author Andrea Fossi
 */
public class EventMessageAuthReceivedAck extends EventMessageAuthPropagate {
    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.AUTH_RECEIVED_ACK;
    }


}
