package it.mapsgroup.gzoom.service.reminder;

import java.util.Hashtable;
import java.util.Map;


public enum ReminderReportContentIdEnum {
    
    REMINDER_SCADENZA("REMINDER_SCADENZA", "selectReminderWorkEffortExpiry"),
    REMINDER_PERIODO("REMINDER_PERIODO", "selectReminderPeriod");
    
    private final String code;
    
    private final String queryReminder;

    /**
     * Constructor
     * @param code
     * @param queryReminder
     */
    ReminderReportContentIdEnum(String code, String queryReminder) {
        this.code = code;
        this.queryReminder = queryReminder;
    }

    /**
     * Return code (es: REMINDER_SCADENZA)
     * @return code
     */
    public String code() {
        return code;
    }
    
    /**
     * Return queryReminder (es: sql/reminder/queryReminderScadObi.sql.ftl)
     * @return queryReminder
     */
    public String queryReminder() {
        return queryReminder;
    }
   
    private static final Map<String, String> REP_CONT_QUERY = new Hashtable<String, String>();

    static {
        for (ReminderReportContentIdEnum ss : values()) {
            REP_CONT_QUERY.put(ss.code, ss.queryReminder);
        }
    }
    
    /**
     * Return String
     * @param code
     * @return
     */
    public static String getQuery(String code) {
        return REP_CONT_QUERY.get(code);
    }
    
}