package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortMeasRatSc;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortMeasRatScExUomRatingScale;
import it.mapsgroup.gzoom.service.WorkEffortMeasRatScService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "work-effort-meas-rat-sc", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortMeasRatScController {
    private final WorkEffortMeasRatScService workEffortMeasRatScService;

    @Autowired
    public WorkEffortMeasRatScController(WorkEffortMeasRatScService workEffortMeasRatScService) {
        this.workEffortMeasRatScService = workEffortMeasRatScService;
    }

    @GetMapping(value = "/{workEffortMeasureId}")
    @ResponseBody
    public Result<WorkEffortMeasRatScExUomRatingScale> getRatingScaleWEM(@PathVariable(value = "workEffortMeasureId") String workEffortMeasureId){
        return Exec.exec("get RatingScaleWEM", () -> this.workEffortMeasRatScService.getRatingScaleWEM(workEffortMeasureId) );
    }

    @PostMapping
    @ResponseBody
    public boolean createWorkEffortMeasRatSc(@RequestBody WorkEffortMeasRatSc req){
        return Exec.exec("create work-effort-meas-rat-sc", () -> this.workEffortMeasRatScService.createWorkEffortMeasRatSc(req));
    }

    @PutMapping
    @ResponseBody
    public int updateWorkEffortMeasRatSc(@RequestBody WorkEffortMeasRatSc req){
        return Exec.exec("update work-effort-meas-rat-sc", () -> this.workEffortMeasRatScService.updateWorkEffortMeasRatSc(req) );
    }

    @DeleteMapping(value = "/{workEffortMeasureId}/{uomId}/{uomRatingValue}")
    @ResponseBody
    public int deleteWorkEffortMeasRatSc(@PathVariable(value = "workEffortMeasureId") String workEffortMeasureId, @PathVariable(value = "uomId") String uomId, @PathVariable(value = "uomRatingValue") Double uomRatingValue){
        return Exec.exec("delete work-effort-meas-rat-sc", () -> this.workEffortMeasRatScService.deleteWorkEffortMeasRatSc(workEffortMeasureId, uomId, uomRatingValue));
    }

}
