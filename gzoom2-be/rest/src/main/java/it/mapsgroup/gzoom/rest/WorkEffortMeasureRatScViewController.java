package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortMeasureRatScView;
import it.mapsgroup.gzoom.service.WorkEffortMeasureRatScViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "work-effort-measure-rat-sc-view", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortMeasureRatScViewController {
    private final WorkEffortMeasureRatScViewService workEffortMeasureRatScViewService;

    @Autowired
    public WorkEffortMeasureRatScViewController(WorkEffortMeasureRatScViewService workEffortMeasureRatScViewService) {
        this.workEffortMeasureRatScViewService = workEffortMeasureRatScViewService;
    }

    @GetMapping(value = "/{workEffortMeasureId}")
    @ResponseBody
    public Result<WorkEffortMeasureRatScView> getWorkEffortMeasureRatScView(@PathVariable(value = "workEffortMeasureId") String workEffortMeasureId){
        return Exec.exec("get getWorkEffortMeasureRatScView", () -> this.workEffortMeasureRatScViewService.getWorkEffortMeasureRatScView(workEffortMeasureId) );
    }
}
