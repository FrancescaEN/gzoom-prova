package it.mapsgroup.gzoom.ofbiz.util;

import java.util.*;

/**
 * 04/06/13
 *
 * @author Andrea Fossi
 */
public class CloneUtil {
    /**
     * Generic deep clone method
     *
     * @param src object to clone
     * @return cloned object
     */
    public static Object deepClone(Object src) {
        if (src == null) return null;
        else {
            if (src instanceof Map) {
                Map<Object, Object> dest = new HashMap<Object, Object>(((Map) src).size());
                @SuppressWarnings("unchecked")
                Set<Map.Entry> set = ((Map) src).entrySet();
                for (Map.Entry entry : set) {
                    dest.put(deepClone(entry.getKey()), (deepClone(entry.getValue())));
                }
                return dest;
            } else if (src instanceof List) {
                List<Object> dest = new ArrayList<Object>(((List) src).size());
                for (Object entry : (List) src) {
                    dest.add(deepClone(entry));
                }
                return dest;
            } else if (src instanceof Set) {
                Set<Object> dest = new HashSet<Object>(((Set) src).size());
                for (Object entry : (Set) src) {
                    dest.add(deepClone(entry));
                }
                return dest;
            } else if (src instanceof Cloneable) {
                try {
                    return src.getClass().getMethod("clone").invoke(src);
                } catch (Throwable e) {
                    throw new RuntimeException("cannot clone object: " + src.toString(), e);
                }
            } else return src;
        }
    }
}