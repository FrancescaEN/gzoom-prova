package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

import java.util.Map;

/**
 * 15/01/14
 *
 * @author Andrea Fossi
 */
public class EventMessageAuthAudit extends EventMessageAuthArchived {

    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.AUTH_AUDIT;
    }

    public void setContextPartyId(String contextPartyId) {
        this.put("contextPartyId", contextPartyId);
    }

    public String getContextPartyId() {
        return (String) this.get("contextPartyId");
    }

    public void setUserloginId(String userLoginId) {
        this.put("userLoginId", userLoginId);
    }

    public String getUserloginId() {
        return (String) this.get("userLoginId");
    }

    public void setActionType(String value) {
        setActionType(ActionTypeEnumOFBiz.valueOf(value));
    }


    public void setPreviousAuthorization(Map<String, Object> authorization) {
        this.put("previousAuthorization", authorization);
    }

    public Map<String, Object> getPreviousAuthorization() {
        return getMapFromMap(this, "previousAuthorization");
    }

    public void setActionType(ActionTypeEnumOFBiz actionType) {
        if (actionType == null) this.put("actionType", null);
        else
            this.put("actionType", actionType.name());
    }

    public ActionTypeEnumOFBiz getActionType() {
        String actionType = (String) this.get("actionType");
        if (actionType != null) return ActionTypeEnumOFBiz.valueOf(actionType);
        else return null;
    }

    /**
     * 23/01/14
     *
     * @author Andrea Fossi
     */
    public static enum ActionTypeEnumOFBiz {
        PUBLISHED,
        DELETED,
        VALIDATED,
        REJECTED,
        RECEIVED,
        CONFLICT_ACCEPTED,
        CONFLICT_REJECTED,
        CONFLICT_MERGED
    }
}
