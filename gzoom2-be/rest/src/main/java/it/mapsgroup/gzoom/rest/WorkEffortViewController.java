package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortView;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortViewEx;
import it.mapsgroup.gzoom.service.WorkEffortViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "work-effort-view", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortViewController {
    private final WorkEffortViewService workEffortViewService;

    @Autowired
    public WorkEffortViewController(WorkEffortViewService workEffortViewService) {
        this.workEffortViewService = workEffortViewService;
    }

    @GetMapping(value = "/{organizationId}")
    @ResponseBody
    public Result<WorkEffortViewEx> getWorkEffortView(@PathVariable(value = "organizationId") String organizationId){
        return Exec.exec("get work-effort-view", () -> this.workEffortViewService.getWorkEffortView(organizationId) );
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    @ResponseBody
    public Result<WorkEffortView> getWorkEffortAssocPagination(@RequestBody Filter filter){
        return Exec.exec("get  work-effort-view-filter", () -> this.workEffortViewService.getWorkEffortViewFilter(filter) );
    }

}
