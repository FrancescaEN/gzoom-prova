package it.mapsgroup.gzoom.persistence.common;

import com.google.common.collect.ImmutableMap;

/**
 * @author Andrea Fossi.
 */
public class SequenceEntity {
    private static final ImmutableMap<Class, String> map;

    static {
        ImmutableMap.Builder<Class, String> builder = new ImmutableMap.Builder<>();
        builder.put(Object.class, "Party");//todo add party class
        map = builder.build();
    }

    public static String sequenceEntity(Class clazz) {
        return map.get(clazz);
    }
}
