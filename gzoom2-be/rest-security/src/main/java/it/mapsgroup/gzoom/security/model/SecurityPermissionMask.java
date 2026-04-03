package it.mapsgroup.gzoom.security.model;

/**
 * @author Andrea Fossi.
 */
public class SecurityPermissionMask {
    public static final int BASIC = 1;
    public static final int READ = 1 << 1;
    public static final int WRITE = 1 << 2;
    public static final int DELETE = 1 << 3;
    public static final int VALIDATE = 1 << 4;

}
