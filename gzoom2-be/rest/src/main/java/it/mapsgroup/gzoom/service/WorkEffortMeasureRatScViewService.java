package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortMeasRatScViewDao;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortMeasureRatScView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Leonardo Minaudo
 */
@Service
public class WorkEffortMeasureRatScViewService {
    private final WorkEffortMeasRatScViewDao workEffortMeasureRatScViewDao;

    @Autowired
    public WorkEffortMeasureRatScViewService(WorkEffortMeasRatScViewDao workEffortMeasureRatScViewDao) {
        this.workEffortMeasureRatScViewDao = workEffortMeasureRatScViewDao;
    }

    public Result<WorkEffortMeasureRatScView> getWorkEffortMeasureRatScView(String workEffortMeasureId) {
        List<WorkEffortMeasureRatScView> list = this.workEffortMeasureRatScViewDao.getWorkEffortMeasureRatScView(workEffortMeasureId);
        return new Result<>(list, list.size());
    }
}
