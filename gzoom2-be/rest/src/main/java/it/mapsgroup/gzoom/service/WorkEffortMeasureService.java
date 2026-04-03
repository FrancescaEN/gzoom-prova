package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.*;
import it.mapsgroup.gzoom.mybatis.dto.*;
import it.mapsgroup.gzoom.mybatis.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

/**
 * @author Leonardo Minaudo
 */
@Service
public class WorkEffortMeasureService {
    private final WorkEffortMeasureDao workEffortMeasureDao;
    private final WorkEffortMeasRatScViewDao workEffortMeasureRatScViewDao;
    private final WorkEffortMeasRatScDao workEffortMeasRatScDao;
    private final WorkEffortDao workEffortDao;
    private final GlAccountDao glAccountDao;
    private final UserPreferenceService userPreferenceService;
    private final Configuration config;
    private final PermissionService permissionService;

    @Autowired
    public WorkEffortMeasureService(WorkEffortMeasureDao workEffortMeasureDao, WorkEffortMeasRatScViewDao workEffortMeasureRatScViewDao, WorkEffortMeasRatScDao workEffortMeasRatScDao, WorkEffortDao workEffortDao, GlAccountDao glAccountDao, UserPreferenceService userPreferenceService, Configuration config, PermissionService permissionService) {
        this.workEffortMeasureDao = workEffortMeasureDao;
        this.workEffortMeasureRatScViewDao = workEffortMeasureRatScViewDao;
        this.workEffortMeasRatScDao = workEffortMeasRatScDao;
        this.workEffortDao = workEffortDao;
        this.glAccountDao = glAccountDao;
        this.userPreferenceService = userPreferenceService;
        this.config = config;
        this.permissionService = permissionService;
    }

    public Result<WorkEffortMeasure> getWeMeasureEvalId(String workEffortIdFrom) {
        List<WorkEffortMeasure> list = this.workEffortMeasureDao.getWeMeasureEvalId(workEffortIdFrom);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortMeasure> dropdownWorkEffortMeasure() {
        String organizationId = this.permissionService.userPrefereceOrganizationUnitId(principal().getUserLoginId());
        List<WorkEffortMeasure> list = this.workEffortMeasureDao.dropdownWorkEffortMeasure(organizationId);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortMeasure> dropdownWorkEffortMeasureWEA() {
        String organizationId = this.permissionService.userPrefereceOrganizationUnitId(principal().getUserLoginId());
        List<WorkEffortMeasure> list = this.workEffortMeasureDao.dropdownWorkEffortMeasureWEA(organizationId);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortMeasExUom> getWorkEffortMeasureList() {
        List<WorkEffortMeasExUom> list = this.workEffortMeasureDao.getWorkEffortMeasureList();
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortMeasure> getWorkEffortMeasureJoinWorkEffort(String query, boolean secondaryLang) {
        List<WorkEffortMeasure> list = this.workEffortMeasureDao.getWorkEffortMeasureJoinWorkEffort(query, secondaryLang);
        return new Result<>(list, list.size());
    }


    public int getTotale(InfoPage infoPage) {
        int tot = this.workEffortMeasureDao.getTotale(infoPage);
        return tot;
    }
    public Result<WorkEffortMeasExUom> getWorkEffortMeasureListPagination(InfoPage infoPage) {
        infoPage.setOrganizationId(this.userPreferenceService.getOrganizationId());
        List<WorkEffortMeasExUom> list = this.workEffortMeasureDao.getWorkEffortMeasureListPagination( infoPage);
        Result<WorkEffortMeasExUom> result = new Result<>(list, list.size());
        return result;
    }

    public WorkEffortMeasure getWorkEffortMeasure(String workEffortMeasureId){
        return workEffortMeasureDao.findById(workEffortMeasureId);
    }

    @Transactional
    public WorkEffortMeasExUom createWorkEffortMeasure(WorkEffortMeasure req) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.WORK_EFFORT_MEASURE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getWorkEffortId(), msg.getMessageColumn(Messages.WORK_EFFORT_MEASURE, Messages.WORK_EFFORT_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getGlAccountId(), msg.getMessageColumn(Messages.WORK_EFFORT_MEASURE, Messages.GL_ACCOUNT_ID, Messages.IS_REQUIRED));

        WorkEffort workEffort = this.workEffortDao.getWorkEffort(req.getWorkEffortId());
        GlAccount glAccount = this.glAccountDao.getGlAccount(req.getGlAccountId());
        Validators.assertNotNull(workEffort, msg.getMessageTable(Messages.WORK_EFFORT_ID, Messages.INVALID));
        Validators.assertNotNull(glAccount, msg.getMessageTable(Messages.GL_ACCOUNT_ID, Messages.INVALID));

        req.setFromDate(workEffort.getEstimatedStartDate());
        req.setThruDate(workEffort.getEstimatedCompletionDate());
        req.setKpiScoreWeight((double) 0);
        req.setKpiOtherWeight((double) 0);
        req.setWeMeasureTypeEnumId(glAccount.getWeMeasureTypeEnumId());
        req.setSequenceId(BigDecimal.valueOf(1));
        req.setPeriodTypeId(glAccount.getPeriodTypeId());
        req.setWeScoreConvEnumId(glAccount.getWeScoreConvEnumId());
        req.setWeWithoutPerf(glAccount.getWeWithoutPerf());
        req.setWeScoreRangeEnumId(glAccount.getWeScoreRangeEnumId());
        req.setUomRangeId(glAccount.getUomRangeId());

        if(req.getThruDate() != null) Validators.assertIsBefore(Date.from(req.getFromDate()), Date.from(req.getThruDate()), msg.getMessagesWithSpace(Messages.WORK_EFFORT_MEASURE, Messages.FROM_DATE_BIG_THAN_THRU_DATE));
        String newId = workEffortMeasureDao.create(req, principal().getUserLoginId());
        if (newId != null){
            WorkEffortMeasExUom newRecord = workEffortMeasureDao.getWorkEffortMeasureExUomById(newId);
            List<WorkEffortMeasureRatScView> workEffortMeasureRatScViewList = workEffortMeasureRatScViewDao.getWorkEffortMeasureRatScView(newRecord.getWorkEffortMeasureId());

            if (workEffortMeasureRatScViewList.size() > 0) {
                workEffortMeasureRatScViewList.forEach(x -> {
                    WorkEffortMeasRatSc workEffortMeasRatSc = new WorkEffortMeasRatSc(x);

                    workEffortMeasRatScDao.create(workEffortMeasRatSc, principal().getUserLoginId());
                });
            }
            return newRecord;
        }
        return null;
    }

    public boolean updateWorkEffortMeasure(WorkEffortMeasure req) {
        return workEffortMeasureDao.update(req, principal().getUserLoginId());
    }

    @Transactional
    public boolean deleteWorkEffortMeasure(String id) {
        WorkEffortMeasExUom record = workEffortMeasureDao.getWorkEffortMeasureExUomById(id);
        Validators.assertNotNull(record, Messages.INVALID_GL_RESOURCE_TYPE);

        List<WorkEffortMeasRatSc> workEffortMeasRatSc = workEffortMeasRatScDao.getWorkEffortMeasRatSc(id);
        if (workEffortMeasRatSc.size() > 0) {
            this.workEffortMeasRatScDao.deleteByMeasureId(id);
        }
        
        return workEffortMeasureDao.delete(id);
    }
}
