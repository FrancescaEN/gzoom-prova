package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.rest.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Validation utilities.
 *
 * @author Fabio G. Strozzi
 *
 */
//fixme make commons library
public class Validators {
    private static final Logger LOG = getLogger(Validators.class);
    private static final String EMAIL_REGEXP = "^[a-z0-9!#$%&'*+\\/=?^_`{|}~.-]+@[a-z0-9]([a-z0-9-]*[a-z0-9])?(\\.[a-z0-9]([a-z0-9-]*[a-z0-9])?)*$";

    public static void assertIsBefore(Date start, Date end, String reason) {
        if (!start.before(end))
            throw new ValidationException(reason);
    }

    public static void assertIsBeforeOrEqual(Date start, Date end, String reason) {
        if (start.compareTo(end) > 0)
            throw new ValidationException(reason);
    }

    public static void assertIsBefore(Instant start, Instant end, String reason) {
        if (end.isBefore(start))
            throw new ValidationException(reason);
    }

    public static void assertNotNull(Object obj, String reason) {
        if (obj == null)
            throw new ValidationException(reason);
    }

    public static void assertGreaterThan(int min, int value, String reason) {
        if (value <= min)
            throw new ValidationException(reason);
    }

    public static void assertGreaterOrEqualThan(int min, int value, String reason) {
        if (value < min)
            throw new ValidationException(reason);
    }

    public static void assertGreaterThan(long min, long value, String reason) {
        if (value <= min)
            throw new ValidationException(reason);
    }

    public static void assertTrue(boolean value, String reason) {
        if (!value)
            throw new ValidationException(reason);
    }

    public static void assertFalse(boolean value, String reason) {
        if (value)
            throw new ValidationException(reason);
    }

    public static void assertEmpty(List<? extends Object> values, String reason) {
        if (values != null && !values.isEmpty())
            throw new ValidationException(reason);
    }

    public static void assertNotEmpty(List<? extends Object> values, String reason) {
        if (values == null || values.isEmpty())
            throw new ValidationException(reason);
    }

    public static <E extends Enum<E>> E assertIsEnum(Class<E> clazz, String value, String reason) {
        try {
            Method parse = clazz.getMethod("parse", String.class);
            Object res = parse.invoke(null, value);
            if (clazz.isInstance(res))
                // noinspection unchecked
                return (E) res;
        } catch (NoSuchMethodException e) {
            // nothing to do, method simply does not exist
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOG.warn("Attempting to call parse method failed, falling back to iteration [class={}]", clazz);
        }

        EnumSet<E> all = EnumSet.allOf(clazz);
        for (E e : all) {
            if (e.toString().equalsIgnoreCase(value))
                return e;
        }

        throw new ValidationException(reason);
    }

    /**
     * Makes sure two objects are not the same instance.
     * <p>
     * BEWARE: test is done by reference equality, not structural!
     * </p>
     *
     * @param obj1   An object
     * @param obj2   Another object
     * @param reason Reason of failure
     */
    public static void assertNotSame(Object obj1, Object obj2, String reason) {
        if (obj1 == obj2)
            throw new ValidationException(reason);
    }


    public static void assertIsEmail(String email, String reason) {
        if (email == null || !email.matches(EMAIL_REGEXP))
            throw new ValidationException(reason);
    }

    public static void assertNotBlank(String val, String reason) {
        if (StringUtils.isBlank(val))
            throw new ValidationException(reason);
    }

    public static void assertSame(Object obj1, Object obj2, String reason) {
        if (obj1 != obj2)
            throw new ValidationException(reason);
    }



}
