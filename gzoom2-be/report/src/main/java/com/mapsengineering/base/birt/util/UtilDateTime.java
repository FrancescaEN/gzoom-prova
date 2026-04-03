package com.mapsengineering.base.birt.util;


import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UtilDateTime {
	
	public static final int FIRST_MONTH = 1;
	public static final int LAST_MONTH = 12;
	public static final String DEFAULT_FORMAT_DATE = "dd/MM/yyyy";
	
	private UtilDateTime() {}
	
	
	public static String toDateString(Date date) {
        return toDateString(date, DEFAULT_FORMAT_DATE);
    }


    public static Date toDate(String date, Locale locale,String timeZone)  {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", locale);
		TimeZone currentTimeZone = null;
		if (timeZone != null) {
			currentTimeZone = TimeZone.getTimeZone(timeZone);
		} else {
			currentTimeZone = TimeZone.getDefault();
		}
		formatter.setTimeZone(currentTimeZone);
		Date ret = null;
		try {
			ret =  formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}


        
	public static String toDateString(Date date, String format) {
        if (date == null) return "";
        SimpleDateFormat dateFormat = null;
        if (format != null) {
            dateFormat = new SimpleDateFormat(format);
        } else {
            dateFormat = new SimpleDateFormat();
        }

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        return dateFormat.format(date);
    }
	
	public static Date getDateMeasureReference() {
		Calendar cal = Calendar.getInstance();
		cal.set(1899, 11, 31, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static Date getMonthStartFromDatetime(String monthStr, int year) {
		if (monthStr != null ) {			
			int month = Integer.valueOf(monthStr);
			if (month >= FIRST_MONTH && month <= LAST_MONTH) {
				Calendar cal = Calendar.getInstance();
				cal.set(year, month-1, 1, 0, 0, 0);
				cal.set(Calendar.MILLISECOND, 0);
				return cal.getTime();
			}
		}	
		return null;
	}
	
	@Deprecated
	public static Date getMonthStartFromDatetime(String monthStr, TimeZone timeZone, int year, Locale locale) {
		return getMonthStartFromDatetime(monthStr, year);
	}
	@Deprecated
	public static Date getMonthStartFromDatetime(String monthStr, String timeZone, int year, Locale locale) {
		return getMonthStartFromDatetime(monthStr, year);
	}
	
	public static Date getMonthEndFromDatetime(String monthStr, int year) {
		if (monthStr != null) {			
			int month = Integer.valueOf(monthStr);
			if (month >= FIRST_MONTH && month <= LAST_MONTH) {
				Calendar cal = Calendar.getInstance();
				cal.set(year,month-1,1,0,0,0);
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				return cal.getTime();
			}
		}
		return null;
	}
	
	@Deprecated
	public static Date getMonthEndFromDatetime(String monthStr, TimeZone timeZone, int year, Locale locale) {
		return getMonthEndFromDatetime(monthStr, year);
	}
	@Deprecated
	public static Date getMonthEndFromDatetime(String monthStr, String timeZone, int year, Locale locale) {
		return getMonthEndFromDatetime(monthStr, year);
	}

// TODO CREDO CHE SIANO SOLO UTILIZZATE NELL'INTERFACCIA
//	/**
//	 * Converte una data in un valore numerico
//	 * @param value
//	 * @param timeZone
//	 * @param locale
//	 * @return
//	 * @throws ParseException 
//	 */
//	public static String dateConvertNumberString(String value, TimeZone timeZone, Locale locale) {		
//		Date data = org.ofbiz.base.util.UtilDateTime.toDate(value, timeZone, locale);		
//		
//		Timestamp timestamp = getTimestampFromDate(data);
//		int number = getIntervalInDays(DATE_MEASURE_REFERENCE, timestamp);
//		return String.valueOf(number);	
//	}
//	
//	/**
//	 * Converte la data in valore numerico, e ritorna l'intero direttamente
//	 * @param value
//	 * @param timeZone
//	 * @param locale
//	 * @return
//	 */
//	public static int dateConvertNumber(String value, TimeZone timeZone, Locale locale) {      
//	    Date data = org.ofbiz.base.util.UtilDateTime.toDate(value, timeZone, locale);       
//
//	    Timestamp timestamp = getTimestampFromDate(data);
//	    return getIntervalInDays(DATE_MEASURE_REFERENCE, timestamp);  
//	}
//	
	
	/**
	 * Converte un numero in una data
	 * @param number
	 * @return
	 */
	public static String numberConvertToDate(Double number) {
		if (number == null || number == 0) {
			return null;
		}
		Date dateMeasureReference = getDateMeasureReference();
		Timestamp timestamp = new Timestamp(dateMeasureReference.getTime() + (24L*60L*60L*1000L*number.intValue()));
		
		return toDateString(new Date(timestamp.getTime()), DEFAULT_FORMAT_DATE);
	}
	
	/**
	 * Converte un numero in una data
	 * @param number
	 * @param locale
	 * @return
	 */
	public static String numberConvertToDate(Double number, Locale locale) {
		if (number == null || number == 0) {
			return null;
		}
		Date dateMeasureReference = getDateMeasureReference();
		Timestamp timestamp = new Timestamp(dateMeasureReference.getTime() + (24L*60L*60L*1000L*number.intValue()));
		
		return toDateString(new Date(timestamp.getTime()), DEFAULT_FORMAT_DATE);
	}
	
	/**
	 * Converte un numero in una data
	 * @param number
	 * @param stringLocale
	 * @return
	 */
	public static String numberConvertToDate(Double number, String stringLocale) {
		return numberConvertToDate(number, new Locale(stringLocale));
	}
	
	
	
	/**
	 * Intervallo in giorni tra due date considerante approssimazione nell'intero
	 * @param from
	 * @param thru
	 * @return
	 */
	public static int getIntervalInDays(Timestamp from, Timestamp thru) {
        if(thru == null){
        	return 0;
        }
        float diff = thru.getTime() - from.getTime();
        int gg = (int) Math.round( diff / (24 *60 *60 *1000));		
		
		return gg; 
    }
	
	/**
	 * ritorna ilTimestamp corrispondente alla data in input, o null se la data è null
	 * @param data
	 * @return
	 */
	public static Timestamp getTimestampFromDate(Date data) {
	    if(data == null) {
	        return null;
	    }
	    return new Timestamp(data.getTime());
	}

	public static Date getDateFromDayMonthYear(int day, String monthStr, int year, Locale locale) {
		Date res=null;
		if (monthStr != null && locale != null) {

			int month = Integer.valueOf(monthStr);
			if (month >= FIRST_MONTH && month <= LAST_MONTH) {
				Calendar cal = Calendar.getInstance();
				if (cal != null) {
					cal.set(Calendar.DAY_OF_MONTH, day);
					cal.set(Calendar.MONTH, month-1);
					cal.set(Calendar.YEAR, year);

					res = cal.getTime();
				}
			}
		}

		return res;

	}





}
