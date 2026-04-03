package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.mybatis.dao.WorkEffortTypeContentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WorkEffortTypeContentService {
    private final WorkEffortTypeContentDao workEffortTypeContentDao;

    @Autowired
    public WorkEffortTypeContentService(WorkEffortTypeContentDao workEffortTypeContentDao) {
        this.workEffortTypeContentDao = workEffortTypeContentDao;
    }

    public Map<String, String> getWorkEffortTypeContentParams(String workEffortTypeId, String contentId) {
        Map<String, String> workEffortTypeContentParams = workEffortTypeContentDao.getWorkEffortTypeContentParams(workEffortTypeId, contentId);
        return workEffortTypeContentParams;
    }
}
