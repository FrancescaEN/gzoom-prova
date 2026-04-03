package it.mapsgroup.gzoom.commons.error;
/**
 * A supplier that can throw exceptions.
 *
 * @author Fabio G. Strozzi
 */
@FunctionalInterface
public interface TrySupplier<T, X extends Exception> {

    /**
     * Retrieves a value of type {@code T} or throws an exception of type {@code X}.
     *
     * @return A value of of type {@code T} if everything goes fine
     * @throws X The exception thrown if something goes wrong
     */
    T get() throws X;
}
