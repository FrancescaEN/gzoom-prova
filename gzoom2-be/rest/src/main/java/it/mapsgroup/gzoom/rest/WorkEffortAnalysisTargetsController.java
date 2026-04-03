package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.DetailKPI;
import it.mapsgroup.gzoom.mybatis.dto.Score;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAnalysisTarget;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAnalysisTypeTypeExt;
import it.mapsgroup.gzoom.service.WorkEffortAnalysisService;
import it.mapsgroup.gzoom.service.analysis.WorkEffortAnalysisTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static it.mapsgroup.gzoom.security.Principals.principal;

@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortAnalysisTargetsController {
    private final WorkEffortAnalysisService workEffortAnalysisService;

    private final WorkEffortAnalysisTargetService workEffortAnalysisTargetService;

    @Autowired
    public WorkEffortAnalysisTargetsController(WorkEffortAnalysisService workEffortAnalysisService, WorkEffortAnalysisTargetService workEffortAnalysisTargetService) {
        this.workEffortAnalysisService = workEffortAnalysisService;
        this.workEffortAnalysisTargetService = workEffortAnalysisTargetService;
    }

    @RequestMapping(value = "work-effort-analysis-targets-header/{analysisId}/{workEffortId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<WorkEffortAnalysisTypeTypeExt> getWorkEffortAnalysisTargetHeader(@PathVariable(value = "analysisId") String analysisId, @PathVariable(value = "workEffortId") String workEffortId) {
        return Exec.exec("get work-effort-analysis with id", () -> workEffortAnalysisService.getWorkEffortAnalysesTargetHeader(analysisId, workEffortId));
    }

    @RequestMapping(value = "work-effort-analysis-targets/{context}/{analysisId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<WorkEffortAnalysisTypeTypeExt> getWorkEffortAnalysisTargetSummary(@PathVariable(value = "context") String context, @PathVariable(value = "analysisId") String analysisId) {
        return Exec.exec("get work-effort-analysis with id", () -> workEffortAnalysisService.getWorkEffortAnalysisTargetSummary(context, analysisId, principal().getUserLoginId()));
    }

    @RequestMapping(value = "work-effort-analysis-targets/header/{analysisId}/{workEffortId}/{rangeDefault}", method = RequestMethod.GET)
    @ResponseBody
    public Result<WorkEffortAnalysisTarget> getWorkEffortAnalysisTargetHeaderOne(@PathVariable(value = "analysisId") String analysisId, @PathVariable(value = "workEffortId") String workEffortId, @PathVariable("rangeDefault") String rangeDefault) {
        return Exec.exec("get work-effort-analysis with id analysis and id work effort", () -> workEffortAnalysisTargetService.getWorkEffortAnalysisTargetHeaderOne(analysisId, workEffortId, rangeDefault));
    }

    @RequestMapping(value = "work-effort-analysis-targets/header-more/{context}/{analysisId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<WorkEffortAnalysisTarget> getWorkEffortAnalysisTargetHeaderMore(@PathVariable(value = "context") String context, @PathVariable(value = "analysisId") String analysisId) {
        return Exec.exec("get work-effort-analysis header more", () -> workEffortAnalysisTargetService.getWorkEffortAnalysisTargetHeaderMore(context, analysisId, principal().getUserLoginId()));
    }

    @RequestMapping(value = "work-effort-analysis-targets/list/{context}/{analysisId}/{dateControl}/{rangeDefault}/{showOrgUnit}", method = RequestMethod.GET)
    @ResponseBody
    public Result<WorkEffortAnalysisTarget> getWorkEffortAnalysisTargetList(@PathVariable(value = "context") String context, @PathVariable(value = "analysisId") String analysisId, @PathVariable(value = "dateControl") String dateControl, @PathVariable("rangeDefault") String rangeDefault, @PathVariable("showOrgUnit") String showOrgUnit) {
        return Exec.exec("get work-effort-analysis target list", () -> workEffortAnalysisTargetService.getWorkEffortAnalysisTargetList(context, analysisId, principal().getUserLoginId(), dateControl, rangeDefault, showOrgUnit));
    }

    @RequestMapping(value = "work-effort-analysis-targets/list-with-work-effort/{analysisId}/{workEffortId}/{dateControl}/{rangeDefault}/{showOrgUnit}", method = RequestMethod.GET)
    @ResponseBody
    public Result<WorkEffortAnalysisTarget> getWorkEffortAnalysisTargetListWithWE( @PathVariable(value = "analysisId") String analysisId, @PathVariable(value = "workEffortId") String workEffortId, @PathVariable(value = "dateControl") String dateControl, @PathVariable("rangeDefault") String rangeDefault, @PathVariable("showOrgUnit") String showOrgUnit) {
        return Exec.exec("get work-effort-analysis target list with work-effort-id", () -> workEffortAnalysisTargetService.getWorkEffortAnalysisTargetListWithWE(analysisId, workEffortId, dateControl, rangeDefault, showOrgUnit));
    }

    @RequestMapping(value = "work-effort-analysis-targets/detailKPIScore/{analysisId}/{workEffortId}/{dateControl}/{rangeDefault}/{purposeKPIList}", method = RequestMethod.GET)
    @ResponseBody
    public Result<DetailKPI> getDetailKPIScore(@PathVariable(value = "analysisId") String analysisId, @PathVariable(value = "workEffortId") String workEffortId, @PathVariable(value = "dateControl") String dateControl, @PathVariable("rangeDefault") String rangeDefault, @PathVariable("purposeKPIList") String[] purposeKPIList) {
        return Exec.exec("get detail-KPI-score", () -> workEffortAnalysisTargetService.getDetailKPIScore(analysisId, workEffortId, dateControl, rangeDefault, purposeKPIList));
    }

    @RequestMapping(value = "work-effort-analysis-targets/detailKPIScore/{analysisId}/{workEffortId}/{dateControl}/{rangeDefault}/", method = RequestMethod.GET)
    @ResponseBody
    public Result<DetailKPI> getDetailKPIScore(@PathVariable(value = "analysisId") String analysisId, @PathVariable(value = "workEffortId") String workEffortId, @PathVariable(value = "dateControl") String dateControl, @PathVariable("rangeDefault") String rangeDefault) {
        return Exec.exec("get detail-KPI-score", () -> workEffortAnalysisTargetService.getDetailKPIScore(analysisId, workEffortId, dateControl, rangeDefault, new String[0]));
    }

    @RequestMapping(value = "work-effort-analysis-targets/detailKPIPeriod/{analysisId}/{workEffortId}/{dateControl}/{rangeDefault}/{purposeKPIList}", method = RequestMethod.GET)
    @ResponseBody
    public Result<DetailKPI> getDetailKPIPeriod(@PathVariable(value = "analysisId") String analysisId, @PathVariable(value = "workEffortId") String workEffortId, @PathVariable(value = "dateControl") String dateControl, @PathVariable("rangeDefault") String rangeDefault, @PathVariable("purposeKPIList") String[] purposeKPIList) {
        return Exec.exec("get detail-KPI-period", () -> workEffortAnalysisTargetService.getDetailKPIPeriod(analysisId, workEffortId, dateControl, rangeDefault, purposeKPIList));
    }

    @RequestMapping(value = "work-effort-analysis-targets/detailKPIPeriod/{analysisId}/{workEffortId}/{dateControl}/{rangeDefault}/", method = RequestMethod.GET)
    @ResponseBody
    public Result<DetailKPI> getDetailKPIPeriod(@PathVariable(value = "analysisId") String analysisId, @PathVariable(value = "workEffortId") String workEffortId, @PathVariable(value = "dateControl") String dateControl, @PathVariable("rangeDefault") String rangeDefault) {
        return Exec.exec("get detail-KPI-period", () -> workEffortAnalysisTargetService.getDetailKPIPeriod(analysisId, workEffortId, dateControl, rangeDefault, new String[0]));
    }

    @RequestMapping(value = "work-effort-analysis-targets/pdoScore/{workEffortId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Score> getPdoScore(@PathVariable(value = "workEffortId") String workEffortId) {
        return Exec.exec("get pdoScore", () -> workEffortAnalysisService.getPdoScore(workEffortId));
    }

    @RequestMapping(value = "work-effort-analysis-targets/KPIscore/{workEffortId}/{analysisRefDate}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Score> getKPIscore(@PathVariable(value = "workEffortId") String workEffortId, @PathVariable(value = "analysisRefDate") Date analysisRefDate) {
        return Exec.exec("get KPIscore", () -> workEffortAnalysisService.getKPIscore(workEffortId, analysisRefDate));
    }

    @RequestMapping(value = "work-effort-analysis-targets/detailPdoScore/{workEffortId}/{glFiscalTypeId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Score> getDetailPdoScore(@PathVariable(value = "workEffortId") String workEffortId, @PathVariable(value = "glFiscalTypeId") String glFiscalTypeId) {
        return Exec.exec("get detailPdoScore", () -> workEffortAnalysisService.getDetailPdoScore(workEffortId, glFiscalTypeId));
    }

    @RequestMapping(value = "work-effort-analysis-targets/pdoAccount/{glAccountId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Score> getPdoAccount(@PathVariable(value = "glAccountId") String glAccountId) {
        return Exec.exec("get pdoAccount(glAccountId)", () -> workEffortAnalysisService.getPdoAccount(glAccountId));
    }

    @RequestMapping(value = "work-effort-analysis-targets/pdoAccount/{glAccountId}/{workEffortMeasureId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Score> getPdoAccount(@PathVariable(value = "glAccountId") String glAccountId, @PathVariable(value = "workEffortMeasureId") String workEffortMeasureId) {
        return Exec.exec("get pdoAccount(glAccountId, workEffortMeasureId)", () -> workEffortAnalysisService.getPdoAccount(glAccountId, workEffortMeasureId));
    }

    @RequestMapping(value = "work-effort-analysis-targets/pdoAccount/{glAccountId}/{orgUnitRoleTypeId}/{orgUnitId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Score> getPdoAccount(@PathVariable(value = "glAccountId") String glAccountId, @PathVariable(value = "orgUnitRoleTypeId") String orgUnitRoleTypeId, @PathVariable(value = "orgUnitId") String orgUnitId) {
        return Exec.exec("get pdoAccount(glAccountId, orgUnitRoleTypeId, orgUnitId)", () -> workEffortAnalysisService.getPdoAccount(glAccountId, orgUnitRoleTypeId, orgUnitId));
    }

    @RequestMapping(value = "work-effort-analysis-targets/pdoScorekpi/{workEffortMeasureId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Score> getPdoScorekpi(@PathVariable(value = "workEffortMeasureId") String workEffortMeasureId) {
        return Exec.exec("get pdoScorekpi", () -> workEffortAnalysisService.getPdoScorekpi(workEffortMeasureId));
    }

}
