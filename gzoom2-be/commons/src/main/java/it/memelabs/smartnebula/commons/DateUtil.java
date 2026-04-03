package it.memelabs.smartnebula.commons;

import it.mapsgroup.gzoom.commons.collect.Tuple2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * @author Andrea Fossi. JAVA 8 date util
 */
public class DateUtil {

    public static final String ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static Date getEndOfDay(Date date) {
        return asDate(asLocalDate(date).atTime(LocalTime.MAX));
    }

    public static Date getStartOfDay(Date date) {
        return asDate(asLocalDate(date).atStartOfDay());
    }

    public static Date addDays(Date date, int amount) {
        return asDate(asLocalDateTime(date).plusDays(amount));
    }

    public static Date addMonths(Date date, int amount) {
        return asDate(asLocalDateTime(date).plusMonths(amount));
    }

    public static Date getFirstDateOfCalendarWeek(Date referenceDate) {
        if (referenceDate == null) {
            return null;
        }
        return getFirstDateOfCalendarWeek(getWeekNumber(referenceDate), getYear(referenceDate));
    }

    /**
     * Extract year number from reference date
     *
     * @param referenceDate
     * @return week number
     */
    public static int getWeekNumber(Date referenceDate) {
        LocalDate date = asLocalDate(referenceDate);
        return date.get(WeekFields.ISO.weekOfWeekBasedYear());
    }

    /**
     * Extract week number from reference date ISO_8601
     *
     * @param referenceDate
     * @return year
     */
    public static int getYear(Date referenceDate) {
        LocalDate date = asLocalDate(referenceDate);
        Year yearFields = Year.from(date);
        return yearFields.getValue();
    }

    /**
     * @param calendarWeek
     * @param year
     * @return firstDayOfWeek
     */
    public static Date getFirstDateOfCalendarWeek(int calendarWeek, int year) {
        TemporalAccessor parse = DateTimeFormatter.ISO_WEEK_DATE.parse(String.format("%d-W%02d-1", year, calendarWeek));
        LocalDate date = LocalDate.from(parse);
        return asDate(date);
    }

    /**
     * @param calendarWeek
     * @param year
     * @return Tuple2<firstDayOfWeek,lastDayOfWeek>
     */
    public static Tuple2<Date, Date> getDatesOfCalendarWeek(int calendarWeek, int year) {
        TemporalAccessor parse = DateTimeFormatter.ISO_WEEK_DATE.parse(String.format("%d-W%02d-1", year, calendarWeek));
        LocalDate date = LocalDate.from(parse);
        return new Tuple2<Date, Date>(asDate(date), asDate(date.plusDays(6)));
    }

    public static List<LocalDate> datesListOfCalendarWeek(int calendarWeek, int year) {
        TemporalAccessor parse = DateTimeFormatter.ISO_WEEK_DATE.parse(String.format("%d-W%02d-1", year, calendarWeek));
        LocalDate start = LocalDate.from(parse);
        return IntStream.range(0, 7).mapToObj(start::plusDays).collect(toList());
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String toString(Date date, String pattern) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        } else {
            return null;
        }
    }

    public static Date parse(String str, String pattern) {
        if (str != null && str.trim().length() > 0) {
            DateFormat df = new SimpleDateFormat(pattern);
            try {
                return df.parse(str);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }
}
