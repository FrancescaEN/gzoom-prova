package it.mapsgroup.gzoom.ofbiz.util;

import java.util.Collection;
import java.util.Map;

/**
 * 13/03/13
 *
 * @author Andrea Fossi
 */
public class MapDifference {
    public static <K, V> com.google.common.collect.MapDifference<K, V> difference(
            Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right) {
        for (K key : left.keySet()) {
            V leftValue = left.get(key);
            V rightValue = right.get(key);
            if ((leftValue != null && leftValue instanceof Collection &&
                    ((Collection) leftValue).size() > 0 && ((Collection) ((Collection) leftValue).iterator().next() instanceof Map))
            && (rightValue != null && rightValue instanceof Collection &&
                    ((Collection) rightValue).size() > 0 && ((Collection) ((Collection) rightValue).iterator().next() instanceof Map))){

            }
        }
        return null;
    }

}
