package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortSequence;
import it.mapsgroup.gzoom.service.WorkEffortSequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "work-effort-sequence", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortSequenceController {
    private final WorkEffortSequenceService workEffortSequenceService;

    @Autowired
    public WorkEffortSequenceController(WorkEffortSequenceService workEffortSequenceService) {
        this.workEffortSequenceService = workEffortSequenceService;
    }

    @GetMapping
    @ResponseBody
    public Result<WorkEffortSequence> getWorkEffortSequence(){
        return Exec.exec("get work-effort-sequence", this.workEffortSequenceService::getWorkEffortSequence);
    }

    @PostMapping
    @ResponseBody
    public boolean createWorkEffortSequence(@RequestBody WorkEffortSequence req){
        return Exec.exec("create work-effort-sequence", () -> this.workEffortSequenceService.createWorkEffortSequence(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateWorkEffortSequence(@RequestBody WorkEffortSequence req){
        return Exec.exec("update work-effort-sequence", () -> this.workEffortSequenceService.updateWorkEffortSequence(req) );
    }

    @DeleteMapping(value = "/{workEffortSequenceName}")
    @ResponseBody
    public boolean deleteWorkEffortSequence(@PathVariable(value = "workEffortSequenceName") String id){
        return Exec.exec("delete work-effort-sequence list", () -> this.workEffortSequenceService.deleteWorkEffortSequence(id) );
    }

}
