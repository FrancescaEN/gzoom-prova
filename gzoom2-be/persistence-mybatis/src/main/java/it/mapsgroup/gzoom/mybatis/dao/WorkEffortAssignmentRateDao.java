package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssignmentRateEx;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortAssignmentRateMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortAssignmentRateDao extends AbstractDao{
    private static final Logger LOG = getLogger(WorkEffortAssignmentRateDao.class);
    private final WorkEffortAssignmentRateMapper workEffortAssignmentRateMapper;

    @Autowired
    public WorkEffortAssignmentRateDao(WorkEffortAssignmentRateMapper workEffortAssignmentRateMapper) {
        this.workEffortAssignmentRateMapper = workEffortAssignmentRateMapper;
    }

    @Transactional
    public  List<WorkEffortAssignmentRateEx> getWorkEffortAssignmentRateExs(String workEffortId) {
        LOG.info("find all WorkEffortAssignmentRateEx");

        List<WorkEffortAssignmentRateEx> workEffortAssignmentRateExList = this.workEffortAssignmentRateMapper.getWorkEffortAssignmentRateExs(workEffortId);
        LOG.info("size = {}", workEffortAssignmentRateExList.size());
        return workEffortAssignmentRateExList;
    }


}
