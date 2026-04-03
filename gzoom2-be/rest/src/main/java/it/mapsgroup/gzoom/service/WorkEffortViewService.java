package it.mapsgroup.gzoom.service;


import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortViewDao;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortView;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortViewEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Leonardo Minaudo
 */
@Service
public class WorkEffortViewService {
    private final WorkEffortViewDao workEffortViewDao;
    private final Configuration config;

    @Autowired
    public WorkEffortViewService(WorkEffortViewDao workEffortViewDao, Configuration config) {
        this.workEffortViewDao = workEffortViewDao;
        this.config = config;
    }

    public Result<WorkEffortViewEx> getWorkEffortView(String organizationId) {
        List<WorkEffortViewEx> list = this.workEffortViewDao.getWorkEffortView(organizationId);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortView> getWorkEffortViewFilter(Filter filter) {
        List<WorkEffortView> list = this.workEffortViewDao.getWorkEffortViewFilter(filter.getSecondValue(), filter.getField(), filter.getValue());
        return new Result<>(list, list.size());
    }
}
