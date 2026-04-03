package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortPartyAssignmentDao;
import it.mapsgroup.gzoom.mybatis.dto.UserPreference;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPartyAssignment;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPartyAssignmentEx;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

/**
 * @author Alex Tivoli
 */
@Service
public class WorkEffortPartyAssignmentService {

    private final Configuration config;
    private final WorkEffortPartyAssignmentDao workEffortPartyAssignmentDao;

    public WorkEffortPartyAssignmentService(Configuration config, WorkEffortPartyAssignmentDao workEffortPartyAssignmentDao) {
        this.config = config;
        this.workEffortPartyAssignmentDao = workEffortPartyAssignmentDao;
    }

    public Result<WorkEffortPartyAssignmentEx> getWorkEffortPartyAssignmentList(UserPreference organization) {
        List<WorkEffortPartyAssignmentEx> list = this.workEffortPartyAssignmentDao.getWorkEffortPartyAssignmentList(organization);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortPartyAssignmentEx> getWorkEffortPartyAssignmentListFilter(InfoPage infoPage, UserPreference organization) {
        List<WorkEffortPartyAssignmentEx> list = this.workEffortPartyAssignmentDao.getWorkEffortPartyAssignmentListFilter(infoPage, organization);
        return new Result<>(list, list.size());
    }

    public boolean createWorkEffortPartyAssignment(WorkEffortPartyAssignment req) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.WORK_EFFORT_PARTY_ASSIGNMENT, Messages.IS_REQUIRED) );

        return workEffortPartyAssignmentDao.create(req, principal().getUserLoginId());
    }

    public boolean updateWorkEffortPartyAssignment(WorkEffortPartyAssignment req) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.WORK_EFFORT_PARTY_ASSIGNMENT, Messages.IS_REQUIRED) );

        WorkEffortPartyAssignment record = workEffortPartyAssignmentDao.get(req.getWorkEffortId(),req.getRoleTypeId(),req.getPartyId(),req.getFromDate());
        Validators.assertNotNull(record, msg.getMessageTable(Messages.WORK_EFFORT_PARTY_ASSIGNMENT, Messages.INVALID) );
        return workEffortPartyAssignmentDao.update(req, principal().getUserLoginId());
    }

    public boolean deleteWorkEffortPartyAssignment(WorkEffortPartyAssignment element) {
        Messages msg = new Messages();
        String id = element.getWorkEffortId();
        if (id != null ) {
            if(id.length() < 3 || !id.substring(0, 3).equals("new")){
                Validators.assertNotBlank(id, msg.getMessageTable(Messages.WORK_EFFORT_PARTY_ASSIGNMENT, Messages.IS_REQUIRED) );
                Validators.assertNotNull(element, Messages.WORK_EFFORT_ID);
                WorkEffortPartyAssignment record = workEffortPartyAssignmentDao.get(element.getWorkEffortId(),element.getRoleTypeId(),element.getPartyId(),element.getFromDate());
                Validators.assertNotNull(record, msg.getMessageTable(Messages.WORK_EFFORT_PARTY_ASSIGNMENT, Messages.INVALID) );
                workEffortPartyAssignmentDao.delete(element);
            }}
        return true;
    }


}
