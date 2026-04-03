package it.mapsgroup.gzoom.security.model;


import com.google.common.collect.ImmutableMap;

import static it.mapsgroup.gzoom.commons.Enums.parseMap;

/**
 * @author Andrea Fossi.
 *         <p>
 */
public enum SecurityDomainObject {
    ACCIDENT(SecurityPermissionMask.READ, SecurityPermissionMask.WRITE, SecurityPermissionMask.DELETE),//
    ADMIN(SecurityPermissionMask.BASIC),
    ANTIMAFIA_PROCESS(SecurityPermissionMask.READ, SecurityPermissionMask.WRITE, SecurityPermissionMask.DELETE, SecurityPermissionMask.VALIDATE, SecurityPermissionMask.DELETE),
    AUDITING(SecurityPermissionMask.READ),
    COMPANY(SecurityPermissionMask.READ, SecurityPermissionMask.WRITE, SecurityPermissionMask.DELETE),
    COMPANY_ATTACHMENT(SecurityPermissionMask.READ),
    CONSTRUCTION_SITE_LOG(SecurityPermissionMask.READ, SecurityPermissionMask.WRITE, SecurityPermissionMask.DELETE),
    CONTRACT(SecurityPermissionMask.READ, SecurityPermissionMask.WRITE, SecurityPermissionMask.DELETE, SecurityPermissionMask.VALIDATE, SecurityPermissionMask.DELETE),
    EQUIPMENT(SecurityPermissionMask.READ, SecurityPermissionMask.WRITE, SecurityPermissionMask.DELETE),
    EQUIPMENT_EMPLOYMENT(SecurityPermissionMask.READ, SecurityPermissionMask.WRITE, SecurityPermissionMask.DELETE),
    GALLERY(SecurityPermissionMask.READ, SecurityPermissionMask.WRITE, SecurityPermissionMask.DELETE),//
    JOB_ORDER(SecurityPermissionMask.READ, SecurityPermissionMask.WRITE, SecurityPermissionMask.DELETE),
    PERSON(SecurityPermissionMask.READ, SecurityPermissionMask.WRITE, SecurityPermissionMask.DELETE),
    PERSON_EMPLOYMENT(SecurityPermissionMask.READ, SecurityPermissionMask.WRITE, SecurityPermissionMask.DELETE),
    WEEKLY_WORK_LOG(SecurityPermissionMask.READ, SecurityPermissionMask.WRITE, SecurityPermissionMask.DELETE),
    WORK_LOG(SecurityPermissionMask.READ, SecurityPermissionMask.WRITE, SecurityPermissionMask.DELETE),
    TIMESHEET(SecurityPermissionMask.READ, SecurityPermissionMask.WRITE, SecurityPermissionMask.DELETE),;

    /**
     * Enum lists {@link SecurityDomainObject} and {@link SecurityPermissionMask} (allowed permissions).
     * {@link SecurityPermissionMask} are permissions that can be selected by the user
     * {@link SecurityDomainObject} can group one or more {@link SecurityObject}
     *
     * @param ops allowed {@link SecurityPermissionMask}
     */
    SecurityDomainObject(Integer... ops) {
        init(ops);
    }

    private void init(Integer[] ops) {
        for (int op : ops) {
            this.basic |= op == SecurityPermissionMask.BASIC;
            this.read |= op == SecurityPermissionMask.READ;
            this.write |= op == SecurityPermissionMask.WRITE;
            this.delete |= op == SecurityPermissionMask.DELETE;
            this.validate |= op == SecurityPermissionMask.VALIDATE;
            this.mask |= op;
        }
    }

    private boolean basic;
    private boolean read;
    private boolean write;
    private boolean delete;
    private boolean validate;
    private int mask = 0;


    public boolean isRead() {
        return read;
    }

    public boolean isWrite() {
        return write;
    }

    public boolean isDelete() {
        return delete;
    }

    public boolean isValidate() {
        return validate;
    }

    public boolean isBasic() {
        return basic;
    }

    public int getMask() {
        return mask;
    }

    public String getId() {
        return this.name();
    }

    private static final ImmutableMap<String, SecurityDomainObject> STR2PERM = parseMap(SecurityDomainObject.class);

    /**
     * Retrieves the permission given its descriptive string.
     *
     * @param perm The permissione string
     * @return The Bond permission
     */
    public static SecurityDomainObject fromString(String perm) {
        return STR2PERM.get(perm);
    }


}

