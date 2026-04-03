package it.mapsgroup.gzoom.quartz;

import it.mapsgroup.gzoom.quartz.oldScheduler.ProbeSchedulerService;
import it.mapsgroup.gzoom.report.report.dto.CreateReport;
import it.mapsgroup.gzoom.report.service.ReportCallbackType;
import it.mapsgroup.gzoom.service.GzoomReportClientConfig;
import it.mapsgroup.gzoom.service.report.ReportClientService;
import it.memelabs.smartnebula.commons.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 */
@RunWith(SpringRunner.class)
@ImportResource("classpath:/lmm/spring/backend-context.xml")
@TestPropertySource("classpath:test.properties")
@Import(SchedulerTestConfig.class)
public class ReportClientServiceQuartzBulkIT {
    private static final Logger LOG = getLogger(ReportClientServiceQuartzBulkIT.class);

    @Autowired
    ProbeSchedulerService probeSchedulerService;

    @Autowired
    ReportClientService client;
    @Autowired
    GzoomReportClientConfig config;

    @Test
    public void test() throws InterruptedException, MalformedURLException {
        //todo sample rest call
        HashMap<String, Object> reportParameters = new HashMap<>();
        //TODO add parameters here

        reportParameters.put("workEffortTypeId", "15AP0PPC");
        reportParameters.put("workEffortId", "E12144");
        reportParameters.put("reportContentId", "REPO_VALUT_RISC"); // REPO_VALUT_RISC - REPO_PRI_VALUT_RISC


        Date date3112 = DateUtil.parse("20171231", "yyyyMMdd");
        reportParameters.put("langLocale", "");
        reportParameters.put("outputFormat", "pdf");
        reportParameters.put("workEffortTypeId", "15AP0PPC");
        reportParameters.put("exposeReleaseDate", "Y");
        reportParameters.put("reportContentId", "REPORT_CATALOGO");
        reportParameters.put("exposePaginator", "Y");
        reportParameters.put("userLoginId", "admin");
        reportParameters.put("userProfile", "MGR_ADMIN");
        reportParameters.put("birtOutputFileName", "CatalogoTreLivelli");
        reportParameters.put("localDispatcherName", "corperf");
        reportParameters.put("defaultOrganizationPartyId", "Company");
        reportParameters.put("date3112", date3112);


        CreateReport request = new CreateReport();
        request.setContentName("test.pdf");
        request.setCreatedByUserLogin("admin");
        request.setModifiedByUserLogin("admin");
        request.setReportLocale("it_IT");
        request.setReportName("CatalogoTreLivelli");
        request.setParams(reportParameters);

        //

        for (int i = 0; i < 20; i++) {
            String id = client.createReport(request);
            probeSchedulerService.scheduleReportProbe(id, ReportCallbackType.TEST, new HashMap<>());
            Thread.sleep(500);
        }
        Thread.sleep(10*60 * 1000);
    }
}
