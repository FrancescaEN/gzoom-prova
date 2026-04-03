package it.mapsgroup.gzoom.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.report.report.dto.ReportStatus;
import it.mapsgroup.gzoom.service.report.ReportDownloadService;
import it.mapsgroup.gzoom.mybatis.dto.ReportActivity;

/**
 */
@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class ReportDownloadController {

    private final ReportDownloadService reportDownloadService;

    @Autowired
    public ReportDownloadController(ReportDownloadService reportDownloadService) {
        this.reportDownloadService = reportDownloadService;
    }
    
    @RequestMapping(value = "report-download", method = RequestMethod.GET)
    @ResponseBody
    public Result<ReportActivity> getReportDownloads() {
        return Exec.exec("report-download get", () -> reportDownloadService.getReportDownloads());
    }
    

    @RequestMapping(value = "report-download/{activityId}/status", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ReportStatus> update(@PathVariable(value = "activityId") String activityId) {
        return Exec.exec("report-download status", () -> reportDownloadService.status(activityId));
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "report-download/{activityId}/stream")
    @ResponseBody
    public String stream(@PathVariable(value = "activityId") String activityId, HttpServletRequest req, HttpServletResponse response) {
    	return Exec.exec("report-download stream", () -> reportDownloadService.stream(activityId, req, response));
    }
    
    @RequestMapping(value = "report-download/{activityId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Boolean delete(@PathVariable(value = "activityId") String activityId) {
        return Exec.exec("report-download delete", () -> reportDownloadService.deleteReport(activityId));
    }    
       
}
