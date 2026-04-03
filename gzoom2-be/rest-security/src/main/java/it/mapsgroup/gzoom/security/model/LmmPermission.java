package it.mapsgroup.gzoom.security.model;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Sets.newEnumSet;
import static it.mapsgroup.gzoom.commons.Enums.parseMap;

/**
 * Bond permissions.
 * <p>
 * Permissions are always immutable and cannot be changed. When adding multiple permissions, a new
 * permission is always created anew.
 *
 * @author Fabio Strozzi
 * @author Andrea Fossi
 */
@Deprecated //will be remove asap
public enum LmmPermission {
    ROLE_NONE(0), // 0
    ROLE_ADMIN(1), // 1
    ROLE_GENERIC_READER(1 << 1), // 2
    ROLE_GENERIC_WRITER(1 << 2), // 4

    ROLE_VALIDATE_CONTRACT(1 << 3),//8
    ROLE_VALIDATE_ANTIMAFIA_PROCESS(1 << 4),//16
    ROLE_AUDITING_READER(1 << 5)//32

    // @Deprecated
    // ROLE_DOC_PUBLISH(1 << 5),//32
    // @Deprecated
    // ROLE_COMPANY_VALIDATE(1 << 6),//64
    // @Deprecated
    // ROLE_COMPANY_WRITE(1 << 7),//128
    // @Deprecated
    // ROLE_COMPANY_BASE_WRITE(1 << 8),//256
    // @Deprecated
    // ROLE_DOC_PUBLISHED_DELETE(1 << 9),//512
    // @Deprecated
    // ROLE_DOC_ARCHIVE(1 << 10),//1024

    // @Deprecated
    // ROLE_AUDITING(1 << 11)//2048
    ;

    private static final ImmutableMap<String, LmmPermission> STR2PERM = parseMap(LmmPermission.class);

    private int mask;

    LmmPermission(int mask) {
        this.mask = mask;
    }

    /**
     * Retrieves the bit mask of this permission.
     *
     * @return The bit mask
     */
    public int mask() {
        return mask;
    }

    /**
     * Retrieves the permission given its descriptive string.
     *
     * @param perm The permissione string
     * @return The Bond permission
     */
    public static LmmPermission fromString(String perm) {
        return STR2PERM.get(perm);
    }

    /**
     * Creates a set of {@link LmmPermission} permissions from a list of comma-separated string values.
     *
     * @param permList The string of comma-separated permissions.
     * @return A set of {@link LmmPermission} permissions.
     */
    public static Set<LmmPermission> fromList(String permList) {
        if (StringUtils.isEmpty(permList)) return Collections.emptySet();
        Iterable<String> values = Splitter.on(',').omitEmptyStrings().trimResults().split(permList);
        Iterable<LmmPermission> perms = transform(values, LmmPermission::fromString);
        perms = filter(perms, v -> v != null);
        return newEnumSet(perms, LmmPermission.class);
    }

    /**
     * Creates a bit mask of the given list of permissions.
     *
     * @param perms An iterable of permissions
     * @return An bit mask of all the permissions.
     */
    public static int maskAll(Iterable<LmmPermission> perms) {
        int m = 0;
        for (LmmPermission p : perms)
            m |= p.mask;
        return m;
    }

    /**
     * Creates list of permissions from given mask.
     *
     * @param mask bit of permissions
     * @return list of permissions.
     */
    public static List<LmmPermission> fromMask(int mask) {
        List<LmmPermission> ret = new ArrayList<>();
        for (LmmPermission p : LmmPermission.values())
            if ((mask & p.mask()) != 0) ret.add(p);
        return ret;
    }

    public static String toString(List<LmmPermission> permissions) {
        StringBuilder sb = new StringBuilder();
        permissions.forEach(p -> sb.append(p.name()).append(","));
        if (sb.length() > 0) return sb.substring(0, sb.length() - 1);
        else return ROLE_NONE.name();
    }
}
