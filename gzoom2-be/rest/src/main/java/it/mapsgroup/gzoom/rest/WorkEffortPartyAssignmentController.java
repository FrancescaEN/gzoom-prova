package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.UserPreference;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPartyAssignment;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPartyAssignmentEx;
import it.mapsgroup.gzoom.service.UserPreferenceService;
import it.mapsgroup.gzoom.service.WorkEffortPartyAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Alex Tivoli
 */
@RestController
@RequestMapping(value = "work-effort-party-assignment", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortPartyAssignmentController {

    private WorkEffortPartyAssignmentService workEffortPartyAssignmentService;
    private UserPreferenceService userPreferenceService;

    @Autowired
    public WorkEffortPartyAssignmentController(WorkEffortPartyAssignmentService workEffortPartyAssignmentService, UserPreferenceService userPreferenceService) {
        this.workEffortPartyAssignmentService = workEffortPartyAssignmentService;
        this.userPreferenceService = userPreferenceService;
    }

    @GetMapping
    @ResponseBody
    public Result<WorkEffortPartyAssignmentEx> getWorkEffortPartyAssignment() {
        UserPreference organization = userPreferenceService.getUserPreference("ORGANIZATION_PARTY");
        return Exec.exec("work-effort-party-assignment get", () -> workEffortPartyAssignmentService.getWorkEffortPartyAssignmentList(organization));
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    @ResponseBody
    public Result<WorkEffortPartyAssignmentEx>getWorkEffortAssocPagination(@RequestBody InfoPage infoPage){
        UserPreference organization = userPreferenceService.getUserPreference("ORGANIZATION_PARTY");
        return Exec.exec("get work-effort-assoc-pagination", () -> this.workEffortPartyAssignmentService.getWorkEffortPartyAssignmentListFilter(infoPage, organization) );
    }

    @PostMapping
    @ResponseBody
    public boolean createWorkEffortPartyAssignment(@RequestBody WorkEffortPartyAssignment req){
        return Exec.exec("create work-effort-party-assignment", () -> this.workEffortPartyAssignmentService.createWorkEffortPartyAssignment(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateWorkEffortPartyAssignment(@RequestBody WorkEffortPartyAssignment req){

        return Exec.exec("update work-effort-party-assignment", () -> this.workEffortPartyAssignmentService.updateWorkEffortPartyAssignment(req) );
    }

    @PostMapping(value="/delete")
    @ResponseBody
    public boolean deleteWorkEffortPartyAssignment(@RequestBody WorkEffortPartyAssignment req){
        System.out.print(req.getFromDate());
        return Exec.exec("delete work-effort-party-assignment", () -> this.workEffortPartyAssignmentService.deleteWorkEffortPartyAssignment(req) );
    }
}
