package com.mapsengineering.base.birt.util;

import java.util.Date;

public class UtilsConvertJdbc {
    
    public static final String DATE_FORMAT_ORACLE_DB = "MM/DD/YYYY";
    public static final String DATE_FORMAT_ORACLE = "MM/dd/yyyy";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DIALECT = "mysql";
    
    
    @Deprecated
    public static String getConvertDateToDateJdbc(Date date, Object delegator) {      
      return getConvertDateToDateJdbc(date, DEFAULT_DIALECT);
    }
    
  /**
  * Utilizzato per convertire la data nel formato corretto in base al tipo di DB utilizzato
  * @param date
  * @param delegator
  * @return
  */
	public static String getConvertDateToDateJdbc(Date date, String odaDialect) {
		String dialect = odaDialect == null ? DEFAULT_DIALECT : odaDialect;
		
		String dateString = "'" + UtilDateTime.toDateString(date, DATE_TIME_FORMAT) + "'";

		if (isOracle(dialect)) {
			dateString = getConvertDateToDateOracle(date);
		} else if (isMsSql(dialect)) {
            dateString = getConvertDateToDateMsSql(dateString);
        }
		return dateString;
	}

    private static String getConvertDateToDateOracle(Date date) {        
        //String dateString = "'" + UtilDateTime.toDateString(date, DATE_FORMAT_ORACLE) + "'";
        String dateString = UtilDateTime.toDateString(date, DATE_FORMAT_ORACLE) ;
        return "TO_DATE('" + dateString + "','" + DATE_FORMAT_ORACLE_DB +"')";
    }

    private static String getConvertDateToDateMsSql(String dateString) {
        return "CONVERT(DATETIME, " + dateString + ", 121)";
    }
  
	private static boolean isMsSql(String type) {
		if ("mssql".equals(type)) {
			return true;
		}
		return false;
	}

	private static boolean isPostgres(String type) {
		if ("postgres".equals(type)) {
			return true;
		}
		return false;
	}

	private static boolean isOracle(String type) {
		if ("oracle".equals(type)) {
			return true;
		}
		return false;
	}
    
	
	
	 /**
     * Utilizzato per convertire la funziona di adddate
     * @param interval = day per aggiungere giorni alla data
     * @param number = campo che contiene il numero di giorni che devono essere aggiunti
     * @param date = campo da dove prendere la data
     * @param delegator
     * @return
     */
    public static String getConvertAddDateToJdbc(String interval, String number, String date, String odaDialect) {
    	String dialect = odaDialect == null ? DEFAULT_DIALECT : odaDialect;    
    	
        String addDateString = "";
        if (isMsSql(dialect)) {
            addDateString = getConvertAddDateToDateMsSql(interval, number, date);
        } else if (isOracle(dialect)) {
            addDateString = getConvertAddDateToDateOracle(number, date);
        } else if (isPostgres(dialect)) {
            addDateString = getConvertAddDateToDatePostgres(interval, number, date);
        } else {
            addDateString = getConvertAddDateToDateMySql(interval, number, date);
        }
        
        return addDateString;
    }
    
    
    /**
     * Utilizzato per convertire la funziona di adddate
     * @param interval = day per aggiungere giorni alla data
     * @param number = campo che contiene il numero di giorni che devono essere aggiunti
     * @param date = campo da dove prendere la data
     * @param delegator
     * @return
     */
    @Deprecated
    public static String getConvertAddDateToJdbc(String interval, String number, String date, Object delegator) {
        return getConvertAddDateToJdbc(interval, number, date, DEFAULT_DIALECT);
    }
    
    /**
     * 
     * @param interval = day per aggiungere giorni alla data
     * @param number = campo che contiene il numero di giorni che devono essere aggiunti
     * @param date 
     * @param delegator
     * @return
     */
    @Deprecated
    public static String getConvertAddDateToJdbc(String interval, String number, Date date, Object delegator) {
        return getConvertAddDateToJdbc(interval, number, UtilDateTime.toDateString(date, DATE_TIME_FORMAT), DEFAULT_DIALECT);
    }
    
    /**
     * 
     * @param interval = day per aggiungere giorni alla data
     * @param number = campo che contiene il numero di giorni che devono essere aggiunti
     * @param date 
     * @return
     */
    public static String getConvertAddDateToJdbc(String interval, String number, Date date) {
        return getConvertAddDateToJdbc(interval, number, UtilDateTime.toDateString(date, DATE_TIME_FORMAT), DEFAULT_DIALECT);
    }
    

    
    /**
     * 
     * DATEADD (datepart, number, date) 
     * @param interval
     * @param number
     * @param date
     * @return
     */
    private static String getConvertAddDateToDateMsSql(String interval, String number, String date) {
        return "DATEADD(" + interval + ", " + number + ", " + date +")";
    }
    /*
    private static String getConvertAddDateToDateMsSql(String interval, String number, Date date) {
        String dateString = "'" + org.ofbiz.base.util.UtilDateTime.toSqlDateString(date) + "'";
        //return "DATEADD(" + interval + ", " + number + ", " + getConvertDateToDateMsSql(dateString) +")";
        return "DATEADD(" + interval + ", " + number + ", " + dateString +")";
    }*/
    
    
    /**
     * ADDDATE(date, INTERVAL expr unit)
     * @param interval
     * @param number
     * @param date
     * @return
     */
    private static String getConvertAddDateToDateMySql(String interval, String number, String date) {
        return "ADDDATE(" + date + ", INTERVAL " + number + " " + interval +")";
    }/*
    private static String getConvertAddDateToDateMySql(String interval, String number, Date date) {
        String dateString = "'" + org.ofbiz.base.util.UtilDateTime.toSqlDateString(date) + "'";
        return "ADDDATE(" + dateString + ", INTERVAL " + number + " " + interval +")";
    }*/
    
    /**
     * date + 'number day'
     * @param interval
     * @param number
     * @param date
     * @return
     */
    private static String getConvertAddDateToDatePostgres(String interval, String number, String date) {
        return date + " + '" + number + " " + interval + "'";
    }/*
    private static String getConvertAddDateToDatePostgres(String interval, String number, Date date) {
        String dateString = "'" + org.ofbiz.base.util.UtilDateTime.toSqlDateString(date) + "'";
        return dateString + " + '" + number + " " + interval + "'";
    }*/
    
    /**
     * date + 1  
     * @param interval
     * @param number
     * @param date
     * @return
     */
    private static String getConvertAddDateToDateOracle(String number, String date) {
        return date + " + " + number ;
    }/*
    private static String getConvertAddDateToDateOracle(String number, Date date) {
        return getConvertDateToDateOracle(date) + " + " + number ;
    }*/
    
}
