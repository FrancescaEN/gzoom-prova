package it.memelabs.gn.services.authorization;

/**
 * 22/03/13
 *
 * @author Andrea Fossi
 */
public enum AgreementTermTypeOfbiz {
    //TermType entity
    //contraints only
    GN_CNS_WASTE_ENTRY(false),
    GN_CNS_WASTE_STOCK(false),
    GN_CNS_WASTE_TRTMNT(false),
    GN_CNS_SUBJECT_KIND(false),
    GN_CNS_SUBJ_COMPANY(true),
    //on both
    GN_CNS_OPERATION(true),
    GN_CNS_VEHICLE_REG_N(true),
    GN_CNS_PACKAGING(true),
    GN_CNS_WASTE(true),
    //filter only
    GN_CNS_HOLDER(true),
    GN_CNS_SUBJ_ROLE(true),
    GN_CNS_AUTH_TYPE(true),
    // slicing filter only
    GN_CNS_CAT_CLASS(true);

    public boolean is(Object termType) {
        if (termType == null)
            return false;
        else
            return this.name().equals(termType.toString());
    }

    private final boolean oneForItem;

    /**
     * @param oneForItem
     */
    private AgreementTermTypeOfbiz(boolean oneForItem) {
        this.oneForItem = oneForItem;
    }

    /**
     * Only one of term of this type is allowed for each authorizationItem
     *
     * @return
     */
    public boolean isOneForItem() {
        return oneForItem;
    }
}
