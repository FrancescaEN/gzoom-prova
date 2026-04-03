package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

/**
 * 31/03/14
 *
 * @author Elisa Spada
 */
public class EventMessageFilterApertureSend extends EventMessageFilterAperture {

    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.FILTER_APERTURE_SEND;
    }

}
