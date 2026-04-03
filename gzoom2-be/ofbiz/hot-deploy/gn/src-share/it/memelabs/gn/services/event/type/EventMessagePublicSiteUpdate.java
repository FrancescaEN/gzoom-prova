package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

import java.util.List;
import java.util.Map;

/**
 * 22/01/14
 *
 * @author Andrea Fossi
 */
public class EventMessagePublicSiteUpdate extends EventMessage {

    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.PUBLIC_SITE_UPDATE;
    }

    /**
     * @param shareEnable
     */
    public void setShareEnable(String shareEnable) {
        this.put("shareEnable", shareEnable);
    }

    /**
     * @return
     */
    public String getShareEnable() {
        return (String) this.get("shareEnable");
    }

    public void setSharerNode(Map<String, Object> sharerNode) {
        this.put("sharerNode", sharerNode);
    }

    public Map<String, Object> getSharerNode() {
        return getMapFromMap(this, "sharerNode");
    }


    public void setAuthorizationToShareList(List<Map<String, Object>> authorizationToShareList) {
        this.put("authorizationToShareList", authorizationToShareList);
        if (authorizationToShareList != null) setAuthorizationToShareListSize(authorizationToShareList.size());
    }

    public List<Map<String, Object>> getAuthorizationToShareList() {
        return checkList(this.get("authorizationToShareList"));
    }

    public void setAuthorizationToShareListSize(Integer authorizationToShareListSize) {
        this.put("authorizationToShareListSize", authorizationToShareListSize);
    }

    public Integer getAuthorizationToShareListSize() {
        return (Integer) this.get("authorizationToShareListSize");
    }
}
