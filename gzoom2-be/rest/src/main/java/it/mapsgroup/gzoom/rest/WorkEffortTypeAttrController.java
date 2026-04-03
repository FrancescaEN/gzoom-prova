package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypeAttr;
import it.mapsgroup.gzoom.service.WorkEffortTypeAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "work-effort-type-attr", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortTypeAttrController {

    private final WorkEffortTypeAttrService workEffortTypeAttrService;

    @Autowired
    public WorkEffortTypeAttrController(WorkEffortTypeAttrService workEffortTypeAttrService) {
        this.workEffortTypeAttrService = workEffortTypeAttrService;
    }

    @GetMapping(value = "/{workEffortId}")
    @ResponseBody
    public Result<WorkEffortTypeAttr> getWorkEffortTypeAttrList(@PathVariable(value = "workEffortId") String workEffortId){
        return Exec.exec("get workEffortTypeAttrList", () -> this.workEffortTypeAttrService.getWorkEffortTypeAttrList(workEffortId));
    }

    @GetMapping()
    @ResponseBody
    public Result<WorkEffortTypeAttr> getWorkEffortTypeAttrList(){
        return Exec.exec("get workEffortTypeAttrListAll", () -> this.workEffortTypeAttrService.getWorkEffortTypeAttrListAll());
    }
}
