package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.ReportActivity;
import it.mapsgroup.gzoom.mybatis.mapper.ReportActivityMapper;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import it.mapsgroup.gzoom.persistence.common.dto.enumeration.ReportActivityStatus;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ReportActivityDao extends AbstractDao {
    private static final Logger LOG = getLogger(ReportActivityDao.class);
    private final ReportActivityMapper reportActivityMapper;
    private final SequenceGenerator sequenceGenerator;

    @Autowired
    public ReportActivityDao(ReportActivityMapper reportActivityMapper, SequenceGenerator sequenceGenerator) {
        this.reportActivityMapper = reportActivityMapper;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Transactional
    public boolean create(ReportActivity reportActivity) {
        LOG.info("create reportActivity");
        setCreatedTimestamp(reportActivity);
        String newId = this.sequenceGenerator.getNextSeqId(ReportActivity.class.getSimpleName());
        reportActivity.setActivityId(newId);
        int result = this.reportActivityMapper.insert(reportActivity);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public List<ReportActivity> getActivities(String userLoginId) {
        LOG.info("find all activities by createdByUserLogin");

        List<ReportActivity> reportActivities = this.reportActivityMapper.selectByCreatedByUserLoginOrderByCreatedStamp(userLoginId);
        LOG.info("size = {}", reportActivities.size());
        return reportActivities;
    }

    @Transactional
    public List<ReportActivity> getActivities(List<ReportActivityStatus> states) {
        LOG.info("getActivities");

        List<String> stateString = new ArrayList<>();
        for (ReportActivityStatus state : states ) {
            stateString.add(state.toString());
        }

        List<ReportActivity> reportActivities = this.reportActivityMapper.getActivitiesByStates(stateString);
        LOG.info("size = {}", reportActivities.size());
        return reportActivities;
    }

    @Transactional
    public ReportActivity get(String activityId) {
        LOG.info("find reportActivity by id");

        ReportActivity reportActivity = this.reportActivityMapper.selectByPrimaryKey(activityId);
        LOG.info("ReportActivity = {}", (reportActivity != null));
        return reportActivity;
    }

    @Transactional
    public boolean updateState(String id,
                               ReportActivityStatus src,
                               ReportActivityStatus dest,
                               String error,
                               String objectInfo) {

        int result = this.reportActivityMapper.updateState(id, src.toString(), dest.toString(), error, objectInfo, (ReportActivityStatus.DONE.equals(dest))? Instant.now() : null);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean updateState(String id,
                               ReportActivityStatus src,
                               ReportActivityStatus dest) {
        return updateState(id, src, dest, null, null);

    }

    @Transactional
    public int resumeRunning() {
        return this.reportActivityMapper.resumeRunning(ReportActivityStatus.RUNNING.toString(), ReportActivityStatus.QUEUED.toString(), "Y");
    }
}
