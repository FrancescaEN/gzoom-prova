package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortMeasRatSc;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortMeasRatScExUomRatingScale;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortMeasRatScMapper;
import it.mapsgroup.gzoom.mybatis.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkEffortMeasRatScDao extends AbstractDao{

    private final WorkEffortMeasRatScMapper workEffortMeasRatScMapper;
    private final FilterService filterService;

    @Autowired
    public WorkEffortMeasRatScDao(WorkEffortMeasRatScMapper workEffortMeasRatScMapper, FilterService filterService) {
        this.workEffortMeasRatScMapper = workEffortMeasRatScMapper;
        this.filterService = filterService;
    }

    public List<WorkEffortMeasRatSc> findAll() {

        return this.workEffortMeasRatScMapper.selectAll();
    }

    public WorkEffortMeasRatSc findById(String workEffortMeasureId, String uomId, Double uomRatingValue) {

        return this.workEffortMeasRatScMapper.selectByPrimaryKey(workEffortMeasureId, uomId, uomRatingValue);
    }

    public List<WorkEffortMeasRatScExUomRatingScale> getRatingScaleWEM(String workEffortIdFrom) {

        return this.workEffortMeasRatScMapper.getRatingScaleWEM(workEffortIdFrom);
    }

    public List<WorkEffortMeasRatSc> getWorkEffortMeasRatSc(String workEffortMeasureId) {

        return this.workEffortMeasRatScMapper.getWorkEffortMeasRatSc(workEffortMeasureId);
    }

    @Transactional
    public boolean create(WorkEffortMeasRatSc workEffortMeasRatSc, String userLoginId) {
        workEffortMeasRatSc.setCreatedByUserLogin(userLoginId);
        this.workEffortMeasRatScMapper.insert(workEffortMeasRatSc);
        return true;
    }
    @Transactional
    public int update(WorkEffortMeasRatSc workEffortMeasRatSc, String userLoginId) {
        workEffortMeasRatSc.setLastModifiedByUserLogin(userLoginId);
        return this.workEffortMeasRatScMapper.updateByPrimaryKey(workEffortMeasRatSc);}

    @Transactional
    public boolean deleteByMeasureId(String workEffortMeasureId ) {
        return this.workEffortMeasRatScMapper.deleteByMeasureId(workEffortMeasureId);
    }


    @Transactional
    public int delete(String workEffortMeasureId, String uomId, Double uomRatingValue) {
        return this.workEffortMeasRatScMapper.deleteByPrimaryKey(workEffortMeasureId, uomId, uomRatingValue);}
}
