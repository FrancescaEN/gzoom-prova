package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.Score;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAnalysis;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAnalysisEx;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAnalysisTypeTypeExt;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortAnalysisMapper;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortAnalysisDao extends AbstractDao {
    private static final Logger LOG = getLogger(WorkEffortAnalysisDao.class);
    private final WorkEffortAnalysisMapper workEffortAnalysisMapper;

    @Autowired
    public WorkEffortAnalysisDao(WorkEffortAnalysisMapper workEffortAnalysisMapper) {
        this.workEffortAnalysisMapper = workEffortAnalysisMapper;
    }

    @Transactional
    public boolean create(WorkEffortAnalysis workEffortAnalysis) {
        LOG.info("create workEffortAnalysis");
        setCreatedTimestamp(workEffortAnalysis);
        int result = this.workEffortAnalysisMapper.insert(workEffortAnalysis);
        LOG.info("result = {}", result);
        return result > 0;
    }


    @Transactional
    public boolean update(WorkEffortAnalysis workEffortAnalysis) {
        LOG.info("update workEffortAnalysis");
        setUpdateTimestamp(workEffortAnalysis);
        int result = this.workEffortAnalysisMapper.updateByPrimaryKey(workEffortAnalysis);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String workEffortAnalysisId) {
        LOG.info("delete workEffortAnalysis");
        int result = this.workEffortAnalysisMapper.deleteByPrimaryKey(workEffortAnalysisId);
        LOG.info("result = {}", result);
        return result > 0;
    }


    /**
     * This function gets the work effort analyses with a context.
     *
     * @param context Context.
     * @param userLoginId User login id.
     * @return List of work effort analysis.
     */
    @Transactional
    public List<WorkEffortAnalysis> getWorkEffortAnalysesWithContext(String context, String userLoginId) {
        LOG.info("getWorkEffortAnalysesWithContext");

        String permission = ContextPermissionPrefixEnum.getPermissionPrefix(context);
        List<WorkEffortAnalysis> workEffortAnalyses = this.workEffortAnalysisMapper.getWorkEffortAnalysesWithContext(context, userLoginId, permission);
        LOG.info("size = {}", workEffortAnalyses.size());
        return workEffortAnalyses;
    }

    @Transactional
    public WorkEffortAnalysis getWorkEffortAnalysis(String workEffortAnalysisId) {
        LOG.info("find workEffortAnalysis by id");
        WorkEffortAnalysis workEffortAnalysis = this.workEffortAnalysisMapper.selectByPrimaryKey(workEffortAnalysisId);
        LOG.info("WorkEffortAnalysis = {}", (workEffortAnalysis != null));
        return workEffortAnalysis;
    }

    @Transactional
    public List<WorkEffortAnalysis> findAll() {
        LOG.info("find all workEffortAnalysis");
        List<WorkEffortAnalysis> workEffortAnalyses = this.workEffortAnalysisMapper.selectAll();
        LOG.info("size = {}", workEffortAnalyses.size());
        return workEffortAnalyses;
    }

    /**
     * This function gets the information for the header by having the workEffortId.
     *
     * @param analysisId Analysis id.
     * @param workEffortId Work effort id
     * @return List of WorkEffortAnalysisTypeTypeExt.
     */
    @Transactional
    public List<WorkEffortAnalysisTypeTypeExt> getWorkEffortAnalysisTargetHeader(String analysisId, String workEffortId) {
        LOG.info("getWorkEffortAnalysisTargetHeader");

        List<WorkEffortAnalysisTypeTypeExt> workEffortAnalysisTypeTypeExts = this.workEffortAnalysisMapper.getWorkEffortAnalysisTargetHeader(analysisId, workEffortId);
        LOG.info("size = {}", workEffortAnalysisTypeTypeExts.size());
        return workEffortAnalysisTypeTypeExts;
    }

    /**
     * This function get the summary objectives of the analysis.
     *
     * @param context Context.
     * @param analysisId Analysis id.
     * @param userLoginId User login id.
     * @return List of WorkEffortAnalysisTypeTypeExt.
     */
    @Transactional
    public List<WorkEffortAnalysisTypeTypeExt> getWorkEffortAnalysisTargetSummary(String context, String analysisId, String userLoginId, String organizationId) {
        LOG.info("getWorkEffortAnalysisTargetSummary");

        String permission = ContextPermissionPrefixEnum.getPermissionPrefix(context);
        List<WorkEffortAnalysisTypeTypeExt> workEffortAnalysisTypeTypeExts = this.workEffortAnalysisMapper.getWorkEffortAnalysisTargetSummary(permission, analysisId, userLoginId, organizationId);
        LOG.info("size = {}", workEffortAnalysisTypeTypeExts.size());
        return workEffortAnalysisTypeTypeExts;
    }

    @Transactional
    public WorkEffortAnalysis getWorkEffortAnalysis(Instant refDate, String workEffortTypeId) {
        LOG.info("getWorkEffortAnalysis");

        List<WorkEffortAnalysis> workEffortAnalysis = this.workEffortAnalysisMapper.getWorkEffortAnalysis(refDate, workEffortTypeId);
        LOG.info("size = {}", workEffortAnalysis.size());
        return workEffortAnalysis.isEmpty() ? null : workEffortAnalysis.get(0);
    }

    @Transactional
    public List<Score> getPdoScore(String workEffortId) {
        LOG.info("getPdoScore");

        List<Score> scores = this.workEffortAnalysisMapper.getPdoScore(workEffortId);
        LOG.info("size = {}", scores.size());
        return scores;
    }

    @Transactional
    public List<Score> getKPIscore(String workEffortId, Instant analysisRefDate) {
        LOG.info("getKPIscore");

        List<Score> scores = this.workEffortAnalysisMapper.getKPIscore(workEffortId, analysisRefDate);
        LOG.info("size = {}", scores.size());
        return scores;
    }

    @Transactional
    public List<Score> getDetailPdoScore(String workEffortId, String glFiscalTypeId) {
        LOG.info("getDetailPdoScore");

        List<Score> scores = this.workEffortAnalysisMapper.getDetailPdoScore(workEffortId, glFiscalTypeId);
        LOG.info("size = {}", scores.size());
        return scores;
    }

    @Transactional
    public List<Score> getPdoAccount(String glAccountId, String orgUnitRoleTypeId, String orgUnitId) {
        LOG.info("getPdoAccount");

        List<Score> scores = this.workEffortAnalysisMapper.getPdoAccount(glAccountId, orgUnitRoleTypeId, orgUnitId, null);
        LOG.info("size = {}", scores.size());
        return scores;
    }

    @Transactional
    public List<Score> getPdoAccount(String glAccountId, String workEffortMeasureId) {
        LOG.info("getPdoAccount");

        List<Score> scores = this.workEffortAnalysisMapper.getPdoAccount(glAccountId, null, null, workEffortMeasureId);
        LOG.info("size = {}", scores.size());
        return scores;
    }

    @Transactional
    public List<Score> getPdoAccount(String glAccountId) {
        LOG.info("getPdoAccount");

        List<Score> scores = this.workEffortAnalysisMapper.getPdoAccount(glAccountId, null, null, null);
        LOG.info("size = {}", scores.size());
        return scores;
    }

    @Transactional
    public List<Score> getPdoScorekpi(String workEffortMeasureId){
        LOG.info("getPdoScorekpi");

        List<Score> scores = this.workEffortAnalysisMapper.getPdoScorekpi(workEffortMeasureId);
        LOG.info("size = {}", scores.size());
        return scores;
    }


    @Transactional
    public List<WorkEffortAnalysisEx> getWorkEffortAnalysisEx(String context) {
        LOG.info("getWorkEffortAnalysisEx");
        List<WorkEffortAnalysisEx> workEffortAnalysisEx = this.workEffortAnalysisMapper.getWorkEffortAnalysisEx(context);
        LOG.info("size = {}", workEffortAnalysisEx.size());
        return workEffortAnalysisEx;
    }

    @Transactional
    public WorkEffortAnalysisEx getWorkEffortAnalysisExById(String workEffortAnalysisId) {
        LOG.info("getWorkEffortAnalysisExById");
        WorkEffortAnalysisEx workEffortAnalysis = this.workEffortAnalysisMapper.getWorkEffortAnalysisExById(workEffortAnalysisId);
        LOG.info("WorkEffortAnalysisEx = {}", (workEffortAnalysis != null));
        return workEffortAnalysis;
    }

}
