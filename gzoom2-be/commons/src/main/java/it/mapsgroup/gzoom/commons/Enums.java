package it.mapsgroup.gzoom.commons;

import com.google.common.collect.ImmutableMap;
import it.mapsgroup.gzoom.commons.collect.Tuple2;

import java.util.Collection;
import java.util.EnumSet;

import static com.google.common.collect.ImmutableBiMap.*;

/**
 * Utilities for enumerations.
 *
 * @author Fabio Strozzi
 */
public class Enums {
    private Enums() {}

    /**
     * Retrieves the simple class name of the given enumerator and its constant name.
     *
     * @param enu Any enumerator
     * @return the simple class name of the given enumerator and its constant name.
     */
    public static Tuple2<String, String> classAndNameOf(Enum<?> enu) {
        String clazz = enu.getDeclaringClass().getSimpleName().toLowerCase();
        String name = enu.toString().toLowerCase();
        return Tuple2.of(clazz, name);
    }

    /**
     * Retrieves the simple, lowercase name of the given enumerator.
     *
     * @param enu Any enumerator
     * @return the simple, lowercase name of the given enumerator.
     */
    public static String nameOf(Enum<?> enu) {
        return enu.toString().toLowerCase();
    }

    /**
     * Creates an immutable conversion map of the specified class of enumerator.
     *
     * @param clazz The enumerator class
     * @param <E>   The enumerator type
     * @return An immutable map suitable for parsing string values.
     */
    public static <E extends Enum<E>> ImmutableMap<String, E> parseMap(Class<E> clazz) {
        ImmutableMap.Builder<String, E> b = builder();
        EnumSet<E> all = EnumSet.allOf(clazz);
        for (E s : all) {
            b.put(s.toString(), s);
        }
        return b.build();
    }

    /**
     * Tells whether the specified collection of enumerators is complete, that is no other elements exist which are not
     * in the collection already.
     *
     * @param coll The collection of enumerator
     * @param <E>  The type of the enumerators
     * @return True if the specified collection of enumerators is complete
     */
    public static <E extends Enum<E>> boolean isComplete(Collection<E> coll) {
        if (coll.isEmpty())
            return false;
        Enum<E> e = coll.iterator().next();
        EnumSet<E> all = EnumSet.allOf(e.getDeclaringClass());
        return coll.containsAll(all);
    }

}