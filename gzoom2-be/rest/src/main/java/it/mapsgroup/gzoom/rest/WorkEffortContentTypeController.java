package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortContentType;
import it.mapsgroup.gzoom.service.WorkEffortContentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "work-effort-content-type", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortContentTypeController {

    private final WorkEffortContentTypeService workEffortContentTypeService;

    @Autowired
    public WorkEffortContentTypeController(WorkEffortContentTypeService workEffortContentTypeService) {
        this.workEffortContentTypeService = workEffortContentTypeService;
    }

    @GetMapping
    @ResponseBody
    public Result<WorkEffortContentType> getWorkEffortContentTypeList(){
        return Exec.exec("get work-effort-content-type", this.workEffortContentTypeService::getWorkEffortContentTypeList);
    }

    @PostMapping
    @ResponseBody
    public boolean createWorkEffortContentType(@RequestBody WorkEffortContentType req){
        return Exec.exec("create work-effort-content-type", () -> this.workEffortContentTypeService.createWorkEffortContentType(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateWorkEffortContentType(@RequestBody WorkEffortContentType req){
        return Exec.exec("update work-effort-content-type", () -> this.workEffortContentTypeService.updateWorkEffortContentType(req) );
    }

    @DeleteMapping(value = "/{workEffortContentTypeId}")
    @ResponseBody
    public boolean deleteWorkEffortContentType(@PathVariable(value = "workEffortContentTypeId") String[] id){
        return Exec.exec("delete work-effort-content-type list", () -> this.workEffortContentTypeService.deleteWorkEffortContentType(id) );
    }

    @GetMapping (value = "/{workEffortId}")
    @ResponseBody
    public Result<WorkEffortContentType> getContentTypeList(@PathVariable(value = "workEffortId") String workEffortId){
        return Exec.exec("get content-type-list", () -> this.workEffortContentTypeService.getContentTypeList(workEffortId) );
    }

}
