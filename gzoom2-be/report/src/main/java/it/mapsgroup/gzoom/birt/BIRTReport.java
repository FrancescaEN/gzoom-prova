package it.mapsgroup.gzoom.birt;


import java.util.Locale;
import java.util.Map;

/**
 * @author Andrea Fossi.
 */
public class BIRTReport extends Report {

    public BIRTReport(String taskId, String name, String type, Map<String, Object> reportParameters, Locale reportLocale) {
        super(taskId, name, type, reportParameters, reportLocale);
    }

}