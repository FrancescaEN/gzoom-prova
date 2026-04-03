package it.memelabs.gn.services.authorization;

/**
 * 01/01/16
 *
 * @author Elisa Spada
 */
public enum CriteriaLogicOfbiz {
    OR,
    AND;

    public boolean is(Object logic) {
        if (logic == null)
            return false;
        else
            return this.name().equals(logic.toString());
    }
}
