package it.mapsgroup.gzoom.service.report;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.ReportParams;
import it.mapsgroup.gzoom.report.report.dto.CreateReport;
import it.mapsgroup.gzoom.report.report.dto.ReportStatus;
import it.mapsgroup.gzoom.service.GzoomReportClientConfig;
import it.mapsgroup.gzoom.mybatis.dto.ReportActivity;
import it.memelabs.smartnebula.commons.DateUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 */
@Service
public class ReportClientService {  

    private static final Logger LOG = getLogger(ReportClientService.class);
    
    private final RestTemplate restTemplate;
    private final GzoomReportClientConfig config;

    @Autowired
    public ReportClientService(RestTemplate restTemplate, GzoomReportClientConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

  //todo sample rest call
    public String createReport() {
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
        reportParameters.put("exposePaginator", "Y");
        reportParameters.put("reportContentId", "REPORT_CATALOGO");
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
        String reportId = restTemplate.postForObject("http://localhost:8081/rest/report/add", request, String.class);
        LOG.info("ReportId {}", reportId);
        return reportId;
    }

    public String createReport(CreateReport request) {
        String reportId = restTemplate.postForObject(config.getServerReportUrl() + "/add", request, String.class, request);
        LOG.info("ReportId {}", reportId);
        return reportId;
    }

    public ResponseEntity<ReportParams> getReportParams(String parentTypeId, String resourceName, String contentName) {
        return restTemplate.getForEntity(config.getServerReportUrl() + "/params/" + parentTypeId + "/" + resourceName+ "/"+contentName, ReportParams.class, resourceName);
    }

    /*public ResponseEntity<ReportStatus> getStatus(String id) {
        return restTemplate.getForEntity("http://localhost:8081/rest/report/{reportId}/status", ReportStatus.class, id);
    }*/

    public ResponseEntity<ReportStatus> getStatus(String id) {
        return restTemplate.getForEntity(config.getServerReportUrl() + "/" + id + "/status", ReportStatus.class, id);
    }

    public ResponseEntity<ReportActivity> getReportActivity(String id) {
        return restTemplate.getForEntity(config.getServerReportUrl() + "/" + id, ReportActivity.class, id);
    }

	public Result<ReportActivity> getReportDownloads(String userLoginId) {
		List rest = (List) ((HashMap) restTemplate.getForObject(config.getServerReportUrl() + "/report-download/" + userLoginId, Object.class, userLoginId)).get("results");
		return new Result<>(rest, rest.size());
    }


    public String cancel(String id) {
    	restTemplate.delete(config.getServerReportUrl() + "/" + id, id);
        return "";
    }
	
}
