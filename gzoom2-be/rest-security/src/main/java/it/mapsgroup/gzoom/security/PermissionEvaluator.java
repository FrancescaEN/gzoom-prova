package it.mapsgroup.gzoom.security;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 */
public class PermissionEvaluator {
    private static final Logger LOG = getLogger(PermissionEvaluator.class);




  /*  public static boolean hasPermission(UserLogin principal, String targetDomainObject, String permission) {
        SecurityDomainObject object = SecurityDomainObject.fromString(targetDomainObject);
        if (object != null)
            return hasPermission(principal, object, permission);
        else {
            LOG.error("Object[{}] not authorized", targetDomainObject);
            return false;
        }
    }

    public static boolean isAdmin(UserLogin principal) {
        for (SecurityRolePermission userPerm : principal.getAclPermissions()) {
            if (userPerm.getBasic() && StringUtils.equals(userPerm.getEntityId(), SecurityDomainObject.ADMIN.getId())) {
                LOG.debug("Authorized [{}]-[{}]", principal.getUsername(), SecurityDomainObject.ADMIN.getId());
                return true;
            }
        }
        return false;
    }



    public static boolean hasPermission(UserLogin principal, SecurityDomainObject object, String permission) {
        for (SecurityRolePermission userPerm : principal.getAclPermissions()) {
            if (StringUtils.equals(userPerm.getEntityId(), object.getId())) {
                if (object.isBasic() && userPerm.getBasic() && (permission == null || "BASIC".equals(permission))) {
                    LOG.debug("Authorized [{}]-[{}]-[{}]", principal.getUsername(), object, permission);
                    return true;
                }
                if (object.isRead() && userPerm.getRead() && "READ".equals(permission)) {
                    LOG.debug("Authorized [{}]-[{}]-[{}]", principal.getUsername(), object, permission);
                    return true;
                }
                if (object.isWrite() && userPerm.getWrite() && "WRITE".equals(permission)) {
                    LOG.debug("Authorized [{}]-[{}]-[{}]", principal.getUsername(), object, permission);
                    return true;
                }
                if (object.isDelete() && userPerm.getDelete() && "DELETE".equals(permission)) {
                    LOG.debug("Authorized [{}]-[{}]-[{}]", principal.getUsername(), object, permission);
                    return true;
                }
                if (object.isValidate() && userPerm.getValidate() && "VALIDATE".equals(permission)) {
                    LOG.debug("Authorized [{}]-[{}]-[{}]", principal.getUsername(), object, permission);
                    return true;
                }
            }
        }
        LOG.debug("Authorized denied [{}]-[{}]-[{}]", principal.getUsername(), object, permission);
        return false;
    }*/




    /*    *//**
     * If user has at least one {@link it.memelabs.smartnebula.lmm.persistence.main.dto.SecurityRole}
     * with given permission
     *
     * @param principal
     * @param permission
     * @return
     *//*
    public static boolean hasAnyPermission(UserLogin principal, String permission) {
        for (SecurityRolePermission userPerm : principal.getAclPermissions()) {
            SecurityObjectPermission object = SecurityObjectPermission.fromString(userPerm.getEntityId());
            if (object.isRead() && userPerm.getRead() && "READ".equals(permission)) {
                LOG.debug("Authorized [{}]-[{}]-[{}]", principal.getUsername(), object, permission);
                return true;
            }
            if (object.isWrite() && userPerm.getWrite() && "WRITE".equals(permission)) {
                LOG.debug("Authorized [{}]-[{}]-[{}]", principal.getUsername(), object, permission);
                return true;
            }
            if (object.isDelete() && userPerm.getDelete() && "DELETE".equals(permission)) {
                LOG.debug("Authorized [{}]-[{}]-[{}]", principal.getUsername(), object, permission);
                return true;
            }
            if (object.isValidate() && userPerm.getValidate() && "VALIDATE".equals(permission)) {
                LOG.debug("Authorized [{}]-[{}]-[{}]", principal.getUsername(), object, permission);
                return true;
            }
        }
        return false;
    }*/
}
