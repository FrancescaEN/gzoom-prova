package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPurposeType;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPurposeTypeEx;
import it.mapsgroup.gzoom.service.WorkEffortPurposeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "work-effort-purpose-type", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortPurposeTypeController {

    private final WorkEffortPurposeTypeService workEffortPurposeTypeService;

    @Autowired
    public WorkEffortPurposeTypeController(WorkEffortPurposeTypeService workEffortPurposeTypeService) {
        this.workEffortPurposeTypeService = workEffortPurposeTypeService;
    }

    @GetMapping
    @ResponseBody
    public Result<WorkEffortPurposeType> getWorkEffortPurposeTypeList(){
        return Exec.exec("get work-effort-purpose-type", this.workEffortPurposeTypeService::getWorkEffortPurposeTypeList);
    }


    @RequestMapping(value = "/{purposeTypeEnumId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<WorkEffortPurposeType> getWorkEffortPurposeTypeList(@PathVariable(value = "purposeTypeEnumId") String purposeTypeEnumId) {
        return Exec.exec("get work-effort-purpose-type by purpose-enum-id", () -> workEffortPurposeTypeService.getWorkEffortPurposeTypeListByPurposeTypeEnumId(purposeTypeEnumId));
    }

    @RequestMapping(value = "purpose-tab-type/{glAccountId}/{in}", method = RequestMethod.GET)
    @ResponseBody
    public Result<WorkEffortPurposeTypeEx> getPurposeTabType(@PathVariable String glAccountId, @PathVariable boolean in) {
        return Exec.exec("get work-effort-purpose-type by purpose-enum-id", () -> workEffortPurposeTypeService.getPurposeTabType(glAccountId, in));
    }

    @PostMapping
    @ResponseBody
    public boolean createWorkEffortPurposeType(@RequestBody WorkEffortPurposeType req){
        return Exec.exec("create work-effort-purpose-type", () -> this.workEffortPurposeTypeService.createWorkEffortPurposeType(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateWorkEffortPurposeType(@RequestBody WorkEffortPurposeType req){
        return Exec.exec("update work-effort-purpose-type", () -> this.workEffortPurposeTypeService.updateWorkEffortPurposeType(req) );
    }

    @DeleteMapping(value = "/{workEffortPurposeTypeId}")
    @ResponseBody
    public boolean deleteWorkEffortPurposeType(@PathVariable(value = "workEffortPurposeTypeId") String[] id){
        return Exec.exec("delete work-effort-purpose-type list", () -> this.workEffortPurposeTypeService.deleteWorkEffortPurposeType(id) );
    }

}
