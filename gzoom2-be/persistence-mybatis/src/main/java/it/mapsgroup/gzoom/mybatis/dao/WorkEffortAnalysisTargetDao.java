package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.DetailKPI;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAnalysisTarget;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortAnalysisTargetsMapper;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Mapper
@Repository
public class WorkEffortAnalysisTargetDao {

    private static final Logger LOG = getLogger(WorkEffortAnalysisTargetDao.class);
    private final WorkEffortAnalysisTargetsMapper workEffortAnalysisTargetsMapper;

    @Autowired
    public WorkEffortAnalysisTargetDao(WorkEffortAnalysisTargetsMapper workEffortAnalysisTargetsMapper) {
        this.workEffortAnalysisTargetsMapper = workEffortAnalysisTargetsMapper;

    }

    /**
     * This function gets the data for header.
     *
     * @param analysisId Analysis id.
     * @param workEffortId Work effort id.
     * @return List of WorkEffortAnalysisTarget
     */
    public List<WorkEffortAnalysisTarget> getWorkEffortAnalysisTargetHeaderOne(String analysisId, String workEffortId, String rangeDefault) {
        List<WorkEffortAnalysisTarget> ret = workEffortAnalysisTargetsMapper.getWorkEffortAnalysisTargetHeaderOne(analysisId, workEffortId, rangeDefault);
        LOG.info("size = {}", ret.size());
        return ret;
    }

    /**
     * This function gets the data for header.
     *
     * @param context Context.
     * @param analysisId Analysis id.
     * @param userLoginId User login id.
     * @return List of WorkEffortAnalysisTarget.
     */
    public List<WorkEffortAnalysisTarget> getWorkEffortAnalysisTargetHeaderMore(String context, String analysisId, String userLoginId, String organizationId) {
        String permission = ContextPermissionPrefixEnum.getPermissionPrefix(context);
        String permissionMGR_ADMIN = permission + "MGR_ADMIN";
        String permissionORG_ADMIN = permission + "ORG_ADMIN";
        List<WorkEffortAnalysisTarget> ret =  workEffortAnalysisTargetsMapper.getWorkEffortAnalysisTargetHeaderMore(analysisId, userLoginId, permissionMGR_ADMIN, permissionORG_ADMIN, organizationId);
        LOG.info("size = {}", ret.size());
        return ret;
    }

    /**
     * This function gets list of work efforts associated with the analysis and the context.
     *
     * @param context Context.
     * @param analysisId Analysis id.
     * @param userLoginId User login id.
     * @param dateControl dateControl from comments.
     * @return List of WorkEffortAnalysisTarget.
     */
    public List<WorkEffortAnalysisTarget> getWorkEffortAnalysisTargetList(String context, String analysisId, String userLoginId, String dateControl, String rangeDefault, String showOrgUnit, String organizationId) {
        String permission = ContextPermissionPrefixEnum.getPermissionPrefix(context);
        String permissionMGR_ADMIN = permission + "MGR_ADMIN";
        String permissionORG_ADMIN = permission + "ORG_ADMIN";
        List<WorkEffortAnalysisTarget> ret = workEffortAnalysisTargetsMapper.getWorkEffortAnalysisTargetList(analysisId, userLoginId, dateControl, permissionMGR_ADMIN, permissionORG_ADMIN, rangeDefault, showOrgUnit, organizationId);
        LOG.info("size = {}", ret.size());
        return ret;
    }

    /**
     * This function gets list of work efforts associated with the analysis and the work effort id.
     *
     * @param analysisId Analysis id.
     * @param workEffortId Work effort id.
     * @param dateControl dateControl from comments.
     * @return List of WorkEffortAnalysisTarget.
     */
    public List<WorkEffortAnalysisTarget> getWorkEffortAnalysisTargetListWithWE(String analysisId, String workEffortId, String dateControl, String rangeDefault, String showOrgUnit) {
        List<WorkEffortAnalysisTarget> ret = workEffortAnalysisTargetsMapper.getWorkEffortAnalysisTargetListWithWE(analysisId, workEffortId, dateControl, rangeDefault, showOrgUnit);
        LOG.info("size = {}", ret.size());
        return ret;
    }

    /**
     * This function gets the indicators when detailKPI from params equals SCORE.
     *
     * @param analysisId Analysis id.
     * @param workEffortId Work effort id.
     * @param dateControl dateControl from comments.
     * @return List of DetailKPI.
     */
    public List<DetailKPI> getDetailKPIScore(String analysisId, String workEffortId, String dateControl, String rangeDefault, String[] purposeKPIList) {
        List<DetailKPI> ret = workEffortAnalysisTargetsMapper.getDetailKPIScore(analysisId, workEffortId, dateControl, rangeDefault, purposeKPIList);
        LOG.info("size = {}", ret.size());
        return ret;
    }

    /**
     * This function gets the indicators when detailKPI from params equals PERIOD.
     *
     * @param analysisId Analysis id.
     * @param workEffortId Work effort id.
     * @param dateControl dateControl from comments.
     * @return List of DetailKPI.
     */
    public List<DetailKPI> getDetailKPIPeriod(String analysisId, String workEffortId, String dateControl, String rangeDefault, String[] purposeKPIList) {
        List<DetailKPI> ret = workEffortAnalysisTargetsMapper.getDetailKPIPeriod(analysisId, workEffortId, dateControl, rangeDefault, purposeKPIList);
        LOG.info("size = {}", ret.size());
        return ret;
    }
}
