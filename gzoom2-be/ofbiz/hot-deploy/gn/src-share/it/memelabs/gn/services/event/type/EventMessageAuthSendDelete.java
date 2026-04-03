package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

/**
 * 28/01/14
 *
 * @author Elisa Spada
 */
public class EventMessageAuthSendDelete extends EventMessageAuthPropagate {

    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.AUTH_SEND_DELETE;
    }

}
