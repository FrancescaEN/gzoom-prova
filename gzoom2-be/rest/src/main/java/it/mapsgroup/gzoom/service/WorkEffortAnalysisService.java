package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;

import it.mapsgroup.gzoom.mybatis.dao.WorkEffortAnalysisDao;
import it.mapsgroup.gzoom.mybatis.dto.*;
import it.mapsgroup.gzoom.mybatis.service.PermissionService;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortAnalysisService {
    private static final Logger LOG = getLogger(WorkEffortAnalysisService.class);

    private final WorkEffortAnalysisDao workEffortAnalysisDao;
    private final ConfigurationImpl config;
    private final PermissionService permissionService;

    @Autowired
    public WorkEffortAnalysisService(WorkEffortAnalysisDao workEffortAnalysisDao, ConfigurationImpl config, PermissionService permissionService) {
        this.workEffortAnalysisDao = workEffortAnalysisDao;
        this.config = config;
        this.permissionService = permissionService;
    }

    public boolean deleteWorkEffortAnalysis (String workEffortAnalysisId) {
        Messages msg = new Messages();
        Validators.assertNotBlank(workEffortAnalysisId, msg.getMessageColumn(Messages.WORK_EFFORT_ANALYSIS, Messages.WORK_EFFORT_ANALYSIS_ID, Messages.IS_REQUIRED));
        WorkEffortAnalysis record = this.workEffortAnalysisDao.getWorkEffortAnalysis(workEffortAnalysisId);
        Validators.assertFalse(record == null, msg.getMessageTable(Messages.WORK_EFFORT_ANALYSIS, Messages.INVALID));
        return this.workEffortAnalysisDao.delete(workEffortAnalysisId);
    }

    public boolean updateWorkEffortAnalysis(WorkEffortAnalysis req) {
        this.validateWorkEffortAnalysis(req, "UPDATE");
        return this.workEffortAnalysisDao.update(req);

    }

    public boolean createWorkEffortAnalysis(WorkEffortAnalysis req) {
        this.validateWorkEffortAnalysis(req, "CREATE");
        return this.workEffortAnalysisDao.create(req);
    }

    private void validateWorkEffortAnalysis(WorkEffortAnalysis req, String method) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.WORK_EFFORT_ANALYSIS, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getWorkEffortAnalysisId(), msg.getMessageColumn(Messages.WORK_EFFORT_ANALYSIS, Messages.WORK_EFFORT_ANALYSIS_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getWorkEffortTypeId(), msg.getMessageColumn(Messages.WORK_EFFORT_ANALYSIS, Messages.WORK_EFFORT_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getDescription(), msg.getMessageColumn(Messages.WORK_EFFORT_ANALYSIS, Messages.DESCRIPTION, Messages.IS_REQUIRED));
        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getDescriptionLang(), msg.getMessageColumn(Messages.WORK_EFFORT_ANALYSIS, Messages.DESCRIPTION_LANG, Messages.IS_REQUIRED));
        }
        Validators.assertNotNull(req.getReferenceDate(), msg.getMessageColumn(Messages.WORK_EFFORT_ANALYSIS, Messages.REFERENCE_DATE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getAvailabilityId(), msg.getMessageColumn(Messages.WORK_EFFORT_ANALYSIS, Messages.AVAILABILITY_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getExcludeValidity(), msg.getMessageColumn(Messages.WORK_EFFORT_ANALYSIS, Messages.EXCLUDE_VALIDITY, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getDescription(), msg.getMessageColumn(Messages.WORK_EFFORT_ANALYSIS, Messages.DESCRIPTION, Messages.IS_REQUIRED));


        WorkEffortAnalysis record = this.workEffortAnalysisDao.getWorkEffortAnalysis(req.getWorkEffortAnalysisId());
        if ( method.equalsIgnoreCase("UPDATE") ||  method.equalsIgnoreCase("DELETE")) {
            Validators.assertFalse(record == null, msg.getMessageTable(Messages.WORK_EFFORT_ANALYSIS, Messages.INVALID));
        }
        if ( method.equalsIgnoreCase("CREATE")) {
            Validators.assertFalse(record != null, msg.getMessageTable(Messages.WORK_EFFORT_ANALYSIS, Messages.EXISTING));
        }

    }


    public WorkEffortAnalysis getWorkEffortAnalysis(String workEffortAnalysisId) {
        return workEffortAnalysisDao.getWorkEffortAnalysis(workEffortAnalysisId);
    }

    public Result<WorkEffortAnalysis> getWorkEffortAnalysesWithContext(String context, String userLoginId) {
        List<WorkEffortAnalysis> list = workEffortAnalysisDao.getWorkEffortAnalysesWithContext(context, userLoginId);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortAnalysis> getWorkEffortAnalysisList() {
        List<WorkEffortAnalysis> list = workEffortAnalysisDao.findAll();
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortAnalysisTypeTypeExt> getWorkEffortAnalysesTargetHeader(String analysisId, String workEffortId) {
        List<WorkEffortAnalysisTypeTypeExt> row = workEffortAnalysisDao.getWorkEffortAnalysisTargetHeader(analysisId, workEffortId);
        return new Result<>(row, row.size());
    }

    public Result<WorkEffortAnalysisTypeTypeExt> getWorkEffortAnalysisTargetSummary(String context, String analysisId, String userLoginId) {
        String organizationId = this.permissionService.userPrefereceOrganizationUnitId(userLoginId);
        List<WorkEffortAnalysisTypeTypeExt> list = workEffortAnalysisDao.getWorkEffortAnalysisTargetSummary(context, analysisId, userLoginId, organizationId);
        return new Result<>(list, list.size());
    }

    public Result<Score> getPdoScore(String workEffortId) {
        List<Score> list = workEffortAnalysisDao.getPdoScore(workEffortId);
        return new Result<>(list, list.size());
    }

    public Result<Score> getKPIscore(String workEffortId, Date analysisRefDate) {
        List<Score> list = workEffortAnalysisDao.getKPIscore(workEffortId, analysisRefDate.toInstant());
        return new Result<>(list, list.size());
    }

    public Result<Score> getDetailPdoScore(String workEffortId, String glFiscalTypeId) {
        List<Score> list = workEffortAnalysisDao.getDetailPdoScore(workEffortId, glFiscalTypeId);
        return new Result<>(list, list.size());
    }

    public Result<Score> getPdoAccount(String glAccountId) {
        List<Score> list = workEffortAnalysisDao.getPdoAccount(glAccountId);
        return new Result<>(list, list.size());
    }

    public Result<Score> getPdoAccount(String glAccountId, String workEffortMeasureId) {
        List<Score> list = workEffortAnalysisDao.getPdoAccount(glAccountId, workEffortMeasureId);
        return new Result<>(list, list.size());
    }

    public Result<Score> getPdoAccount(String glAccountId, String orgUnitRoleTypeId, String orgUnitId) {
        List<Score> list = workEffortAnalysisDao.getPdoAccount(glAccountId, orgUnitRoleTypeId, orgUnitId);
        return new Result<>(list, list.size());
    }

    public Result<Score> getPdoScorekpi(String workEffortMeasureId) {
        List<Score> list = workEffortAnalysisDao.getPdoScorekpi(workEffortMeasureId);
        return new Result<>(list, list.size());
    }

    public WorkEffortAnalysis getWorkEffortAnalysis(WorkEffortAnalysis workEffortAnalysis) {
        return workEffortAnalysisDao.getWorkEffortAnalysis(workEffortAnalysis.getReferenceDate(), workEffortAnalysis.getWorkEffortTypeId());
    }


    public Result<WorkEffortAnalysisEx> getWorkEffortAnalysisEx(String context) {
        if(ContextPermissionPrefixEnum.valueOf(context) != null) {
            List<WorkEffortAnalysisEx> list = workEffortAnalysisDao.getWorkEffortAnalysisEx(context);
            return new Result<>(list, list.size());
        }

        List<WorkEffortAnalysisEx> emptyList = new ArrayList<>();
        return new Result<>(emptyList, emptyList.size());

    }

    public WorkEffortAnalysisEx getWorkEffortAnalysisExById(String workEffortAnalysisId) {
        return workEffortAnalysisDao.getWorkEffortAnalysisExById(workEffortAnalysisId);
    }
}
