package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.node.PartyNodeTypeOfbiz;

/**
 * 15/01/14
 *
 * @author Andrea Fossi
 */
public abstract class EventMessageNode extends EventMessage {

    public void setNodeKey(String nodeKey) {
        this.put("nodeKey", nodeKey);
    }

    public void setPartyId(String partyId) {
        this.put("partyId", partyId);
    }

    public void setNodeType(PartyNodeTypeOfbiz nodeType) {
        if (nodeType == null) this.put("nodeType", null);
        else
            this.put("nodeType", nodeType.toString());
    }

    public void setNodeType(String nodeType) {
        if (nodeType == null) this.put("nodeType", null);
        else
            setNodeType(PartyNodeTypeOfbiz.valueOf(nodeType));
    }


    public String getNodeKey() {
        return (String) this.get("nodeKey");
    }

    public String getPartyId() {
        return (String) this.get("partyId");
    }

    public PartyNodeTypeOfbiz getNodeType() {
        String nodeType = (String) this.get("nodeType");
        if (nodeType != null) return (PartyNodeTypeOfbiz) PartyNodeTypeOfbiz.valueOf(nodeType);
        else return null;
    }
}
