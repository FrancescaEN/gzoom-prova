package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.*;
import it.mapsgroup.gzoom.mybatis.dto.*;
import it.mapsgroup.gzoom.mybatis.service.PermissionService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortService {
	private static final Logger LOG = getLogger(WorkEffortService.class);
	
	private final WorkEffortDao workEffortDao;
    private final WorkEffortContentDao workEffortContentDao;
    private  final WorkEffortTypeDao workEffortTypeDao;
    private final ContentDao contentDao;
    private final DataResourceDao dataResourceDao;
    private final NoteDataDao noteDataDao;
    private final WorkEffortNoteDao workEffortNoteDao;
    private final WorkEffortMeasureDao workEffortMeasureDao;
    private final AcctgTransEntryDao acctgTransEntryDao;
    private final AcctgTransDao acctgTransDao;
    private final WorkEffortMeasRatScDao workEffortMeasRatScDao;
    private final WorkEffortPartyAssignmentDao workEffortPartyAssignmentDao;
    private final WorkEffortAssocDao workEffortAssocDao;
    private final WorkEffortStatusDao workEffortStatusDao;
	private final PermissionService permissionService;

	@Autowired
    public WorkEffortService(WorkEffortDao workEffortDao, WorkEffortContentDao workEffortContentDao, WorkEffortTypeDao workEffortTypeDao, ContentDao contentDao, DataResourceDao dataResourceDao, NoteDataDao noteDataDao, WorkEffortNoteDao workEffortNoteDao, WorkEffortMeasureDao workEffortMeasureDao, AcctgTransEntryDao acctgTransEntryDao, AcctgTransDao acctgTransDao, WorkEffortMeasRatScDao workEffortMeasRatScDao, WorkEffortPartyAssignmentDao workEffortPartyAssignmentDao, WorkEffortAssocDao workEffortAssocDao, WorkEffortStatusDao workEffortStatusDao, PermissionService permissionService) {
        this.workEffortDao = workEffortDao;
        this.workEffortContentDao = workEffortContentDao;
        this.workEffortTypeDao = workEffortTypeDao;
        this.contentDao = contentDao;
        this.dataResourceDao = dataResourceDao;
        this.noteDataDao = noteDataDao;
        this.workEffortNoteDao = workEffortNoteDao;
        this.workEffortMeasureDao = workEffortMeasureDao;
        this.acctgTransEntryDao = acctgTransEntryDao;
        this.acctgTransDao = acctgTransDao;
        this.workEffortMeasRatScDao = workEffortMeasRatScDao;
        this.workEffortPartyAssignmentDao = workEffortPartyAssignmentDao;
        this.workEffortAssocDao = workEffortAssocDao;
        this.workEffortStatusDao = workEffortStatusDao;
        this.permissionService = permissionService;
    }

    public Result<WorkEffort> getWorkEfforts(String userLoginId, String parentTypeId, String[] workEffortTypeIds, String useFilter) {

        String userPreferenceOrganizationUnitId = this.permissionService.userPrefereceOrganizationUnitId(userLoginId);
        List<WorkEffort> list = workEffortDao.getWorkEfforts(userLoginId, parentTypeId, useFilter, workEffortTypeIds, userPreferenceOrganizationUnitId );
        return new Result<>(list, list.size());
    }

    public Result<WorkEffort> getWorkEffortsIsRootIsTemplate() {
        List<WorkEffort> list = workEffortDao.getWorkEffortsIsRootIsTemplate();
        return new Result<>(list, list.size());
    }
    
    public Result<WorkEffort> getWorkEfforts() {
        List<WorkEffort> list = workEffortDao.findAll();
        return new Result<>(list, list.size());
    }

    public WorkEffort getWorkEffort(String workEffortId){
	    return this.workEffortDao.findById(workEffortId);
    }

    public WorkEffortEx getWorkEffortEx(String workEffortId){
        return this.workEffortDao.getWorkEffortEx(workEffortId);
    }

    public Result<WorkEffort> getWorkEffortParents(String workEffortParentId) {
        List<WorkEffort> list = workEffortDao.getWorkEffortParents(workEffortParentId);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortEx> getWorkEffortExList(String userLoginId) {
        String userPreferenceOrganizationUnitId = this.permissionService.userPrefereceOrganizationUnitId(userLoginId);
        List<WorkEffortEx> list = workEffortDao.getWorkEffortExList(userPreferenceOrganizationUnitId);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortEx> getWorkEffortExListPagination(String userLoginId, InfoPage infoPage) {
        String userPreferenceOrganizationUnitId = this.permissionService.userPrefereceOrganizationUnitId(userLoginId);
        List<WorkEffortEx> list = workEffortDao.getWorkEffortExListPagination(userPreferenceOrganizationUnitId, infoPage);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffort> getWorkEffortByOrgId(String userLoginId) {
        String userPreferenceOrganizationUnitId = this.permissionService.userPrefereceOrganizationUnitId(userLoginId);
        List<WorkEffort> list = workEffortDao.getWorkEffortByOrgId(userPreferenceOrganizationUnitId);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortEx> getWorkEffortDropdown(String organizationId) {
        List<WorkEffortEx> list = workEffortDao.getWorkEffortDropdown(organizationId);
        return new Result<>(list, list.size());
    }

    public WorkEffort createWorkEffort(WorkEffort req) {
        Messages msg = new Messages();

        Validators.assertNotNull(req, msg.getMessageTable(Messages.WORK_EFFORT, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getWorkEffortId(), msg.getMessageColumn(Messages.WORK_EFFORT, Messages.WORK_EFFORT_ID, Messages.IS_REQUIRED));
        String userPreferenceOrganizationUnitId = this.permissionService.userPrefereceOrganizationUnitId(principal().getUserLoginId());
        WorkEffort record = workEffortDao.findById(req.getWorkEffortId());
        Validators.assertTrue(record == null, Messages.WORK_EFFORT_EXIST);
        return workEffortDao.create(req, principal().getUserLoginId(), userPreferenceOrganizationUnitId);
    }

    public int updateWorkEffort(WorkEffort workEffort) {
        Messages msg = new Messages();
        Validators.assertNotNull(workEffort, msg.getMessageTable(Messages.WORK_EFFORT, Messages.IS_REQUIRED));
        Validators.assertNotBlank(workEffort.getWorkEffortId(), msg.getMessageColumn(Messages.WORK_EFFORT, Messages.WORK_EFFORT_ID, Messages.IS_REQUIRED));
        String userPreferenceOrganizationUnitId = this.permissionService.userPrefereceOrganizationUnitId(principal().getUserLoginId());

        WorkEffortStatus workEffortStatus = new WorkEffortStatus();
        workEffortStatus.setWorkEffortId(workEffort.getWorkEffortId());
        workEffortStatus.setStatusId(workEffort.getCurrentStatusId());
        workEffortStatus.setStatusDatetime(Instant.now());
        this.workEffortStatusDao.create(workEffortStatus, principal().getUserLoginId());

        WorkEffort newWorkEffort = this.workEffortDao.getWorkEffort(workEffort.getWorkEffortId());
        newWorkEffort.setLastModifiedByUserLogin(principal().getUserLoginId());
        newWorkEffort.setOrganizationId(userPreferenceOrganizationUnitId);
        newWorkEffort.setWorkEffortId(workEffort.getWorkEffortId());
        newWorkEffort.setSourceReferenceId(workEffort.getSourceReferenceId());
        newWorkEffort.setWorkEffortName(workEffort.getWorkEffortName());
        newWorkEffort.setWorkEffortNameLang(workEffort.getWorkEffortNameLang());
        newWorkEffort.setWorkEffortTypeId(workEffort.getWorkEffortTypeId());
        newWorkEffort.setCurrentStatusId(workEffort.getCurrentStatusId());
        newWorkEffort.setEstimatedCompletionDate(workEffort.getEstimatedCompletionDate());
        newWorkEffort.setEstimatedStartDate(workEffort.getEstimatedStartDate());
        newWorkEffort.setOrgUnitId(workEffort.getOrgUnitId());
        newWorkEffort.setOrganizationId(workEffort.getOrganizationId());

        return workEffortDao.update(newWorkEffort, principal().getUserLoginId(), userPreferenceOrganizationUnitId);
    }

    public int deleteWorkEffort(String id) {
        Messages msg = new Messages();
        WorkEffort record = workEffortDao.getWorkEffort(id);
        Validators.assertNotBlank(id, msg.getMessageColumn(Messages.WORK_EFFORT, Messages.WORK_EFFORT_ID, Messages.IS_REQUIRED));

        return workEffortDao.delete(record.getWorkEffortId());
    }

    @Transactional
    public boolean deleteWorkEffortTree(String id) {
        Messages msg = new Messages();
        WorkEffort record = workEffortDao.getWorkEffort(id);
        Validators.assertNotBlank(id, msg.getMessageColumn(Messages.WORK_EFFORT, Messages.WORK_EFFORT_ID, Messages.IS_REQUIRED));

        WorkEffortType workEffortType = this.workEffortTypeDao.selectByPrimaryKey(record.getWorkEffortTypeId());
        if(!Objects.equals(workEffortType.getIsRoot(), "Y")){

            this.deleteWorkEffortTreeFunction(record);

        }else{

            List<WorkEffort> workEffortList = this.workEffortDao.getParentsbyWorkEffort(record.getWorkEffortId());
            workEffortList.forEach(workEffort -> {
                this.deleteWorkEffortTreeFunction(workEffort);
            });

        }

        return true;
    }

    private void deleteWorkEffortTreeFunction(WorkEffort record){
        List<WorkEffortContent> workEffortContentList = this.workEffortContentDao.getWorkEffortContentByWorkEffortId(record.getWorkEffortId());
        workEffortContentList.forEach( workEffortContent -> {
            Content content = this.contentDao.getContentById(workEffortContent.getContentId());
            this.dataResourceDao.delete(content.getDataResourceId());

            this.contentDao.delete(workEffortContent.getContentId());

            this.workEffortContentDao.delete(workEffortContent);
        });

        List<WorkEffortNote> workEffortNoteList = this.workEffortNoteDao.getWorkEffortNoteByWorkEffortId(record.getWorkEffortId());
        workEffortNoteList.forEach(workEffortNote -> {
            this.noteDataDao.delete(workEffortNote.getNoteId());
            this.workEffortNoteDao.delete(workEffortNote.getWorkEffortId(), workEffortNote.getNoteId());
        });

        List<WorkEffortMeasure> workEffortMeasureList = this.workEffortMeasureDao.getWorkEffortMeasureByWorkEffortId(record.getWorkEffortId());
        workEffortMeasureList.forEach(workEffortMeasure -> {
            List<AcctgTransEntry> acctgTransEntryList = this.acctgTransEntryDao.getAcctgTransEntryByWorkEffortMeasureId(workEffortMeasure.getWorkEffortMeasureId());
            acctgTransEntryList.forEach(this.acctgTransEntryDao::delete);

            List<AcctgTrans> acctgTransList = this.acctgTransDao.getAcctgTransByWorkEffortMeasureId(workEffortMeasure.getWorkEffortMeasureId());
            acctgTransList.forEach(this.acctgTransDao::delete);

            this.workEffortMeasRatScDao.deleteByMeasureId(workEffortMeasure.getWorkEffortMeasureId());

        });

        workEffortMeasureList.forEach(workEffortMeasure -> {this.workEffortMeasureDao.delete(workEffortMeasure.getWorkEffortMeasureId());});

        this.workEffortPartyAssignmentDao.deleteByWorkEffortId(record.getWorkEffortId());

        this.workEffortAssocDao.deleteByWorkEffortId(record.getWorkEffortId());

        this.workEffortDao.delete(record.getWorkEffortId());
    }

    public int anonymizeWorkEffort(Instant expirationDate) {
        return this.workEffortDao.anonymizeWorkEffort(expirationDate);
    }

    
}
