//EventMessageAuthPropagateDelete.java, created on 03/feb/2014
package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

/**
*@author Aldo Figlioli
*/
public class EventMessageAuthPropagateDelete extends EventMessageAuthPropagate {

    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.AUTH_PROPAGATE_DELETE;
    }
}
