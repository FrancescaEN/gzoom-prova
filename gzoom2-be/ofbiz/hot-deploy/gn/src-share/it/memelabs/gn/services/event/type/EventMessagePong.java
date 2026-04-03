package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

/**
 * 15/01/14
 *
 * @author Andrea Fossi
 */
public class EventMessagePong extends EventMessagePing {
    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.PONG;
    }
}
