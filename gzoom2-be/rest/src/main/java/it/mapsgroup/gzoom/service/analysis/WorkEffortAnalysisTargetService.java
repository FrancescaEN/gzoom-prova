package it.mapsgroup.gzoom.service.analysis;

import it.mapsgroup.gzoom.mybatis.dao.WorkEffortAnalysisTargetDao;
import it.mapsgroup.gzoom.mybatis.dto.DetailKPI;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAnalysisTarget;
import it.mapsgroup.gzoom.mybatis.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.mapsgroup.gzoom.model.Result;

import java.util.List;

@Service
public class WorkEffortAnalysisTargetService {
    private WorkEffortAnalysisTargetDao workEffortAnalysisTargetDao;
    private final PermissionService permissionService;

    @Autowired
    public WorkEffortAnalysisTargetService(WorkEffortAnalysisTargetDao workEffortAnalysisTargetDao, PermissionService permissionService) {
        this.workEffortAnalysisTargetDao = workEffortAnalysisTargetDao;
        this.permissionService = permissionService;
    }


    public Result<WorkEffortAnalysisTarget> getWorkEffortAnalysisTargetHeaderOne(String analysisId, String workEffortId, String rangeDefault) {
        List<WorkEffortAnalysisTarget> list = workEffortAnalysisTargetDao.getWorkEffortAnalysisTargetHeaderOne(analysisId, workEffortId, rangeDefault);
        return  new Result<>(list, list.size());
    }

    public Result<WorkEffortAnalysisTarget> getWorkEffortAnalysisTargetHeaderMore(String context, String analysisId, String userLoginId) {
        String organizationId = permissionService.userPrefereceOrganizationUnitId(userLoginId);
        List<WorkEffortAnalysisTarget> list = workEffortAnalysisTargetDao.getWorkEffortAnalysisTargetHeaderMore(context, analysisId, userLoginId, organizationId);
        return  new Result<>(list, list.size());
    }

    public Result<WorkEffortAnalysisTarget> getWorkEffortAnalysisTargetList(String context, String analysisId, String userLoginId, String dateControl, String rangeDefault, String showOrgUnit) {
        String organizationId = permissionService.userPrefereceOrganizationUnitId(userLoginId);
        List<WorkEffortAnalysisTarget> list = workEffortAnalysisTargetDao.getWorkEffortAnalysisTargetList(context, analysisId, userLoginId, dateControl, rangeDefault, showOrgUnit, organizationId);
        return  new Result<>(list, list.size());
    }

    public Result<WorkEffortAnalysisTarget> getWorkEffortAnalysisTargetListWithWE(String analysisId, String workEffortId, String dateControl, String rangeDefault, String showOrgUnit) {
        List<WorkEffortAnalysisTarget> list = workEffortAnalysisTargetDao.getWorkEffortAnalysisTargetListWithWE( analysisId, workEffortId, dateControl, rangeDefault, showOrgUnit);
        return  new Result<>(list, list.size());
    }

    public Result<DetailKPI> getDetailKPIScore(String analysisId, String workEffortId, String dateControl, String rangeDefault, String[] purposeKPIList) {
        List<DetailKPI> list = workEffortAnalysisTargetDao.getDetailKPIScore( analysisId, workEffortId, dateControl, rangeDefault, purposeKPIList);
        return  new Result<>(list, list.size());
    }

    public Result<DetailKPI> getDetailKPIPeriod(String analysisId, String workEffortId, String dateControl, String rangeDefault, String[] purposeKPIList) {
        List<DetailKPI> list = workEffortAnalysisTargetDao.getDetailKPIPeriod( analysisId, workEffortId, dateControl, rangeDefault, purposeKPIList);
        return  new Result<>(list, list.size());
    }

}
