package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortMeasureRatScView;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortMeasureRatScViewMapper;
import it.mapsgroup.gzoom.mybatis.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
public class WorkEffortMeasRatScViewDao extends AbstractDao{

    private final WorkEffortMeasureRatScViewMapper workEffortMeasureRatScViewMapper;
    private final FilterService filterService;

    @Autowired
    public WorkEffortMeasRatScViewDao(WorkEffortMeasureRatScViewMapper workEffortMeasureRatScViewMapper, FilterService filterService) {
        this.workEffortMeasureRatScViewMapper = workEffortMeasureRatScViewMapper;
        this.filterService = filterService;
    }

    public List<WorkEffortMeasureRatScView> findAll() {

        return this.workEffortMeasureRatScViewMapper.selectAll();
    }

    public List<WorkEffortMeasureRatScView> getWorkEffortMeasureRatScView(String workEffortMeasureId) {

        return this.workEffortMeasureRatScViewMapper.getWorkEffortMeasureRatScView(workEffortMeasureId);
    }

    public boolean create(WorkEffortMeasureRatScView workEffortMeasureRatScView) {

        this.workEffortMeasureRatScViewMapper.insert(workEffortMeasureRatScView);
        return true;
    }
}
