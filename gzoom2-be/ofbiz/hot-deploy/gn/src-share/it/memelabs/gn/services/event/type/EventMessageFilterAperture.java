package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageDirectionOfbiz;
import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

/**
 * 31/03/14
 *
 * @author Elisa Spada
 */
public class EventMessageFilterAperture extends EventMessageNode {

    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.FILTER_APERTURE;
    }

    public void setSenderNodeKey(String senderNodeKey) {
        this.put("senderNodeKey", senderNodeKey);
    }

    public String getSenderNodeKey() {
        return (String) this.get("senderNodeKey");
    }

    public void setSenderPartyId(String senderPartyId) {
        this.put("senderPartyId", senderPartyId);
    }

    public String getSenderPartyId() {
        return (String) this.get("senderPartyId");
    }

    public void setTaxIdentificationNumber(String taxIdentificationNumber) {
        this.put("taxIdentificationNumber", taxIdentificationNumber);
    }

    public String getTaxIdentificationNumber() {
        return (String) this.get("taxIdentificationNumber");
    }

    public void setDirection(EventMessageDirectionOfbiz direction) {
        this.put("direction", (direction != null) ? direction.toString() : null);
    }

    public EventMessageDirectionOfbiz getDirection() {
        return (this.get("direction") != null) ? EventMessageDirectionOfbiz.valueOf((String) this.get("direction")) : null;
    }

}
