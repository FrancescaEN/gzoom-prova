package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.GlAccountInputCalcDao;
import it.mapsgroup.gzoom.mybatis.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class GlAccountInputCalcService {
    private final GlAccountInputCalcDao glAccountInputCalcDao;
    private final GlAccountService glAccountService;

    @Autowired
    public GlAccountInputCalcService(GlAccountInputCalcDao glAccountInputCalcDao, GlAccountService glAccountService) {
        this.glAccountInputCalcDao = glAccountInputCalcDao;
        this.glAccountService = glAccountService;
    }

    public boolean showWarningCalculationFormula(String glAccountId) {
        GlAccount glAccount = this.glAccountService.getGlAccount(glAccountId);
        if(glAccount.getCalcCustomMethodId() != null) {
            int result = this.glAccountInputCalcDao.countByGlAccountId(glAccountId);
            return result == 0;
        }
        return false;
    }

    public Result<GlAccountInputCalc> getGlAccountInputCalcByGlAccountId(String glAccountId) {
        List<GlAccountInputCalc> list = this.glAccountInputCalcDao.getGlAccountInputCalcByGlAccountId(glAccountId);
        return new Result<>(list, list.size());
    }

    public Result<GlAccountInputCalc> getGlAccountInputCalcByGlAccountIdRef(String glAccountIdRef) {
        List<GlAccountInputCalc> list = this.glAccountInputCalcDao.getGlAccountInputCalcByGlAccountIdRef(glAccountIdRef);
        return new Result<>(list, list.size());
    }



    public GlAccountInputCalc createGlAccountInputCalc(GlAccountInputCalc req) {
        Messages msg = new Messages();
        Validators.assertNotBlank(req.getGlAccountId(),msg.getMessageColumn(Messages.GL_ACCOUNT_INPUT_CALC, Messages.GL_ACCOUNT_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getInputSequenceNum(),msg.getMessageColumn(Messages.GL_ACCOUNT_INPUT_CALC, Messages.INPUT_SEQUENCE_NUM, Messages.IS_REQUIRED));

        return this.glAccountInputCalcDao.create(req, principal().getUserLoginId());
    }

    @Transactional
    public boolean updateGlAccountInputCalc(GlAccountInputCalc[] req) {
        Messages msg = new Messages();

        for (int i = 0; i < req.length; i++) {
            Validators.assertNotBlank(req[i].getGlAccountId(),msg.getMessageColumn(Messages.GL_ACCOUNT_INPUT_CALC, Messages.GL_ACCOUNT_ID, Messages.IS_REQUIRED));
            Validators.assertNotBlank(req[i].getInputSequenceNum(),msg.getMessageColumn(Messages.GL_ACCOUNT_INPUT_CALC, Messages.INPUT_SEQUENCE_NUM, Messages.IS_REQUIRED));
            this.glAccountInputCalcDao.update(req[i], principal().getUserLoginId());
        }
       return true;
    }

    public boolean updateGlAccountIdRef(String glAccountInputCalcId, String glAccountIdRef)  {
        return this.glAccountInputCalcDao.updateGlAccountIdRef(glAccountInputCalcId, glAccountIdRef,principal().getUserLoginId());
    }

    public boolean deleteGlAccountImputCalc(String[] ids) {
        for (int i = 0; i < ids.length; i++) {
            this.glAccountInputCalcDao.delete(ids[i]);
        }
        return true;
    }
}
