package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortRevision;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortRevisionMapper;
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
public class WorkEffortRevisionDao extends AbstractDao{
    private static final Logger LOG = getLogger(WorkEffortRevisionDao.class);
    private final WorkEffortRevisionMapper workEffortRevisionMapper;

    @Autowired
    public WorkEffortRevisionDao(WorkEffortRevisionMapper workEffortRevisionMapper) {
        this.workEffortRevisionMapper = workEffortRevisionMapper;
    }
    
    @Transactional
    public List<WorkEffortRevision> getWorkEffortRevisions(String workEffortTypeId) {
        LOG.info("find all WorkEffortRevision");

        List<WorkEffortRevision> workEffortRevisionList = this.workEffortRevisionMapper.getWorkEffortRevisions(workEffortTypeId);
        LOG.info("size = {}", (workEffortRevisionList.size()));
        return workEffortRevisionList;
    }

}
