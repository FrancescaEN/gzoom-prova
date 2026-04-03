package it.mapsgroup.gzoom.birt;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.logging.Level;

/**
 * @author Andrea Fossi.
 */
@Component
public class BirtConfig {

    public static final String DEFAULT_LOGGING_DIRECTORY = "defaultBirtLoggingDirectory/";

    private final String birtLoggingDirectory;
    private final String birtTempFileOutputDir;
    private final String birtReportInputDir;
    private final String birtReportOutputDir;
    private final Level birtLoggingLevel;
    private final String odaUrl;
    private final String odaDriverClass;
    private final String odaPassword;
    private final String odaUser;
    private final String odaDialect;
    private final String developerBirtPath;
    private final String odaIsolationMode;


    public BirtConfig(Environment env) {
        birtLoggingDirectory = env.getProperty("birt.logging.directory", DEFAULT_LOGGING_DIRECTORY);
        birtLoggingLevel = Level.parse(env.getProperty("birt.logging.level", Level.SEVERE.getName()));
        birtTempFileOutputDir = env.getProperty("birt.temp.file.output.dir");
        birtReportInputDir = env.getProperty("birt.report.input.dir");
        birtReportOutputDir = env.getProperty("birt.report.output.dir");
        odaUrl = env.getProperty("oda.url");
        odaDriverClass = env.getProperty("oda.driver.class");
        odaPassword = env.getProperty("oda.password");
        odaUser = env.getProperty("oda.user");
        odaDialect = env.getProperty("oda_dialect");
        odaIsolationMode = env.getProperty("oda.IsolationMode");
        developerBirtPath = env.getProperty("birt.developerBirtPath");
    }

    public String getBirtLoggingDirectory() {
        return birtLoggingDirectory;
    }

    public String getBirtTempFileOutputDir() {
        return birtTempFileOutputDir;
    }

    public String getBirtReportInputDir() {
        return birtReportInputDir;
    }

    public Level getBirtLoggingLevel() {
        return birtLoggingLevel;
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

    public String getBirtReportOutputDir() {
        return birtReportOutputDir;
    }

    public String getOdaDialect() {
        return odaDialect;
    }

    public String getOdaIsolationMode() {return odaIsolationMode; }
    
    public String getDeveloperBirtPath() { return  developerBirtPath; }
}
