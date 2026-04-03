package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortStatusDao;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortStatus;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortStatusEx;
import it.mapsgroup.gzoom.mybatis.service.PermissionService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortStatusService {
	private static final Logger LOG = getLogger(WorkEffortStatusService.class);

	private final WorkEffortStatusDao workEffortStatusDao;
	private final PermissionService permissionService;

	@Autowired
    public WorkEffortStatusService(WorkEffortStatusDao workEffortStatusDao, PermissionService permissionService) {
        this.workEffortStatusDao = workEffortStatusDao;
        this.permissionService = permissionService;
    }

    public Result<WorkEffortStatus> getWorkEffortStatusList() {
        List<WorkEffortStatus> list = workEffortStatusDao.findAll();
        return new Result<>(list, list.size());
}

    public Result<WorkEffortStatusEx> getWorkEffortStatusEx(String workEffortId) {
        List<WorkEffortStatusEx> list = workEffortStatusDao.getWorkEffortStatusEx(workEffortId);
        return new Result<>(list, list.size());
    }

    public WorkEffortStatus createWorkEffortStatus(WorkEffortStatus req) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.WORK_EFFORT_STATUS, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getWorkEffortId(), msg.getMessageColumn(Messages.WORK_EFFORT_STATUS, Messages.WORK_EFFORT_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getWorkEffortId(), msg.getMessageColumn(Messages.WORK_EFFORT_STATUS, Messages.STATUS_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getWorkEffortId(), msg.getMessageColumn(Messages.WORK_EFFORT_STATUS, Messages.STATUS_DATE_TIME, Messages.IS_REQUIRED));
        WorkEffortStatus record = workEffortStatusDao.findById(req.getWorkEffortId(), req.getStatusId(), req.getStatusDatetime());
        Validators.assertTrue(record == null, Messages.WORK_EFFORT_STATUS_EXIST);
        return workEffortStatusDao.create(req, principal().getUserLoginId());
    }

    public int updateWorkEffortStatus(WorkEffortStatus req) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.WORK_EFFORT_STATUS, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getWorkEffortId(), msg.getMessageColumn(Messages.WORK_EFFORT_STATUS, Messages.WORK_EFFORT_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getWorkEffortId(), msg.getMessageColumn(Messages.WORK_EFFORT_STATUS, Messages.STATUS_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getWorkEffortId(), msg.getMessageColumn(Messages.WORK_EFFORT_STATUS, Messages.STATUS_DATE_TIME, Messages.IS_REQUIRED));
        return workEffortStatusDao.update(req, principal().getUserLoginId());
    }

    public int deleteWorkEffortStatus(String workEffortId, String statusId, Instant statusDatetime) {
        Messages msg = new Messages();
        WorkEffortStatus record = workEffortStatusDao.findById(workEffortId, statusId, statusDatetime);
        Validators.assertNotBlank(workEffortId, msg.getMessageColumn(Messages.WORK_EFFORT_STATUS, Messages.WORK_EFFORT_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(statusId, msg.getMessageColumn(Messages.WORK_EFFORT_STATUS, Messages.STATUS_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(statusDatetime.toString(), msg.getMessageColumn(Messages.WORK_EFFORT_STATUS, Messages.STATUS_DATE_TIME, Messages.IS_REQUIRED));
        return workEffortStatusDao.delete(record.getWorkEffortId(), record.getStatusId(), record.getStatusDatetime());
    }
}
