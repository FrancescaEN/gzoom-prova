package it.memelabs.gn.services.node;

/**
 * 26/04/13
 *
 * @author Andrea Fossi
 */
public enum RelationshipTypeOfbiz {
    GN_CONTEXT_USER(PartyRoleOfbiz.GN_CONTEXT, PartyRoleOfbiz.GN_USER),// Context-User
    GN_CONTEXT_NODE(PartyRoleOfbiz.GN_CONTEXT, PartyRoleOfbiz.GN_NODE_CONTEXT), //Context-Node
    GN_CONTEXT_CBASE(PartyRoleOfbiz.GN_CONTEXT, PartyRoleOfbiz.GN_COMPANY_BASE), //Context-CompanyBase
    GN_OWNS(null, null), //Node-owns-Node
    GN_BELONGS_TO(null, null), //Node-belongsTo-Node
    GN_CBASE_DELEGATES(PartyRoleOfbiz.GN_COMPANY_BASE, PartyRoleOfbiz.GN_COMPANY_BASE), //CompanyBase-delegates-CompanyBase
    GN_PROPAGATES_TO(PartyRoleOfbiz.GN_PROPAGATION_NODE, PartyRoleOfbiz.GN_PROPAGATION_NODE), //Node-propagatesTo-Node
    GN_RECEIVES_FROM(PartyRoleOfbiz.GN_PROPAGATION_NODE, PartyRoleOfbiz.GN_PROPAGATION_NODE), //Node-receivesFrom-Node
    GN_PUBLISHES_TO_ROOT(PartyRoleOfbiz.GN_COMPANY, PartyRoleOfbiz.GN_ROOT), //Node-publishesTo-Root

    EMPLOYMENT(null, null);// user is employed

    private PartyRoleOfbiz partyRoleFrom;
    private PartyRoleOfbiz partyRoleTo;


    private RelationshipTypeOfbiz(PartyRoleOfbiz partyRoleFrom, PartyRoleOfbiz partyRoleTo) {
        this.partyRoleTo = partyRoleTo;
        this.partyRoleFrom = partyRoleFrom;
    }

    public PartyRoleOfbiz getPartyRoleFrom() {
        return partyRoleFrom;
    }

    public PartyRoleOfbiz getPartyRoleTo() {
        return partyRoleTo;
    }

    public boolean is(Object partyRelationshipTypeId) {
        if (partyRelationshipTypeId == null)
            return false;
        else
            return this.name().equals(partyRelationshipTypeId.toString());
    }

}

