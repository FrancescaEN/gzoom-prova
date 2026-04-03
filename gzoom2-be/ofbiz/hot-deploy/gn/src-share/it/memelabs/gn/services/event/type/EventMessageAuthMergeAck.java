package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

/**
 * 19/02/14
 *
 * @author Elisa Spada
 */
public class EventMessageAuthMergeAck extends EventMessageAuthPropagate {

    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.AUTH_MERGE_ACK;
    }

}
