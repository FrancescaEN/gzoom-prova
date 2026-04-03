package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortMeasExUom;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortMeasure;
import it.mapsgroup.gzoom.service.WorkEffortMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "work-effort-measure", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortMeasureController {
    private final WorkEffortMeasureService workEffortMeasureService;

    @Autowired
    public WorkEffortMeasureController(WorkEffortMeasureService workEffortMeasureService) {
        this.workEffortMeasureService = workEffortMeasureService;
    }

    @GetMapping(value = "/{workEffortIdFrom}")
    @ResponseBody
    public Result<WorkEffortMeasure> getWeMeasureEvalId(@PathVariable(value = "workEffortIdFrom") String workEffortIdFrom){
        return Exec.exec("get WeMeasureEvalId", () -> this.workEffortMeasureService.getWeMeasureEvalId(workEffortIdFrom) );
    }

    @GetMapping(value = "/id/{workEffortMeasureId}")
    @ResponseBody
    public WorkEffortMeasure getWorkEffortMeasure(@PathVariable(value = "workEffortMeasureId") String workEffortMeasureId){
        return Exec.exec("get WeMeasureEvalId", () -> this.workEffortMeasureService.getWorkEffortMeasure(workEffortMeasureId) );
    }
    
    @GetMapping
    @ResponseBody
    public Result<WorkEffortMeasExUom> getWorkEffortMeasureList(){
        return Exec.exec("get work-effort-measure-list", () -> this.workEffortMeasureService.getWorkEffortMeasureList() );
    }

    @GetMapping("/work-effort")
    @ResponseBody
    public Result<WorkEffortMeasure> getWorkEffortMeasureJoinWorkEffort(@RequestParam String query, @RequestParam boolean secondaryLang){
        return Exec.exec("get work-effort-measure-join-work-effort", () -> this.workEffortMeasureService.getWorkEffortMeasureJoinWorkEffort(query, secondaryLang) );
    }

    @GetMapping("/dropdown-movement")
    @ResponseBody
    public Result<WorkEffortMeasure> dropdownWorkEffortMeasure(){
        return Exec.exec("dropdownWorkEffortMeasure", this.workEffortMeasureService::dropdownWorkEffortMeasure);
    }

    @GetMapping("/dropdown-wea")
    @ResponseBody
    public Result<WorkEffortMeasure> dropdownWorkEffortMeasureWEA(){
        return Exec.exec("dropdownWorkEffortMeasure", this.workEffortMeasureService::dropdownWorkEffortMeasureWEA);
    }

    @RequestMapping(value = "/total", method = RequestMethod.POST)
    @ResponseBody
    public int getWorkEffortMeasureListPaginationTotal(@RequestBody InfoPage infoPage){
        return Exec.exec("get work-effort-measure-list-pagination-total", () -> this.workEffortMeasureService.getTotale(infoPage) );
    }

    @RequestMapping(value = "/pagination", method = RequestMethod.POST)
    @ResponseBody
    public Result<WorkEffortMeasExUom> getWorkEffortMeasureListPagination(@RequestBody InfoPage infoPage){
        return Exec.exec("get work-effort-measure-list-pagination", () -> this.workEffortMeasureService.getWorkEffortMeasureListPagination(infoPage) );
    }

    @PostMapping
    @ResponseBody
    public WorkEffortMeasExUom createWorkEffortMeasure(@RequestBody WorkEffortMeasure req){
        return Exec.exec("create work-effort-measure", () -> this.workEffortMeasureService.createWorkEffortMeasure(req));
    }

    @PutMapping
    @ResponseBody
    public boolean updateWorkEffortMeasure(@RequestBody WorkEffortMeasure req){
        return Exec.exec("update work-effort-measure", () -> this.workEffortMeasureService.updateWorkEffortMeasure(req) );
    }

    @DeleteMapping(value = "/{workEffortMeasureId}")
    @ResponseBody
    public boolean deleteWorkEffortMeasure(@PathVariable(value = "workEffortMeasureId") String id){
        return Exec.exec("delete work-effort-measure", () -> this.workEffortMeasureService.deleteWorkEffortMeasure(id));
    }

}
