package it.mapsgroup.gzoom;

import com.ibm.icu.util.Calendar;
import it.mapsgroup.gzoom.jasper.JasperReport;
import it.mapsgroup.gzoom.jasper.JasperReportRunner;
import it.mapsgroup.gzoom.jasper.Report;
import it.mapsgroup.gzoom.jasper.ReportHandler;
import it.memelabs.smartnebula.spring.boot.config.ApplicationContextProvider;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,})
public class JasperReportRunnerApplication {

    @Bean
    public ApplicationContextProvider applicationContextProvider(ApplicationContext ac) {
        ApplicationContextProvider provider = new ApplicationContextProvider();
        provider.setApplicationContext(ac);
        return provider;
    }

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext ctx = SpringApplication.run(JasperReportRunnerApplication.class, args);
        JasperReportRunner reportRunner = ctx.getBean(JasperReportRunner.class);
        HashMap<String, Object> reportParameters = new HashMap<>();
        //TODO add parameters here

        reportParameters.put("workEffortTypeId", "PRVST");
        reportParameters.put("reportContentId", "REP_TIMESHEET");
        reportParameters.put("localDispatcherName", "procperf");
        Calendar cal = Calendar.getInstance();
        cal.set(2017, 11, 31, 0, 0, 0);
        reportParameters.put("monitoringDate", cal.getTime());
        reportParameters.put("excludeValidity", "N");
        reportParameters.put("exposeReleaseDate", "Y");
        reportParameters.put("exposePaginator", "Y");
        reportParameters.put("langLocale", "");
        reportParameters.put("outputFormat", "pdf");
        reportParameters.put("userLoginId", "admin");
        reportParameters.put("userProfile", "MGR_ADMIN");
        reportParameters.put("birtOutputFileName", "Timesheet");
        reportParameters.put("birtOutputFileName", "Timesheet");
        reportParameters.put("defaultOrganizationPartyId", "Company");
        Report report = new JasperReport(System.currentTimeMillis() + "", "CTX_CO","StampaTimesheet/StampaTimesheet", reportParameters, Locale.ITALIAN);
        ReportHandler reportHandler = reportRunner.runReport(report);
        String namePath = "C:/data/Gzoom_2/birt/logs/report/StampaTimesheet_" + Calendar.getInstance().getTimeInMillis() + ".pdf";
        IOUtil.copyCompletely(reportHandler.getReportContent(), new FileOutputStream(namePath));
    }
}