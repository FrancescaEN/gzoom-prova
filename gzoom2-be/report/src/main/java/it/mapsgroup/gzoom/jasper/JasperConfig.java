package it.mapsgroup.gzoom.jasper;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.logging.Level;


@Component
public class JasperConfig {

    public static final String DEFAULT_LOGGING_DIRECTORY = "defaultBirtLoggingDirectory/";

    private final String jasperLoggingDirectory;
    private final String jasperTempFileOutputDir;
    private final String jasperReportInputDir;
    private final String jasperReportOutputDir;
    private final Level jasperLoggingLevel;
    private final String odaUrl;
    private final String odaDriverClass;
    private final String odaPassword;
    private final String odaUser;
    private final String odaDialect;
    private final String developerBirtPath;
    private final String odaIsolationMode;


    public JasperConfig(Environment env) {
        jasperLoggingDirectory = env.getProperty("birt.logging.directory", DEFAULT_LOGGING_DIRECTORY);
        jasperLoggingLevel = Level.parse(env.getProperty("birt.logging.level", Level.SEVERE.getName()));
        jasperTempFileOutputDir = env.getProperty("birt.temp.file.output.dir");
        jasperReportInputDir = env.getProperty("birt.report.input.dir");
        jasperReportOutputDir = env.getProperty("birt.report.output.dir");
        odaUrl = env.getProperty("oda.url");
        odaDriverClass = env.getProperty("oda.driver.class");
        odaPassword = env.getProperty("oda.password");
        odaUser = env.getProperty("oda.user");
        odaDialect = env.getProperty("oda_dialect");
        odaIsolationMode = env.getProperty("oda.IsolationMode");
        developerBirtPath = env.getProperty("birt.developerBirtPath");
    }

    public String getJasperLoggingDirectory() {
        return jasperLoggingDirectory;
    }

    public String getJasperTempFileOutputDir() {
        return jasperTempFileOutputDir;
    }

    public String getJasperReportInputDir() {
        return jasperReportInputDir;
    }

    public Level getJasperLoggingLevel() {
        return jasperLoggingLevel;
    }

    public String getOdaUrl() {
        return odaUrl;
    }

    public String getOdaDriverClass() {
        return odaDriverClass;
    }

    public String getOdaPassword() {
        return odaPassword;
    }

    public String getOdaUser() {
        return odaUser;
    }

    public String getJasperReportOutputDir() {
        return jasperReportOutputDir;
    }

    public String getOdaDialect() {
        return odaDialect;
    }

    public String getOdaIsolationMode() {return odaIsolationMode; }
    
    public String getDeveloperBirtPath() { return  developerBirtPath; }
}
