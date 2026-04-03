package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.mybatis.dao.WorkEffortTypePeriodDao;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypePeriod;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortTypePeriodService {
    private static final Logger LOG = getLogger(WorkEffortService.class);

    private final WorkEffortTypePeriodDao workEffortTypePeriodDao;

    @Autowired
    public WorkEffortTypePeriodService(WorkEffortTypePeriodDao workEffortTypePeriodDao) {
        this.workEffortTypePeriodDao = workEffortTypePeriodDao;
    }

    public  List<WorkEffortTypePeriod> getWorkEffortTypePeriodByWorkEffortTypeId(String workEffortTypeId) {
        List<WorkEffortTypePeriod> workEffortTypePeriodList = workEffortTypePeriodDao.getWorkEffortTypePeriodByWorkEffortTypeId(workEffortTypeId);
        return workEffortTypePeriodList;
    }

}
