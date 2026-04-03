package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssocType;
import it.mapsgroup.gzoom.service.WorkEffortAssocTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "work-effort-assoc-type", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortAssocTypeController {

    private final WorkEffortAssocTypeService workEffortAssocTypeService;

    @Autowired
    public WorkEffortAssocTypeController(WorkEffortAssocTypeService workEffortAssocTypeService) {
        this.workEffortAssocTypeService = workEffortAssocTypeService;
    }

    @GetMapping
    @ResponseBody
    public Result<WorkEffortAssocType> getWorkEffortAssocType(){
        return Exec.exec("get work-effort-assoc-type", this.workEffortAssocTypeService::getWorkEffortAssocType);
    }

    @PostMapping
    @ResponseBody
    public boolean createWorkEffortAssocType(@RequestBody WorkEffortAssocType req){
        return Exec.exec("create work-effort-assoc-type", () -> this.workEffortAssocTypeService.createWorkEffortAssocType(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateWorkEffortAssocType(@RequestBody WorkEffortAssocType req){
        return Exec.exec("update work-effort-assoc-type", () -> this.workEffortAssocTypeService.updateWorkEffortAssocType(req) );
    }

    @DeleteMapping(value = "/{workEffortAssocTypeId}")
    @ResponseBody
    public boolean deleteWorkEffortAssocType(@PathVariable(value = "workEffortAssocTypeId") String[] id){
        return Exec.exec("delete work-effort-assoc-type list", () -> this.workEffortAssocTypeService.deleteWorkEffortAssocType(id) );
    }


}
