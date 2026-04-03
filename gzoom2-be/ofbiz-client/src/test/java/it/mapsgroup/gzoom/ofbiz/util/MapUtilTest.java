package it.mapsgroup.gzoom.ofbiz.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * 08/03/13
 *
 * @author Andrea Fossi
 */
public class MapUtilTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testGetValue_List() {
        Map map = MapUtil.toMap("first", MapUtil.toMap("second", Arrays.asList(MapUtil.toMap("third", "_third"),"www")));
        assertThat("error", MapUtil.getValue(map, "first.second[0].third", String.class), is("_third"));
        assertThat("error", MapUtil.getValue(map, "first.second[1]", String.class), is("www"));
        assertThat("error", (String) MapUtil.getValue(map, "first.second[0].third"), is("_third"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetValue_Array() {
        Map map = MapUtil.toMap("first", MapUtil.toMap("second", new Object[]{MapUtil.toMap("third", "_third"), "www"}));
        assertThat("error", MapUtil.getValue(map, "first.second[0].third", String.class), is("_third"));
        assertThat("error", MapUtil.getValue(map, "first.second[1]", String.class), is("www"));
        assertThat("error", (String) MapUtil.getValue(map, "first.second[0].third"), is("_third"));;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetValue_Set() {
        Map map = MapUtil.toMap("first", MapUtil.toMap("second", new HashSet(Arrays.asList(MapUtil.toMap("third", "_third"), "www"))));
        assertThat("error", MapUtil.getValue(map, "first.second[0].third", String.class), is("_third"));
        assertThat("error", MapUtil.getValue(map, "first.second[1]", String.class), is("www"));
        assertThat("error", (String) MapUtil.getValue(map, "first.second[0].third"), is("_third"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSetValue_() {
        Map map = MapUtil.toMap("first", MapUtil.toMap("second", new HashSet(Arrays.asList(MapUtil.toMap("third", "_third"), "www"))));
        assertThat("error", MapUtil.getValue(map, "first.second[0].third", String.class), is("_third"));
        assertThat("error", MapUtil.getValue(map, "first.second[1]", String.class), is("www"));
        assertThat("error", (String) MapUtil.getValue(map, "first.second[0].third"), is("_third"));
        MapUtil.setValue(map,"first.second[0].third","_third2");
        assertThat("error", (String) MapUtil.getValue(map, "first.second[0].third"), is("_third2"));
    }
}
