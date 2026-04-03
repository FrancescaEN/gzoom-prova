package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 23/01/15
 *
 * @author Elisa Spada
 */
public class EventMessageEntityAudit extends EventMessage {

    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.ENTITY_AUDIT;
    }

    public List<Map<String, Object>> getEntries() {
        return checkList(this.get("entries"));
    }

    public void setEntries(List<Map<String, Object>> entries) {
        this.put("entries", entries);
    }

    public Map<String, Object> getAuditContext() {
        return (Map<String, Object>) this.get("auditContext");
    }

    public void setAuditContext(Map<String, Object> auditContext) {
        this.put("auditContext", auditContext);
    }

    public Date getAuditTimestamp() {
        return (Date) this.get("auditTimestamp");
    }

    public void setAuditTimestamp(Date auditTimestamp) {
        this.put("auditTimestamp", auditTimestamp);
    }

}
