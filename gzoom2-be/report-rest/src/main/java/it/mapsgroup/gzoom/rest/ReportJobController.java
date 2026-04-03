package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.ReportParams;
import it.mapsgroup.gzoom.report.report.dto.CancelReport;
import it.mapsgroup.gzoom.report.report.dto.CreateReport;
import it.mapsgroup.gzoom.report.report.dto.ReportStatus;
import it.mapsgroup.gzoom.service.ReportJobService;
import it.mapsgroup.gzoom.mybatis.dto.ReportActivity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 */
@RestController
@RequestMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ReportJobController {

    private final ReportJobService reportJobService;

    @Autowired
    public ReportJobController(ReportJobService reportJobService) {
        this.reportJobService = reportJobService;
    }
    

    @RequestMapping(value = "/report-job/report-download/{userLoginId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<ReportActivity> getActities(@PathVariable(value = "userLoginId") String userLoginId){
        return Exec.exec("reports", () -> reportJobService.getActivity(userLoginId));
    }

    @RequestMapping(value = "/report-job/add", method = RequestMethod.POST)
    @ResponseBody
    public String add(@RequestBody CreateReport report) {
        return Exec.exec("addToQueue-report", () -> reportJobService.add(report));
    }

    @RequestMapping(value = "/report-job/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String cancel(@PathVariable(value = "id") String id, @RequestBody CancelReport cancelReport) {
        return Exec.exec("cancel-report", () -> reportJobService.cancel(id, cancelReport.getReason()));
    }

    @RequestMapping(value = "/report-job/{id}/status", method = RequestMethod.GET)
    @ResponseBody
    public ReportStatus status(@PathVariable(value = "id") String id) {
        return Exec.exec("status-report", () -> reportJobService.getStatus(id));
    }
    
    @RequestMapping(value = "/report-job/params/{parentTypeId}/{resourceName}/{contentName}", method = RequestMethod.GET)
    @ResponseBody
    public ReportParams params(@PathVariable(value = "parentTypeId") String parentTypeId,@PathVariable(value = "resourceName") String resourceName, @PathVariable(value = "contentName") String contentName) {
        return Exec.exec("getParams-report", () -> reportJobService.params(parentTypeId, resourceName, contentName));
    }

    @RequestMapping(value = "/report-job/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ReportActivity get(@PathVariable(value = "id") String id) {
        return Exec.exec("get-report", () -> reportJobService.get(id));
    }

}
