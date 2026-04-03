package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.GlAccountMeasRatScDao;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountMeasRatSc;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountMeasRatSc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class GlAccountMeasRatScService {
    private final GlAccountMeasRatScDao glAccountMeasRatScDao;
    private final Configuration configuration;

    @Autowired
    public GlAccountMeasRatScService(GlAccountMeasRatScDao glAccountMeasRatScDao, Configuration configuration) {
        this.glAccountMeasRatScDao = glAccountMeasRatScDao;
        this.configuration = configuration;
    }

    public boolean showWarningValueList(String glAccountId) {
        int result = this.glAccountMeasRatScDao.countByGlAccountId(glAccountId);
        return result == 0;
    }

    public Result<GlAccountMeasRatSc> getGlAccountMeasRatScByGlAccountId(String glAccountId) {
        List<GlAccountMeasRatSc> list = this.glAccountMeasRatScDao.getGlAccountMeasRatScByGlAccountId(glAccountId);
        return new Result<>(list, list.size());
    }
    
    public boolean createGlAccountMeasRatSc(GlAccountMeasRatSc req) {
        Messages msg = new Messages();
        Validators.assertNotBlank(req.getGlAccountId(),msg.getMessageColumn(Messages.GL_ACCOUNT_MEAS_RAT_SC, Messages.GL_ACCOUNT_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomId(),msg.getMessageColumn(Messages.GL_ACCOUNT_MEAS_RAT_SC, Messages.UOM_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomCode(),msg.getMessageColumn(Messages.GL_ACCOUNT_MEAS_RAT_SC, Messages.UOM_CODE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomDescr(),msg.getMessageColumn(Messages.GL_ACCOUNT_MEAS_RAT_SC, Messages.UOM_DESCR, Messages.IS_REQUIRED));

        if(this.configuration.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getUomCodeLang(),msg.getMessageColumn(Messages.GL_ACCOUNT_MEAS_RAT_SC, Messages.UOM_CODE_LANG, Messages.IS_REQUIRED));
            Validators.assertNotBlank(req.getUomDescrLang(),msg.getMessageColumn(Messages.GL_ACCOUNT_MEAS_RAT_SC, Messages.UOM_DESCR_LANG, Messages.IS_REQUIRED));
        }
        return this.glAccountMeasRatScDao.create(req, principal().getUserLoginId());
    }

    public boolean updateGlAccountMeasRatSc(GlAccountMeasRatSc[] req) {
        Messages msg = new Messages();
        boolean result = false;
        for (int i = 0; i < req.length; i++) {
            Validators.assertNotBlank(req[i].getGlAccountId(),msg.getMessageColumn(Messages.GL_ACCOUNT_MEAS_RAT_SC, Messages.GL_ACCOUNT_ID, Messages.IS_REQUIRED));
            Validators.assertNotBlank(req[i].getUomId(),msg.getMessageColumn(Messages.GL_ACCOUNT_MEAS_RAT_SC, Messages.UOM_ID, Messages.IS_REQUIRED));
            Validators.assertNotBlank(req[i].getUomCode(),msg.getMessageColumn(Messages.GL_ACCOUNT_MEAS_RAT_SC, Messages.UOM_CODE, Messages.IS_REQUIRED));
            Validators.assertNotBlank(req[i].getUomDescr(),msg.getMessageColumn(Messages.GL_ACCOUNT_MEAS_RAT_SC, Messages.UOM_DESCR, Messages.IS_REQUIRED));

            if(this.configuration.getLanguageType().equals("BILING")) {
                Validators.assertNotBlank(req[i].getUomCodeLang(),msg.getMessageColumn(Messages.GL_ACCOUNT_MEAS_RAT_SC, Messages.UOM_CODE_LANG, Messages.IS_REQUIRED));
                Validators.assertNotBlank(req[i].getUomDescrLang(),msg.getMessageColumn(Messages.GL_ACCOUNT_MEAS_RAT_SC, Messages.UOM_DESCR_LANG, Messages.IS_REQUIRED));
            }

            result = this.glAccountMeasRatScDao.update(req[i], principal().getUserLoginId());
            Validators.assertTrue(result,msg.getMessagesWithSpace(Messages.GL_ACCOUNT_MEAS_RAT_SC, "Cannot update record"));
        }
        return result;
  }

    public boolean deleteGlAccountMeasRatSc(GlAccountMeasRatSc[] glAccountMeasRatSc) {
        Messages msg = new Messages();
        for (int i = 0; i < glAccountMeasRatSc.length; i++) {
            boolean result = this.glAccountMeasRatScDao.delete(glAccountMeasRatSc[i].getGlAccountId(), glAccountMeasRatSc[i].getUomId(), glAccountMeasRatSc[i].getUomRatingValue());
            Validators.assertTrue(result,msg.getMessagesWithSpace(Messages.GL_ACCOUNT_MEAS_RAT_SC, "Cannot delete record"));
        }
        return true;
    }

}
