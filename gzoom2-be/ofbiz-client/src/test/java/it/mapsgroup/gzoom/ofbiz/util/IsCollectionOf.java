package it.mapsgroup.gzoom.ofbiz.util;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 18/02/14
 *
 * @author Andrea Fossi
 */
public class IsCollectionOf<T> extends TypeSafeMatcher<Collection<T>> {
    private final Collection<T> expected;

    public  IsCollectionOf(Collection<T> expected) {
        this.expected = expected;
    }

    public boolean matchesSafely(Collection<T> given) {
        List<T> tmp = new ArrayList<T>(expected);
        for (T t : given) {
            if (!tmp.remove(t)) {
                return false;
            }
        }
        return tmp.isEmpty();
    }

    // describeTo here
    public static <T> Matcher<Collection<T>> isCollectionOfOfItems(T... items) {
        return new IsCollectionOf<T>(Arrays.asList(items));
    }
    public static <T> Matcher<Collection<T>> isCollectionOfOfItems(Collection<T> items) {
        return new IsCollectionOf<T>(items);
    }


    @Override
    public void describeTo(Description description) {
          description.appendValueList("[",",","]",expected);
    }
}
