package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.GlAccountTypeGlFiscalTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountTypeGlFiscalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class GlAccountTypeGlFiscalTypeService {
    private final GlAccountTypeGlFiscalTypeDao glAccountTypeGlFiscalTypeDao;

    @Autowired
    public GlAccountTypeGlFiscalTypeService(GlAccountTypeGlFiscalTypeDao glAccountTypeGlFiscalTypeDao) {
        this.glAccountTypeGlFiscalTypeDao = glAccountTypeGlFiscalTypeDao;
    }

    public Result<GlAccountTypeGlFiscalType> getGlAccountTypeGlFiscalTypeList(String uomRangeId) {
        List<GlAccountTypeGlFiscalType> list = glAccountTypeGlFiscalTypeDao.getGlAccountTypeGlFiscalTypeList(uomRangeId);
        return new Result<>(list, list.size());
    }

    public boolean createGlAccountTypeGlFiscalType(GlAccountTypeGlFiscalType req) {
        this.validateGlAccountTypeGlFiscalType(req, "CREATE");
        return glAccountTypeGlFiscalTypeDao.create(req, principal().getUserLoginId());
    }

    public boolean updateGlAccountTypeGlFiscalType(GlAccountTypeGlFiscalType req) {
        this.validateGlAccountTypeGlFiscalType(req, "UPDATE");
        return glAccountTypeGlFiscalTypeDao.update(req, principal().getUserLoginId());
    }

    public boolean deleteGlAccountTypeGlFiscalType (String glAccountTypeId, String glFiscalTypeId) {
        Messages msg = new Messages();
        Validators.assertNotBlank(glAccountTypeId, msg.getMessageColumn(Messages.GL_ACCOUNT_TYPE_GL_FISCAL_TYPE, Messages.GL_ACCOUNT_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(glFiscalTypeId, msg.getMessageColumn(Messages.GL_ACCOUNT_TYPE_GL_FISCAL_TYPE, Messages.GL_FISCAL_TYPE_ID, Messages.IS_REQUIRED));
        GlAccountTypeGlFiscalType record = glAccountTypeGlFiscalTypeDao.get(glAccountTypeId, glFiscalTypeId);
        Validators.assertFalse(record == null, msg.getMessageColumn(Messages.GL_ACCOUNT_TYPE, Messages.GL_ACCOUNT_TYPE_ID, Messages.INVALID));

        return glAccountTypeGlFiscalTypeDao.delete(glAccountTypeId, glFiscalTypeId);
    }

    private void validateGlAccountTypeGlFiscalType(GlAccountTypeGlFiscalType req, String method) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.GL_ACCOUNT_TYPE_GL_FISCAL_TYPE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getGlAccountTypeId(), msg.getMessageColumn(Messages.GL_ACCOUNT_TYPE_GL_FISCAL_TYPE, Messages.GL_ACCOUNT_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getGlFiscalTypeId(), msg.getMessageColumn(Messages.GL_ACCOUNT_TYPE_GL_FISCAL_TYPE, Messages.GL_FISCAL_TYPE_ID, Messages.IS_REQUIRED));

        GlAccountTypeGlFiscalType record = glAccountTypeGlFiscalTypeDao.get(req.getGlAccountTypeId(), req.getGlFiscalTypeId());
        if ( method.equalsIgnoreCase("CREATE")) {
            Validators.assertTrue(record == null, msg.getMessageTable(Messages.GL_ACCOUNT_TYPE, Messages.EXISTING));
        }
        if ( method.equalsIgnoreCase("UPDATE")) {
            Validators.assertFalse(record == null, msg.getMessageColumn(Messages.GL_ACCOUNT_TYPE_GL_FISCAL_TYPE, Messages.GL_ACCOUNT_TYPE_ID + " and " + Messages.GL_FISCAL_TYPE_ID, Messages.INVALID));
        }

    }
}
