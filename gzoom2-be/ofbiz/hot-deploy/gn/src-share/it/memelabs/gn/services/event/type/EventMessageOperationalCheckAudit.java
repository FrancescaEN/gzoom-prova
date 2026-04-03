package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

import java.util.List;
import java.util.Map;

/**
 * 09/04/14
 *
 * @author Elisa Spada
 */
public class EventMessageOperationalCheckAudit extends EventMessageNode {

    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.OPERATIONAL_CHECK_AUDIT;
    }

    public List<Map<String, Object>> getOperationalCheckDataList() {
        return checkList(this.get("operationalCheckDataList"));
    }

    public void setOperationalCheckDataList(List<Map<String, Object>> operationalCheckDataList) {
        this.put("operationalCheckDataList", operationalCheckDataList);
    }

    public void setUserloginId(String userLoginId) {
        this.put("userLoginId", userLoginId);
    }

    public String getUserloginId() {
        return (String) this.get("userLoginId");
    }


}
