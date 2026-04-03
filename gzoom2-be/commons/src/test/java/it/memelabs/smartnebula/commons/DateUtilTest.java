package it.memelabs.smartnebula.commons;

import it.memelabs.smartnebula.commons.DateUtil;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author Andrea Fossi.
 */
public class DateUtilTest {

    /**
     * TODO @see http://planetcalc.com/1252/
     */

    @Test
    public void testGetWeekNumber() {
        //week 1/2016  04-gen-2016 -> 10-gen-2016
        assertEquals(1, DateUtil.getWeekNumber(dateFromString("10/01/2016")));
        assertEquals(2016, DateUtil.getYear(dateFromString("10/01/2016")));
        //week 53/2015  28-dec-2015 -> 03-gen-2016
        assertEquals(53, DateUtil.getWeekNumber(dateFromString("01/01/2016")));
        assertEquals(2016, DateUtil.getYear(dateFromString("01/01/2016")));
        //week 20/2016  16-may-2016 -> 22-may-2016
        assertEquals(19, DateUtil.getWeekNumber(dateFromString("15/05/2016")));
        assertEquals(20, DateUtil.getWeekNumber(dateFromString("16/05/2016")));
        assertEquals(20, DateUtil.getWeekNumber(dateFromString("22/05/2016")));
        assertEquals(21, DateUtil.getWeekNumber(dateFromString("23/05/2016")));
        assertEquals(2016, DateUtil.getYear(dateFromString("22/05/2016")));
    }

    @Test
    public void testGetWeekDate() {
        //week 1/2016  04-gen-2016 -> 10-gen-2016
        assertEquals("04/01/2016", dateToString(DateUtil.getDatesOfCalendarWeek(1, 2016).first()));
        assertEquals("10/01/2016", dateToString(DateUtil.getDatesOfCalendarWeek(1, 2016).second()));


        //week 53/2015  28-dec-2015 -> 03-gen-2016
        assertEquals("28/12/2015", dateToString(DateUtil.getDatesOfCalendarWeek(53, 2015).first()));
        assertEquals("03/01/2016", dateToString(DateUtil.getDatesOfCalendarWeek(53, 2015).second()));


        //week 20/2016  16-may-2016 -> 22-may-2016
        assertEquals("15/05/2016", dateToString(DateUtil.getDatesOfCalendarWeek(19, 2016).second()));
        assertEquals("16/05/2016", dateToString(DateUtil.getDatesOfCalendarWeek(20, 2016).first()));
        assertEquals("22/05/2016", dateToString(DateUtil.getDatesOfCalendarWeek(20, 2016).second()));
        assertEquals("23/05/2016", dateToString(DateUtil.getDatesOfCalendarWeek(21, 2016).first()));
    }

    @Test
    public void testGetEndOfDay() {
        Calendar c = Calendar.getInstance();
        c.set(2016, Calendar.FEBRUARY, 10, 10, 22, 66);
        Date date = c.getTime();
        assertEquals(dateToString(date) + " 23:59:59.999", dateToString(DateUtil.getEndOfDay(date), "dd/MM/yyyy HH:mm:ss.S"));
    }

    @Test
    public void testGetStartOfDay() {
        Calendar c = Calendar.getInstance();
        c.set(2016, Calendar.FEBRUARY, 10, 10, 22, 66);
        Date date = c.getTime();
        assertEquals(dateToString(date) + " 00:00:00.000", dateToString(DateUtil.getStartOfDay(date), "dd/MM/yyyy HH:mm:ss.SSS"));
    }

    @Test
    public void testAddDay() {
        Calendar c = Calendar.getInstance();
        c.set(2016, Calendar.FEBRUARY, 10, 10, 22, 66);
        Date date = c.getTime();
        assertEquals(dateToString(date,"12/MM/yyyy HH:mm:ss.SSS"), dateToString(DateUtil.addDays(date,2), "dd/MM/yyyy HH:mm:ss.SSS"));
    }
    @Test
    public void testAddMonth() {
        Calendar c = Calendar.getInstance();
        c.set(2016, Calendar.FEBRUARY, 10, 10, 22, 66);
        Date date = c.getTime();
        assertEquals(dateToString(date,"dd/04/yyyy HH:mm:ss.SSS"), dateToString(DateUtil.addMonths(date,2), "dd/MM/yyyy HH:mm:ss.SSS"));
    }

    /**
     * @param string
     * @return
     */
    public static Date dateFromString(String string) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return format.parse(string);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

    /**
     * @param date
     * @return
     */
    public static String dateToString(Date date, String format2) {
        DateFormat format = new SimpleDateFormat(format2);
        return format.format(date);
    }
}
