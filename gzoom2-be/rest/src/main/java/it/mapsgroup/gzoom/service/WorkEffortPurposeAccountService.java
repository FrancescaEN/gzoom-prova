package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortPurposeAccountDao;

import it.mapsgroup.gzoom.mybatis.dto.StatusValidChange;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPurposeAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class WorkEffortPurposeAccountService {
    private final WorkEffortPurposeAccountDao workEffortPurposeAccountDao;
    @Autowired
    public WorkEffortPurposeAccountService(WorkEffortPurposeAccountDao workEffortPurposeAccountDao) {
        this.workEffortPurposeAccountDao = workEffortPurposeAccountDao;
    }

    public boolean existWorkEffortPurposeAccountByGlAccountId(String glAccountId) {
        return this.workEffortPurposeAccountDao.countByGlAccountId(glAccountId) > 0;
    }

    @Transactional
    public boolean deleteWorkEffortPurposeAccount(String glAccountId, String[] workEffortPurposeTypeId) {
        Messages msg = new Messages();
        for (int i = 0; i < workEffortPurposeTypeId.length ; i++) {
            boolean result = this.workEffortPurposeAccountDao.deleteByPrimaryKey(workEffortPurposeTypeId[i], glAccountId);
            Validators.assertTrue(result, msg.getMessageTable(Messages.WORK_EFFORT_PURPOSE_ACCOUNT, "Cannot delete records"));
        }
        return true;
    }


    @Transactional
    public boolean updateWorkEffortPurposeAccount(WorkEffortPurposeAccount[] req) {
        Messages msg = new Messages();
        for (int i = 0; i < req.length; i++) {
            Validators.assertNotNull(req[i], msg.getMessageTable(Messages.WORK_EFFORT_PURPOSE_ACCOUNT, Messages.IS_REQUIRED));
            WorkEffortPurposeAccount record = this.workEffortPurposeAccountDao.getWorkEffortPurposeAccount(req[i].getWorkEffortPurposeTypeId(), req[i].getGlAccountId());
            Validators.assertFalse(record == null, msg.getMessageTable(Messages.WORK_EFFORT_PURPOSE_ACCOUNT, Messages.INVALID));
            this.workEffortPurposeAccountDao.update(req[i], principal().getUserLoginId());
        }
        return  true;
    }

    @Transactional
    public boolean createWorkEffortPurposeAccount(String glAccountId, String[] workEffortPurposeTypeId, String comments) {
        Messages msg = new Messages();
        for (int i = 0; i < workEffortPurposeTypeId.length ; i++) {
            WorkEffortPurposeAccount workEffortPurposeAccount = new WorkEffortPurposeAccount();
            workEffortPurposeAccount.setGlAccountId(glAccountId);
            workEffortPurposeAccount.setWorkEffortPurposeTypeId(workEffortPurposeTypeId[i]);
            workEffortPurposeAccount.setComments(comments);
            boolean result = this.workEffortPurposeAccountDao.create(workEffortPurposeAccount, principal().getUserLoginId());
            Validators.assertTrue(result, msg.getMessageTable(Messages.WORK_EFFORT_PURPOSE_ACCOUNT, "Cannot create record"));

        }
        return true;
    }
}
