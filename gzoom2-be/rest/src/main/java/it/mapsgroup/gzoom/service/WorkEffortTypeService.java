package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortTypeService {
    private static final Logger LOG = getLogger(WorkEffortService.class);

    private final WorkEffortTypeDao workEffortTypeDao;

    @Autowired
    public WorkEffortTypeService(WorkEffortTypeDao workEffortTypeDao) {
        this.workEffortTypeDao = workEffortTypeDao;
    }

    public WorkEffortType getWorkEffortType(String workEffortTypeId) {
        WorkEffortType wet = workEffortTypeDao.selectByPrimaryKey(workEffortTypeId);
        return wet;
    }

    public Result<WorkEffortType> getAllWorkEffortType() {
        List<WorkEffortType> list = workEffortTypeDao.selectAll();
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortType> getLikeWorkEffortTypeId(String likeId) {
        List<WorkEffortType> list = workEffortTypeDao.selectLikeWorkEffortTypeId(likeId);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortType> getWorkEffortTypes(String workEffortTypeId) {
        List<WorkEffortType> list = workEffortTypeDao.getWorkEffortTypes(workEffortTypeId);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortType> getWorkEffortTypeByParentId(String parentTypeId) {
        List<WorkEffortType> list = workEffortTypeDao.getWorkEffortTypeByParentId(parentTypeId);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortType> getWorkEffortTypesParametric(String workEffortTypeId) {
        List<WorkEffortType> list = workEffortTypeDao.getWorkEffortTypesParametric(workEffortTypeId);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortType> getWorkEffortTypesIsRoot(String isRoot) {
        List<WorkEffortType> list = workEffortTypeDao.getWorkEffortTypesIsRoot(isRoot);
        return new Result<>(list, list.size());
    }

}
