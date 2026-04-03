//EventMessageAuthIndexingDelete.java, created on 18/feb/2014
package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

/**
*@author Aldo Figlioli
*/
public class EventMessageAuthIndexingDelete extends EventMessageNode {
    @Override
    public EventMessageTypeOfBiz getType() {
        return EventMessageTypeOfBiz.AUTH_INDEXING_DELETE;
    }
    
    public void setAuthorizationKey(String authorizationKey) {
        this.put("authorizationKey", authorizationKey);
    }

    public String getAuthorizationKey() {
        return (String) this.get("authorizationKey");
    }
}
