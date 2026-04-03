package it.mapsgroup.gzoom.commons.collect;

import java.util.function.Function;

/**
 * A tuple of 2 elements.
 *
 * @author Fabio Strozzi
 */
public final class Tuple2<S, T> {
    private final S first;
    private final T second;

    public Tuple2(S first, T second) {
        this.first = first;
        this.second = second;
    }

    public S first() {
        return first;
    }

    public T second() {
        return second;
    }

    /**
     * Convenient static method that creates a new {@link Tuple2} instance..
     *
     * @param s   The first element
     * @param t   The second element
     * @param <M> The type of the first element
     * @param <N> The type of the second element
     * @return A tuple of two elements.
     */
    public static <N, M> Tuple2<N, M> of(N s, M t) {
        return new Tuple2<>(s, t);
    }

    public static <N, M> Function<Tuple2<N, M>, N> firstOf() {
        return t -> t.first;
    }

    public static <N, M> Function<Tuple2<N, M>, M> secondOf() {
        return t -> t.second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple2<?, ?> tuple2 = (Tuple2) o;

        if (first != null ? !first.equals(tuple2.first) : tuple2.first != null) return false;
        if (second != null ? !second.equals(tuple2.second) : tuple2.second != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }
}