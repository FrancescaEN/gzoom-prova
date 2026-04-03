package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortDao;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortTypeAttrDao;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypeAttr;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkEffortTypeAttrService {

    private final WorkEffortTypeAttrDao workEffortTypeAttrDao;
    private final WorkEffortDao workEffortDao;

    @Autowired
    public WorkEffortTypeAttrService(WorkEffortTypeAttrDao workEffortTypeAttrDao, WorkEffortDao workEffortDao) {
        this.workEffortTypeAttrDao = workEffortTypeAttrDao;
        this.workEffortDao = workEffortDao;
    }

    public Result<WorkEffortTypeAttr> getWorkEffortTypeAttrList(String workEffortId) {
        WorkEffort workEffort = this.workEffortDao.getWorkEffort(workEffortId);
        List<WorkEffortTypeAttr> list = this.workEffortTypeAttrDao.getWorkEffortTypeAttrList(workEffort.getWorkEffortTypeId());
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortTypeAttr> getWorkEffortTypeAttrListAll() {
        List<WorkEffortTypeAttr> list = this.workEffortTypeAttrDao.getWorkEffortTypeAttrListAll();
        return new Result<>(list, list.size());
    }
}
