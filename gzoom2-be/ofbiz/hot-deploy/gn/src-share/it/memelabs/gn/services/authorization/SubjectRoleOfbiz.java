package it.memelabs.gn.services.authorization;

/**
 * 25/03/13
 *
 * @author Andrea Fossi
 */
public enum SubjectRoleOfbiz {
    //EnumerationType Authorizaion holder role GN_AUTH_HOLDER_ROLE
    GN_PRODUCER,
    GN_CARRIER,
    GN_RECIPIENT,
    GN_BROKER;

    public boolean is(Object termType) {
        return this.name().equals(termType.toString());
    }

}
