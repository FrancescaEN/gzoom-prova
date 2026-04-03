package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.GlAccountResourceDao;
import it.mapsgroup.gzoom.mybatis.dao.GlAccountTypeDao;
import it.mapsgroup.gzoom.mybatis.dao.GlAccountTypeGlFiscalTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class GlAccountTypeService {
    private final GlAccountTypeDao glAccountTypeDao;
    private final Configuration config;
    private final GlAccountTypeGlFiscalTypeDao glAccountTypeGlFiscalTypeDao;
    private final GlAccountResourceDao glAccountResourceDao;

    @Autowired
    public GlAccountTypeService(GlAccountTypeDao glAccountTypeDao, Configuration config, GlAccountTypeGlFiscalTypeDao glAccountTypeGlFiscalTypeDao, GlAccountResourceDao glAccountResourceDao) {
        this.glAccountTypeDao = glAccountTypeDao;
        this.config = config;
        this.glAccountTypeGlFiscalTypeDao = glAccountTypeGlFiscalTypeDao;
        this.glAccountResourceDao = glAccountResourceDao;
    }

    public Result<GlAccountType> getGlAccountTypeList() {
        List<GlAccountType> list = this.glAccountTypeDao.getGlAccountTypeList();
        return new Result<>(list, list.size());
    }

    public Result<GlAccountType> getGlAccountTypeList(String accountTypeEnumId) {
        List<GlAccountType> list = this.glAccountTypeDao.getGlAccountTypeList(accountTypeEnumId);
        return new Result<>(list, list.size());
    }

    public Result<GlAccountType> getGlAccountTypeList(String accountTypeEnumId, String isReservedAccount) {
        List<GlAccountType> list = this.glAccountTypeDao.getGlAccountTypeList(accountTypeEnumId, isReservedAccount);
        return new Result<>(list, list.size());
    }

    public GlAccountType getGlAccountTypeId(String glAccountTypeId) {
        GlAccountType glAccountType = this.glAccountTypeDao.getGlAccountTypeId(glAccountTypeId);
        return glAccountType;
    }

    public boolean createGlAccountType(GlAccountType req) {
        this.validateGlAccountType(req, "CREATE");
        return glAccountTypeDao.create(req, principal().getUserLoginId());
    }

    public boolean updateGlAccountType(GlAccountType req) {
        this.validateGlAccountType(req, "UPDATE");
        return glAccountTypeDao.update(req, principal().getUserLoginId());
    }

    @Transactional
    public boolean deleteGlAccountType(String glAccountTypeId) {
        Messages msg = new Messages();
        Validators.assertNotBlank(glAccountTypeId, msg.getMessageColumn(Messages.GL_ACCOUNT_TYPE, Messages.GL_ACCOUNT_TYPE_ID, Messages.IS_REQUIRED));
        GlAccountType record = glAccountTypeDao.get(glAccountTypeId);
        Validators.assertFalse(record == null, msg.getMessageColumn(Messages.GL_ACCOUNT_TYPE, Messages.GL_ACCOUNT_TYPE_ID, Messages.INVALID));

        this.glAccountTypeGlFiscalTypeDao.deleteWithGlAccountTypeId(glAccountTypeId);
        this.glAccountResourceDao.deleteWithGlAccountTypeId(glAccountTypeId);
        
        return glAccountTypeDao.delete(glAccountTypeId);
    }

    private void validateGlAccountType(GlAccountType req, String method) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.GL_ACCOUNT_TYPE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getGlAccountTypeId(), msg.getMessageColumn(Messages.GL_ACCOUNT_TYPE, Messages.GL_ACCOUNT_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getDescription(), msg.getMessageColumn(Messages.GL_ACCOUNT_TYPE, Messages.DESCRIPTION, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getIsReservedAccount(), msg.getMessageColumn(Messages.GL_ACCOUNT_TYPE, Messages.IS_RESERVED_ACCOUNT, Messages.IS_REQUIRED));

        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getDescriptionLang(), msg.getMessageColumn(Messages.GL_ACCOUNT_TYPE, Messages.DESCRIPTION_LANG, Messages.IS_REQUIRED));
        }
        GlAccountType record = glAccountTypeDao.get(req.getGlAccountTypeId());

        if ( method.equalsIgnoreCase("CREATE")) {
            Validators.assertTrue(record == null, msg.getMessageTable(Messages.GL_ACCOUNT_TYPE, Messages.EXISTING));
        }
        if ( method.equalsIgnoreCase("UPDATE")) {
            Validators.assertFalse(record == null, msg.getMessageColumn(Messages.GL_ACCOUNT_TYPE, Messages.GL_ACCOUNT_TYPE_ID, Messages.INVALID));
        }

    }
}
