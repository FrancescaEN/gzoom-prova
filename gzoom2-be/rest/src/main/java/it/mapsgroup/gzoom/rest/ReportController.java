package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.service.report.ReportQueryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Report;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.service.reminder.ReminderService;
import it.mapsgroup.gzoom.service.report.ReportAddService;
import it.mapsgroup.gzoom.service.report.ReportService;

import jakarta.servlet.http.HttpServletResponse;

/**
 */
@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class ReportController {

    private final ReportService reportService;
    private final ReportAddService reportAddService;
    private final ReminderService reminderService;

    @Autowired
    public ReportController(ReportService reportService, ReportAddService reportAddService, ReminderService reminderService) {
        this.reportService = reportService;
        this.reportAddService = reportAddService;
        this.reminderService = reminderService;
    }

    @RequestMapping(value = "report/workEffortTypeId/{workEffortTypeId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Report> getReportsByWorkEffortTypeId(@PathVariable(value = "workEffortTypeId") String workEffortTypeId) {
        return Exec.exec("report get", () -> reportService.getReportsByWorkEffortTypeId(workEffortTypeId));
    }

    @RequestMapping(value = "report/{parentTypeId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Report> getReports(@PathVariable(value = "parentTypeId") String parentTypeId) {
        return Exec.exec("report get", () -> reportService.getReports(parentTypeId));
    }

    @RequestMapping(value = "report/{parentTypeId}/{reportContentId}/{resourceName}/{workEffortTypeId}", method = RequestMethod.GET)
    @ResponseBody
    public Report getReport(@PathVariable(value = "parentTypeId") String parentTypeId, 
    		@PathVariable(value = "reportContentId") String reportContentId, 
    		@PathVariable(value = "resourceName") String resourceName,
    		@PathVariable(value= "workEffortTypeId") String workEffortTypeId) {
        return Exec.exec("report get", () -> reportService.getReport(parentTypeId, reportContentId, resourceName, workEffortTypeId));
    }
    
    @RequestMapping(value = "report/add", method = RequestMethod.POST)
    @ResponseBody
    public String createReport(@RequestBody Report req) {
        return Exec.exec("report/add post", () -> reportAddService.add(req));
    }

    @RequestMapping(value = "report/query-config/add", method = RequestMethod.POST)
    @ResponseBody
    public String createReportQueryConfig(@RequestBody ReportQueryConfig req, HttpServletResponse response) {
        return Exec.exec("report/query-config/add post", () -> reportAddService.addQueryConfig(req.getReport(), req.getQueryConfig(), response));
    }
    
    @RequestMapping(value = "report/mail", method = RequestMethod.POST)
    @ResponseBody
    public String sendmail(@RequestBody Report req) {
        return Exec.exec("report/sendmail post", () -> reminderService.sendMail(req));
    }

}
