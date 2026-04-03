package it.mapsgroup.gzoom.jasper;



import java.util.Locale;
import java.util.Map;


public class JasperReport extends Report {

    public JasperReport(String taskId, String name, String type, Map<String, Object> reportParameters, Locale reportLocale) {
        super(taskId, name, type, reportParameters, reportLocale);
    }

}