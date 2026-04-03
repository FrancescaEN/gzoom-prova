package it.mapsgroup.gzoom;

import com.ibm.icu.util.Calendar;
import it.mapsgroup.gzoom.birt.BIRTReport;
import it.mapsgroup.gzoom.birt.BIRTReportRunner;
import it.mapsgroup.gzoom.birt.Report;
import it.mapsgroup.gzoom.birt.ReportHandler;
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

/**
 * @author Andrea Fossi.
 * <p>
 * Spring boot command line report test application
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,})
public class BirtReportRunnerApplication {

    @Bean
    public ApplicationContextProvider applicationContextProvider(ApplicationContext ac) {
        ApplicationContextProvider provider = new ApplicationContextProvider();
        provider.setApplicationContext(ac);
        return provider;
    }

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext ctx = SpringApplication.run(BirtReportRunnerApplication.class, args);

        BIRTReportRunner reportRunner = ctx.getBean(BIRTReportRunner.class);

        //Report report = new BIRTReport("simple_report", "?null=null", reportRunner).runReport();
        HashMap<String, Object> reportParameters = new HashMap<>();
        //TODO add parameters here

        /*
         * SchedaDupOperativa2018
        reportParameters.put("workEffortTypeId", "15AP0PPC");
        reportParameters.put("workEffortId", "14363");
        reportParameters.put("reportContentId", "REPORT_DUP_MAND_STR");
        reportParameters.put("selectNote", "DATA");
        reportParameters.put("typeNotes", "ALL");
        reportParameters.put("exposeOnlyIndex", "N");
        reportParameters.put("localDispatcherName", "stratperf");
        */


//        reportParameters.put("workEffortTypeId", "15AP0PPC");
//        reportParameters.put("workEffortId", "E12144");
//        reportParameters.put("reportContentId", "REPO_VALUT_RISC"); // REPO_VALUT_RISC - REPO_PRI_VALUT_RISC - REPORT_MON_TRAT
        
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

        Report report = new BIRTReport(System.currentTimeMillis() + "", "CTX_CO","StampaTimesheet/StampaTimesheet", reportParameters, Locale.ITALIAN);
        //Report report = new BIRTReport("CatalogoTreLivelli_ORI", reportParameters, reportRunner, Locale.ITALIAN).runReport();
        ReportHandler reportHandler = reportRunner.runReport(report);
        //Report report = new BIRTReport("simple_report", reportParameters, reportRunner, Locale.ITALIAN).runReport();

        
        String namePath = "C:/data/Gzoom_2/birt/logs/report/StampaTimesheet_" + Calendar.getInstance().getTimeInMillis() + ".pdf";
        //String namePath = "C:/data/Gzoom_2/birt/logs/report/ValutazioniRischi_" + Calendar.getInstance().getTimeInMillis() + ".pdf";
        IOUtil.copyCompletely(reportHandler.getReportContent(), new FileOutputStream(namePath));
        //report.getReportContent().writeTo(new FileOutputStream(namePath));

    }

}