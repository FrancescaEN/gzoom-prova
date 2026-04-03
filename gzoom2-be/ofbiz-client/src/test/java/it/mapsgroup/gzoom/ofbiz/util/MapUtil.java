package it.mapsgroup.gzoom.ofbiz.util;

import com.google.common.base.Predicate;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 29/01/13
 *
 * @author Andrea Fossi
 */
public class MapUtil {
    /**
     * Computes the difference between two maps
     * Null values are ignored.
     * See {@link Maps#difference(java.util.Map, java.util.Map)}
     *
     * @param left  the map to treat as the "left" map for purposes of comparison
     * @param right the map to treat as the "right" map for purposes of comparison
     * @return the difference between the two maps
     */
    public static MapDifference<String, Object> difference(Map<String, Object> left, Map<String, Object> right) {
        Map<String, Object> _right = filterEmptyValue(right);
        Map<String, Object> _left = filterEmptyValue(left);
        MapDifference<String, Object> nodeDiff = Maps.difference(_left, _right);
        return nodeDiff;
    }

    /**
     * Remove map Entry with null value
     *
     * @param input map
     * @return
     */
    public static Map<String, Object> filterEmptyValue(Map<String, Object> input) {
        Predicate<Map.Entry<String, Object>> predicate1 = new Predicate<Map.Entry<String, Object>>() {
            @Override
            public boolean apply(Map.Entry<String, Object> input) {
                if (null != input.getValue() && !"".equals(input)) //satisfy your filter
                    return true;
                else
                    return false;
            }
        };
        return Maps.filterEntries(input, predicate1);
    }

    /**
     * * Remove map Entry with value that is an instance of filterClass
     *
     * @param input       input map
     * @param filterClass entry value to remove
     * @return
     */
    public static Map<String, Object> filterValue(Map<String, Object> input, final Class<? extends Object> filterClass) {
        Predicate<Map.Entry<String, Object>> predicate1 = new Predicate<Map.Entry<String, Object>>() {
            @Override
            public boolean apply(Map.Entry<String, Object> input) {
                if (null != input.getValue() && filterClass.isAssignableFrom(input.getValue().getClass()))
                    return false;
                else
                    return true;
            }
        };
        return Maps.filterEntries(input, predicate1);
    }


    /**
     * Create a map from passed nameX, valueX parameters
     *
     * @return The resulting Map
     */
    public static <V, V1 extends V> Map<String, V> toMap(String name1, V1 value1) {
        Map<String, V> map = new HashMap<String, V>();
        map.put(name1, value1);
        return map;
    }

    /**
     * Create a map from passed nameX, valueX parameters
     *
     * @return The resulting Map
     */
    public static <V, V1 extends V, V2 extends V> Map<String, V> toMap(String name1, V1 value1, String name2, V2 value2) {
        Map<String, V> map = new HashMap<String, V>();
        map.put(name1, value1);
        map.put(name2, value2);
        return map;

    }

    /**
     * Create a map from passed nameX, valueX parameters
     *
     * @return The resulting Map
     */
    public static <V, V1 extends V, V2 extends V, V3 extends V> Map<String, V> toMap(String name1, V1 value1, String name2, V2 value2, String name3, V3 value3) {
        Map<String, V> map = new HashMap<String, V>();
        map.put(name1, value1);
        map.put(name2, value2);
        map.put(name3, value3);
        return map;
    }

    /**
     * Create a map from passed nameX, valueX parameters
     *
     * @return The resulting Map
     */
    public static <V, V1 extends V, V2 extends V, V3 extends V, V4 extends V> Map<String, V> toMap(String name1, V1 value1, String name2, V2 value2, String name3, V3 value3, String name4, V4 value4) {
        Map<String, V> map = new HashMap<String, V>();
        map.put(name1, value1);
        map.put(name2, value2);
        map.put(name3, value3);
        map.put(name4, value4);

        return map;
    }

    /**
     * Create a map from passed nameX, valueX parameters
     *
     * @return The resulting Map
     */
    public static <V, V1 extends V, V2 extends V, V3 extends V, V4 extends V, V5 extends V> Map<String, V> toMap(String name1, V1 value1, String name2, V2 value2, String name3, V3 value3, String name4, V4 value4, String name5, V5 value5) {
        Map<String, V> map = new HashMap<String, V>();
        map.put(name1, value1);
        map.put(name2, value2);
        map.put(name3, value3);
        map.put(name4, value4);
        map.put(name5, value5);
        return map;
    }

    /**
     * Create a map from passed nameX, valueX parameters
     *
     * @return The resulting Map
     */
    public static <V, V1 extends V, V2 extends V, V3 extends V, V4 extends V, V5 extends V, V6 extends V> Map<String, V> toMap(String name1, V1 value1, String name2, V2 value2, String name3, V3 value3, String name4, V4 value4, String name5, V5 value5, String name6, V6 value6) {
        Map<String, V> map = new HashMap<String, V>();
        map.put(name1, value1);
        map.put(name2, value2);
        map.put(name3, value3);
        map.put(name4, value4);
        map.put(name5, value5);
        map.put(name6, value6);
        return map;
    }

    public static <K, V> Map<V, Map<K, V>> listToMap(List<Map<K, V>> list, String keyField) {
        if (list == null || list.isEmpty()) return Collections.emptyMap();

        Map<V, Map<K, V>> ret = new HashMap<V, Map<K, V>>(list.size());
        for (Map<K, V> item : list) {
            V key = item.get(keyField);
            if (key == null) throw new RuntimeException("key values is null");
            ret.put(key, item);
        }
        return ret;
    }

    public static <K, V, Z> Map<Z, Map<K, V>> listToMap(List<Map<K, V>> list, String keyField, Class<Z> clazz) {
        if (list == null || list.isEmpty()) return Collections.emptyMap();

        Map<Z, Map<K, V>> ret = new HashMap<Z, Map<K, V>>(list.size());
        for (Map<K, V> item : list) {
            Z key = null;
            if (clazz.equals(String.class)) key = (Z) item.get(keyField).toString();
            else key = (Z) item.get(keyField);
            if (key == null) throw new RuntimeException("key values is null");
            ret.put(key, item);
        }
        return ret;
    }

    public static Object getValue(Map<String, ?> source, String path) {
        return getValue(source, path, Object.class);
    }

    /**
     * Like Apache BeanUtils extract an Object from a Map of Map o Collection....
     * Path example:
     * tob[2].name extract value tob from a map
     *
     * @param source
     * @param path
     * @param clazz
     * @param <T>    return class type
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Map<String, ?> source, String path, Class<T> clazz) {
        String[] keys = path.split("\\.");
        Pattern pattern = Pattern.compile("(\\w*\\[)(\\d*)(\\])");
        Object value = source;
        for (String key : keys) {
            if (value instanceof Map) {
                if (pattern.matcher(key).find()) {
                    String _key = key.substring(0, key.indexOf("["));
                    int index = Integer.valueOf(pattern.matcher(key).replaceFirst("$2"));
                    Object collection = ((Map<String, Object>) value).get(_key);
                    if (collection instanceof List)
                        value = ((List<Object>) collection).get(index);
                    else if (collection instanceof Object[])
                        value = ((Object[]) collection)[index];
                    else if (collection instanceof Set) {
                        Iterator<Object> it = ((Set) collection).iterator();
                        for (int i = 0; i < index; i++) it.next();
                        value = it.next();
                    }
                } else {
                    value = ((Map<String, Object>) value).get(key);
                }

            } else throw new ClassCastException("Object[" + key + "] is not a map.");
        }

        if (clazz.isAssignableFrom(value.getClass())) {
            return (T) value;
        } else {
            throw new ClassCastException("Return type is not valid.");
        }
    }

    /**
     * Set value on map path
     *
     * @param source
     * @param path
     * @param value
     * @param <T>
     */
    public static <T> void setValue(Map<String, T> source, String path, T value) {
        Map<String, T> target = source;
        String key = path;
        if (path.lastIndexOf('.') > 0) {
            String subPath = path.substring(0, path.lastIndexOf('.'));
            key = path.substring(path.lastIndexOf('.') + 1);
            target = getValue(source, subPath, Map.class);
        }
        target.put(key, value);
    }

    /**
     * Copy a list of maps in another; for each map, only entries with key in keysToCopy will be copied.
     * For details see {@link #copyKeys(java.util.Map, Object[])} method.
     *
     * @param src
     * @param keysToCopy
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V> List<Map<T, V>> copyKeys(List<Map<T, V>> src, T... keysToCopy) {
        List<Map<T, V>> ret = new ArrayList<Map<T, V>>(src.size());
        for (Map<T, V> map : src) {
            ret.add(copyKeys(map, keysToCopy));
        }
        return ret;
    }

    /**
     * Copy map in another map. Only entries with key in keysToCopy will be copied.
     *
     * @param src
     * @param keysToCopy
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V> Map<T, V> copyKeys(Map<T, V> src, final T... keysToCopy) {
        HashMap<T, V> ret = new HashMap<T, V>(src.size());
        for (T key : keysToCopy) {
            if (src.containsKey(key))
                ret.put(key, src.get(key));
        }
        return ret;

        //with guava
        /*final Set<T> keys = new HashSet<T>(Arrays.asList(keysToCopy));
        return new HashMap<T, V>(Maps.filterKeys(src, new Predicate<T>() {
            @Override
            public boolean apply(T input) {
                return keys.contains(input);
            }
        }));*/
    }

    public static String printTree(Object o) {
        StringBuffer sb = new StringBuffer();
        if (o instanceof Map) {
            Map o1 = (Map) o;
            for (Object key : o1.keySet()) {
                sb.append("|\n+").append(key.toString()).append(" ").append(printTree(o1.get(key))).append("\n");
            }
            if (o instanceof List) {

            }

        } else return (o != null) ? o.getClass().getSimpleName() : "unknown";
        return sb.toString();
    }

    /**
     * Example:
     * {@code Map<String, Object> map = (Map<String, Object>) new NodeOfBizClientImpl(config)
     * .findNodeById(MapUtil.toMap("partyId", (Object) "GN_MEMEWASTE_PR"), sessionId, context1).get("partyNode");
     * MapUtil.toJava(map,"companyBase");}
     *
     * @param items
     * @param fieldName
     * @return
     */
    public static String toJava(Map<String, Object> items, String fieldName) {
        StringBuilder sb = new StringBuilder();
        sb.append("Map<String,Object> ").append(fieldName).append("=new HashMap<String,Object>();\n");
        for (String key : items.keySet()) {
            Object o = items.get(key);
            sb.append(fieldName).append(".put(\"").append(key).append("\",");
            if (o == null) sb.append("null);\n");
            else if (o instanceof String) sb.append("\"").append(o).append("\");\n");
            else if (o instanceof Map) {
                sb.append(key).append(");\n");
                sb.append(toJava((Map<String, Object>) o, key));
            } else sb.append("").append(o).append(");\n");
        }
        return sb.toString();
    }

 /*   private static void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + name);
        for (Iterator<TreeNode> iterator = children.iterator(); iterator.hasNext(); ) {
            iterator.next().print(prefix + (isTail ? "    " : "│   "), !iterator.hasNext());
        }
    }*/
}
