package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypePeriod;
import it.mapsgroup.gzoom.service.WorkEffortTypePeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "work-effort-type-period", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortTypePeriodController {

    private final WorkEffortTypePeriodService workEffortTypePeriodService;

    @Autowired
    public WorkEffortTypePeriodController(WorkEffortTypePeriodService workEffortTypePeriodService) {
        this.workEffortTypePeriodService = workEffortTypePeriodService;
    }

    @RequestMapping(value = "/{workEffortType}", method = RequestMethod.GET)
    @ResponseBody
    public List<WorkEffortTypePeriod> getWorkEffortTypePeriodByWorkEffortTypeId(@PathVariable(value = "workEffortType") String workEffortTypeId) {
        return Exec.exec("workEffortType get", () -> workEffortTypePeriodService.getWorkEffortTypePeriodByWorkEffortTypeId(workEffortTypeId));
    }

}
