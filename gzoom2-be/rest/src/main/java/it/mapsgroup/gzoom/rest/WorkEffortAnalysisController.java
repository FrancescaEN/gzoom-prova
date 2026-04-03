package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAnalysis;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAnalysisEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import it.mapsgroup.gzoom.service.WorkEffortAnalysisService;
import static it.mapsgroup.gzoom.security.Principals.principal;

@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortAnalysisController {

    private final WorkEffortAnalysisService workEffortAnalysisService;

    @Autowired
    public WorkEffortAnalysisController(WorkEffortAnalysisService workEffortAnalysisService) {
        this.workEffortAnalysisService = workEffortAnalysisService;
    }

    @RequestMapping(value = "work-effort-analysis-id/{analysisId}", method = RequestMethod.GET)
    @ResponseBody
    public WorkEffortAnalysis getWorkEffortAnalysis(@PathVariable(value = "analysisId") String analysisId) {
        return Exec.exec("get work-effort-analysis with id", () -> workEffortAnalysisService.getWorkEffortAnalysis(analysisId));
    }

    @RequestMapping(value = "work-effort-analysis/{context}", method = RequestMethod.GET)
    @ResponseBody
    public Result<WorkEffortAnalysis> getWorkEffortAnalysisWithContext(@PathVariable(value = "context") String context) {
        return Exec.exec("get work-effort-analysis with id", () -> workEffortAnalysisService.getWorkEffortAnalysesWithContext(context, principal().getUserLoginId()));
    }

    @RequestMapping(value = "work-effort-analysis", method = RequestMethod.GET)
    @ResponseBody
    public Result<WorkEffortAnalysis> getWorkEffortAnalysisList() {
        return Exec.exec("get work-effort-analysis list", workEffortAnalysisService::getWorkEffortAnalysisList);
    }

    @RequestMapping(value = "work-effort-analysis/refDate&&workEffortTypeId", method = RequestMethod.POST)
    @ResponseBody
    public WorkEffortAnalysis getWorkEffortAnalysis(@RequestBody WorkEffortAnalysis req) {
        return Exec.exec("get work-effort-analysis with refDate and workEffortTypeId", () -> workEffortAnalysisService.getWorkEffortAnalysis(req));
    }

    @RequestMapping(value = "work-effort-analysis-ex/context/{context}", method = RequestMethod.GET)
    @ResponseBody
    public Result<WorkEffortAnalysisEx> getWorkEffortAnalysisEx(@PathVariable String context) {
        return Exec.exec("get work-effort-analysis with id", () -> workEffortAnalysisService.getWorkEffortAnalysisEx(context));
    }

    @RequestMapping(value = "work-effort-analysis-ex/{workEffortAnalysisId}", method = RequestMethod.GET)
    @ResponseBody
    public WorkEffortAnalysisEx getWorkEffortAnalysisExById(@PathVariable String workEffortAnalysisId) {
        return Exec.exec("get work-effort-analysis with id", () -> workEffortAnalysisService.getWorkEffortAnalysisExById(workEffortAnalysisId));
    }

    @PutMapping("work-effort-analysis")
    @ResponseBody
    public boolean updateWorkEffortAnalysis(@RequestBody WorkEffortAnalysis req){
        return Exec.exec("update work-effort-analysis", () -> this.workEffortAnalysisService.updateWorkEffortAnalysis(req) );
    }

    @DeleteMapping("work-effort-analysis/{workEffortAnalysisId}")
    @ResponseBody
    public boolean deleteWorkEffortAnalysis(@PathVariable String workEffortAnalysisId){
        return Exec.exec("delete work-effort-analysis list", () -> this.workEffortAnalysisService.deleteWorkEffortAnalysis(workEffortAnalysisId) );
    }

    @PostMapping("work-effort-analysis")
    @ResponseBody
    public boolean createWorkEffortAnalysis(@RequestBody WorkEffortAnalysis req){
        return Exec.exec("create work-effort-analysis", () -> this.workEffortAnalysisService.createWorkEffortAnalysis(req) );
    }
}
