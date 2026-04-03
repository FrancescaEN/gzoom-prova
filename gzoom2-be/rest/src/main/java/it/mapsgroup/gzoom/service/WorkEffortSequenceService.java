package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortSequenceDao;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Leonardo Minaudo.
 */
@Service
public class WorkEffortSequenceService {
    private static final Logger LOG = getLogger(WorkEffortSequenceService.class);
    private final WorkEffortSequenceDao workEffortSequenceDao;

    @Autowired
    public WorkEffortSequenceService(WorkEffortSequenceDao workEffortSequenceDao) {
        this.workEffortSequenceDao = workEffortSequenceDao;
    }

    public Result<WorkEffortSequence> getWorkEffortSequence() {
        List<WorkEffortSequence> list = this.workEffortSequenceDao.selectAllOrderByPrimaryKey();
        return new Result<>(list, list.size());
    }

    public boolean createWorkEffortSequence(WorkEffortSequence req) {
        Validators.assertNotNull(req, Messages.WORK_EFFORT_SEQUENCE_REQUIRED);
        Validators.assertNotBlank(req.getSeqName(), Messages.WORK_EFFORT_SEQUENCE_NAME_REQUIRED);
        Validators.assertNotNull(req.getSeqId(), Messages.WORK_EFFORT_SEQUENCE_ID_REQUIRED);
        WorkEffortSequence record = workEffortSequenceDao.get(req.getSeqName());
        Validators.assertTrue(record == null, Messages.WORK_EFFORT_SEQUENCE_NAME_EXIST);
        return workEffortSequenceDao.create(req, principal().getUserLoginId());
    }

    public boolean updateWorkEffortSequence(WorkEffortSequence req) {
        Validators.assertNotBlank(req.getSeqName(), Messages.WORK_EFFORT_SEQUENCE_NAME_REQUIRED);
        Validators.assertNotNull(req.getSeqId(), Messages.WORK_EFFORT_SEQUENCE_ID_REQUIRED);
        WorkEffortSequence record = workEffortSequenceDao.get(req.getSeqName());
        Validators.assertNotNull(record, Messages.INVALID_WORK_EFFORT_SEQUENCE);
        return workEffortSequenceDao.update(req, principal().getUserLoginId());
    }

    public boolean deleteWorkEffortSequence(String id) {
        WorkEffortSequence record = workEffortSequenceDao.get(id);
        Validators.assertNotNull(record, Messages.INVALID_WORK_EFFORT_SEQUENCE);
        return workEffortSequenceDao.delete(id);
    }
}
