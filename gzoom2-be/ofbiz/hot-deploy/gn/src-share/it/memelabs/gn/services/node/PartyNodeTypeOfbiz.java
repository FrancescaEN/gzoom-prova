package it.memelabs.gn.services.node;

/**
 * 07/03/13
 *
 * @author Andrea Fossi
 */
public enum PartyNodeTypeOfbiz {
    COMPANY_BASE,
    COMPANY,
    ISSUER,
    ISSUER_BASE,
    ORGANIZATION,
    ROOT,
    STUB,
    PRIVATE_COMPANY_BASE,//propagation company base
    PRIVATE_COMPANY,    //propagation company
    INVITED_COMPANY;    //potential company

    public static boolean isACompany(Object nodeType) {
        return COMPANY.name().equals(nodeType);
    }

    public static boolean isACompanyBase(Object nodeType) {
        return COMPANY_BASE.name().equals(nodeType);
    }

    public static boolean isAPrivateCompany(Object nodeType) {
        return PRIVATE_COMPANY.name().equals(nodeType);
    }

    public static boolean isAPrivateCompanyBase(Object nodeType) {
        return PRIVATE_COMPANY_BASE.name().equals(nodeType);
    }

    public static boolean isAInvitedCompany(Object nodeType) {

        return INVITED_COMPANY.name().equals(nodeType);
    }

    public static boolean isAOrganizzationNode(Object nodeType) {
        return ORGANIZATION.name().equals(nodeType);
    }

  /*  public boolean is(Object in){
        //this.name().in
    }*/
}
