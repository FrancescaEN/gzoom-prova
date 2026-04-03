package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortAssocDao;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssoc;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssocEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

/**
 * @author Leonardo Minaudo
 */
@Service
public class WorkEffortAssocService {

    private final Configuration config;
    private final WorkEffortAssocDao workEffortAssocDao;

    @Autowired
    public WorkEffortAssocService(Configuration config, WorkEffortAssocDao workEffortAssocDao) {
        this.config = config;
        this.workEffortAssocDao = workEffortAssocDao;
    }

    public  List<WorkEffortAssoc> getWorkEffortAssocList() {
        return this.workEffortAssocDao.findAll();
    }

    public int getTotale(InfoPage infoPage) {
        int tot = this.workEffortAssocDao.getTotale(infoPage);
        return tot;
    }


    public Result<WorkEffortAssocEx> getWorkEffortAssocPagination(InfoPage infoPage) {
        List<WorkEffortAssocEx> list = this.workEffortAssocDao.getWorkEffortAssocPagination(infoPage);
        Result<WorkEffortAssocEx> result = new Result<>(list, list.size());
        return result;
    }

    public boolean createWorkEffortAssoc(WorkEffortAssoc req) {
        Validators.assertNotNull(req, Messages.WORK_EFFORT_ASSOC_REQUIRED);
        Validators.assertNotBlank(req.getWorkEffortIdFrom(), Messages.WORK_EFFORT_ASSOC_ID_FROM_REQUIRED);
        Validators.assertNotBlank(req.getWorkEffortIdTo(), Messages.WORK_EFFORT_ASSOC_ID_TO_REQUIRED);
        Validators.assertNotBlank(req.getWorkEffortAssocTypeId(), Messages.WORK_EFFORT_ASSOC_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(req.getFromDate().toString(), Messages.WORK_EFFORT_ASSOC_FROM_DATE_REQUIRED);
        Validators.assertNotNull(req.getAssocWeight(), Messages.WORK_EFFORT_ASSOC_WEIGHT_REQUIRED);


        WorkEffortAssoc record = workEffortAssocDao.findById(req.getWorkEffortIdFrom(), req.getWorkEffortIdTo(), req.getWorkEffortAssocTypeId(), req.getFromDate());
        Validators.assertTrue(record == null, Messages.WORK_EFFORT_ASSOC_EXIST);

        return workEffortAssocDao.create(req, principal().getUserLoginId());
    }

    public int updateWorkEffortAssoc(WorkEffortAssoc req) {
        Validators.assertNotNull(req, Messages.WORK_EFFORT_ASSOC_REQUIRED);
        Validators.assertNotBlank(req.getWorkEffortIdFrom(), Messages.WORK_EFFORT_ASSOC_ID_FROM_REQUIRED);
        Validators.assertNotBlank(req.getWorkEffortIdTo(), Messages.WORK_EFFORT_ASSOC_ID_TO_REQUIRED);
        Validators.assertNotBlank(req.getWorkEffortAssocTypeId(), Messages.WORK_EFFORT_ASSOC_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(req.getFromDate().toString(), Messages.WORK_EFFORT_ASSOC_FROM_DATE_REQUIRED);
        Validators.assertNotNull(req.getAssocWeight(), Messages.WORK_EFFORT_ASSOC_WEIGHT_REQUIRED);

        WorkEffortAssoc record = workEffortAssocDao.findById(req.getWorkEffortIdFrom(), req.getWorkEffortIdTo(), req.getWorkEffortAssocTypeId(), req.getFromDate());
        Validators.assertNotNull(record, Messages.INVALID_WORK_EFFORT_ASSOC);
        return workEffortAssocDao.update(req, principal().getUserLoginId());
    }

    public int deleteWorkEffortAssoc(WorkEffortAssoc req) {
        Validators.assertNotNull(req, Messages.WORK_EFFORT_ASSOC_REQUIRED);
        Validators.assertNotBlank(req.getWorkEffortIdFrom(), Messages.WORK_EFFORT_ASSOC_ID_FROM_REQUIRED);
        Validators.assertNotBlank(req.getWorkEffortIdTo(), Messages.WORK_EFFORT_ASSOC_ID_TO_REQUIRED);
        Validators.assertNotBlank(req.getWorkEffortAssocTypeId(), Messages.WORK_EFFORT_ASSOC_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(req.getFromDate().toString(), Messages.WORK_EFFORT_ASSOC_FROM_DATE_REQUIRED);

        WorkEffortAssoc record = workEffortAssocDao.findById(req.getWorkEffortIdFrom(), req.getWorkEffortIdTo(), req.getWorkEffortAssocTypeId(), req.getFromDate());
        Validators.assertNotNull(record, Messages.INVALID_WORK_EFFORT_ASSOC);
        return workEffortAssocDao.delete(req.getWorkEffortIdFrom(), req.getWorkEffortIdTo(), req.getWorkEffortAssocTypeId(), req.getFromDate());
    }
}
