package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortRevisionDao;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortRevision;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortRevisionService {
	private static final Logger LOG = getLogger(WorkEffortRevisionService.class);

	private final WorkEffortRevisionDao workEffortRevisionDao;

	@Autowired
    public WorkEffortRevisionService(WorkEffortRevisionDao workEffortRevisionDao) {
        this.workEffortRevisionDao = workEffortRevisionDao;
    }

    public Result<WorkEffortRevision> getWorkEffortRevisions(String workEffortTypeId) {
        List<WorkEffortRevision> list = workEffortRevisionDao.getWorkEffortRevisions(workEffortTypeId);
        return new Result<>(list, list.size());
    }
}
