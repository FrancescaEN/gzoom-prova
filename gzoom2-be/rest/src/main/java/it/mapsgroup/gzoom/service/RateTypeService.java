package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.RateTypeDao;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortAssignmentRateDao;
import it.mapsgroup.gzoom.mybatis.dto.RateType;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssignmentRateEx;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RateTypeService {

    private static final Logger LOG = getLogger(PartyTypeService.class);

    private final RateTypeDao rateTypeDao;
    private final WorkEffortAssignmentRateDao workEffortAssignmentRateDao;

    @Autowired
    public RateTypeService(RateTypeDao rateTypeDao, WorkEffortAssignmentRateDao workEffortAssignmentRateDao) {
        this.rateTypeDao = rateTypeDao;
        this.workEffortAssignmentRateDao = workEffortAssignmentRateDao;
    }

    public Result<RateType> getRateTypes() {
        List<RateType> list = rateTypeDao.getRateTypes();
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortAssignmentRateEx> getRateTypesWorkEffortId(String workEffortId) {
        List<WorkEffortAssignmentRateEx> list = workEffortAssignmentRateDao.getWorkEffortAssignmentRateExs(workEffortId);
        return new Result<>(list, list.size());
    }
}
