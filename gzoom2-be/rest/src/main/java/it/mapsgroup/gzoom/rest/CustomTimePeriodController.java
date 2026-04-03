package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.CustomTimePeriod;
import it.mapsgroup.gzoom.service.CustomTimePeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static it.mapsgroup.gzoom.security.Principals.principal;

@RestController
@RequestMapping(value = "customtimeperiods", produces = { MediaType.APPLICATION_JSON_VALUE })
public class CustomTimePeriodController {

    private final CustomTimePeriodService customTimePeriodService;

    @Autowired
    public CustomTimePeriodController(CustomTimePeriodService customTimePeriodService) {
        this.customTimePeriodService = customTimePeriodService;
    }

    @RequestMapping(value = "/{periodTypeId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<CustomTimePeriod> getCustomTimePeriods(@PathVariable(value = "periodTypeId") String periodTypeId) {
        return Exec.exec("get customTimePeriods",() -> customTimePeriodService.getCustomTimePeriods(periodTypeId));
    }

    @GetMapping
    @ResponseBody
    public Result<CustomTimePeriod> getCustomTimePeriodList(){
        return Exec.exec("get custom-time-period", this.customTimePeriodService::getCustomTimePeriodList);
    }

    @GetMapping("indicator-movement/{acctgTransId}/{acctgTransEntrySeqId}")
    @ResponseBody
    public CustomTimePeriod getCustomTimePeriodForIndicatorMovement(@PathVariable String acctgTransId, @PathVariable String acctgTransEntrySeqId) {
        return Exec.exec("getCustomTimePeriodForIndicatorMovement", () -> this.customTimePeriodService.getCustomTimePeriodForIndicatorMovement(acctgTransId, acctgTransEntrySeqId));
    }

    @RequestMapping(value = "/custom-time-period-dropdown-filter", method = RequestMethod.GET)
    @ResponseBody
    public Result<CustomTimePeriod> getTimesheetCustomTimePeriodDropdownFilter() {
        return Exec.exec("timesheet get", () -> customTimePeriodService.getTimesheetCustomTimePeriodDropdownFilter(principal().getUserLoginId()));
    }

    @PostMapping
    @ResponseBody
    public boolean createCustomTimePeriod(@RequestBody CustomTimePeriod req){
        return Exec.exec("create custom-time-period", () -> this.customTimePeriodService.createCustomTimePeriod(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateCustomTimePeriod(@RequestBody CustomTimePeriod req){
        return Exec.exec("update custom-time-period", () -> this.customTimePeriodService.updateCustomTimePeriod(req) );
    }

    @DeleteMapping(value = "/{customTimePeriodId}")
    @ResponseBody
    public boolean deleteCustomTimePeriod(@PathVariable(value = "customTimePeriodId") String[] id){
        return Exec.exec("delete custom-time-period list", () -> this.customTimePeriodService.deleteCustomTimePeriod(id) );
    }

}
