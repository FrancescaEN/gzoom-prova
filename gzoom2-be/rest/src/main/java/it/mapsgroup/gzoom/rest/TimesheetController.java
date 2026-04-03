package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.Timesheet;
import it.mapsgroup.gzoom.mybatis.dto.TimesheetEx;
import it.mapsgroup.gzoom.mybatis.dto.NoteData;
import it.mapsgroup.gzoom.service.TimesheetService;
import it.mapsgroup.gzoom.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static it.mapsgroup.gzoom.security.Principals.principal;

/**
 */
@RestController
@RequestMapping(value = "timesheet", produces = { MediaType.APPLICATION_JSON_VALUE })
public class TimesheetController {

    private final TimesheetService timesheetService;
    private final UserPreferenceService userPreferenceService;

    @Autowired
    public TimesheetController(TimesheetService timesheetService, UserPreferenceService userPreferenceService) {
        this.timesheetService = timesheetService;
        this. userPreferenceService = userPreferenceService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Result<TimesheetEx> getTimesheet() {
        return Exec.exec("timesheet get", () -> timesheetService.getTimesheet(principal().getUserLoginId()));
    }

    @RequestMapping(value = "/timesheetbyid/{id}", method = RequestMethod.GET)
    @ResponseBody
    public TimesheetEx getTimesheetById(@PathVariable(value = "id") String id) {
        return Exec.exec("timesheet get", () -> timesheetService.getTimesheetExsBy(id,principal().getUserLoginId()));
    }

    @RequestMapping(value = "/pagination", method = RequestMethod.POST)
    @ResponseBody
    private Result<TimesheetEx> getTimesheetPagination(@RequestBody InfoPage infoPage) {
        return Exec.exec("timesheet pagination with filter get", () -> timesheetService.getTimesheetPagination(principal().getUserLoginId(), infoPage));
    }

    @RequestMapping(value = "/params/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Result<NoteData> getParamsTimesheet(@PathVariable(value = "id")  String id) {
        return Exec.exec("timesheet-params get", () -> timesheetService.getParamsTimesheet(id));
    }

    @RequestMapping(value = "/" , method = RequestMethod.POST)
    @ResponseBody
    public String createTimesheet(@RequestBody Timesheet req) {
        return Exec.exec( "timesheet-post", () -> timesheetService.createTimesheet(req));
    }

    @PostMapping(value="/delete")
    @ResponseBody
    public boolean deleteTimesheet(@RequestBody String[] timesheets){
        return Exec.exec("timesheets delete", () -> timesheetService.deleteTimesheet(timesheets));
    }

    @RequestMapping(value = "/validStatus/{timesheetId}", method = RequestMethod.PUT)
    @ResponseBody
    public String validStatusItem(@PathVariable(value = "timesheetId") String timesheetId) {
        return Exec.exec("timesheet put", () -> timesheetService.validStatusItem(timesheetId));
    }

    @RequestMapping(value = "/reopenStatus/{timesheetId}", method = RequestMethod.PUT)
    @ResponseBody
    public String reopenStatusItem(@PathVariable(value = "timesheetId") String timesheetId) {
        return Exec.exec("timesheet put", () -> timesheetService.reopenStatusItem(timesheetId));
    }


}
