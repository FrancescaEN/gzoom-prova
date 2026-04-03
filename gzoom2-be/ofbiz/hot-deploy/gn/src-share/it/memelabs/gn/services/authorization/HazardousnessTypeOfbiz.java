package it.memelabs.gn.services.authorization;

/**
 * 26/03/13
 *
 * @author Andrea Fossi
 */
public enum HazardousnessTypeOfbiz {
    GN_HAZARDOUS_ANY,
    GN_HAZARDOUS,
    GN_NOT_HAZARDOUS;

    public boolean is(Object termType) {
        if (termType == null)
            return false;
        else
            return this.name().equals(termType.toString());
    }
}
