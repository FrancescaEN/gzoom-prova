package it.mapsgroup.gzoom.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.*;
import it.mapsgroup.gzoom.service.WorkEffortPurposeAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "work-effort-purpose-account", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortPurposeAccountController {
    private final WorkEffortPurposeAccountService workEffortPurposeAccountService;

    @Autowired
    public WorkEffortPurposeAccountController(WorkEffortPurposeAccountService workEffortPurposeAccountService) {
        this.workEffortPurposeAccountService = workEffortPurposeAccountService;
    }

    @GetMapping("{glAccountId}")
    @ResponseBody
    public boolean existWorkEffortPurposeAccountByGlAccountId(@PathVariable String glAccountId) {
        return Exec.exec("exist work-effort-purpose-account by gl-account-id", () -> workEffortPurposeAccountService.existWorkEffortPurposeAccountByGlAccountId(glAccountId));
    }

    @PutMapping()
    @ResponseBody
    public boolean updateStatusValidChange(@RequestBody WorkEffortPurposeAccount[] req){
        return Exec.exec("update work-effort-purpose-account", () -> this.workEffortPurposeAccountService.updateWorkEffortPurposeAccount(req) );
    }


    @PostMapping("{glAccountId}/{workEffortPurposeTypeId}")
    @ResponseBody
    public boolean createWorkEffortPurposeAccount(@PathVariable String glAccountId, @PathVariable String[] workEffortPurposeTypeId, @RequestBody(required = false) String comments){
        return Exec.exec("create work-effort-purpose-type", () -> this.workEffortPurposeAccountService.createWorkEffortPurposeAccount(glAccountId, workEffortPurposeTypeId, comments) );
    }

    @DeleteMapping("{glAccountId}/{workEffortPurposeTypeId}")
    @ResponseBody
    public boolean deleteWorkEffortPurposeAccount(@PathVariable String glAccountId, @PathVariable String[] workEffortPurposeTypeId){
        return Exec.exec("delete work-effort-purpose-type", () -> this.workEffortPurposeAccountService.deleteWorkEffortPurposeAccount(glAccountId, workEffortPurposeTypeId) );
    }
}
