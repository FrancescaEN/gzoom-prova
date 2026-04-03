package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypePeriod;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortTypePeriodMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortTypePeriodDao extends AbstractDao{
    private static final Logger LOG = getLogger(WorkEffortTypePeriodDao.class);
    private final WorkEffortTypePeriodMapper workEffortTypePeriodMapper;

    @Autowired
    public WorkEffortTypePeriodDao(WorkEffortTypePeriodMapper workEffortTypePeriodMapper) {
        this.workEffortTypePeriodMapper = workEffortTypePeriodMapper;
    }

    /**
     * Gets a list of WorkEffortTypePeriod.
     *
     * @return
     */
    @Transactional
    public List<WorkEffortTypePeriod> getWorkEffortTypePeriodByWorkEffortTypeId(String workEffortTypeId) {
        LOG.info("find WorkEffortTypePeriod By WorkEffortTypeId");
        List<WorkEffortTypePeriod> workEffortTypePeriodList = this.workEffortTypePeriodMapper.getWorkEffortTypePeriodByWorkEffortTypeId(workEffortTypeId);
        LOG.info("size = {}", workEffortTypePeriodList.size());
        return workEffortTypePeriodList;
    }
}
