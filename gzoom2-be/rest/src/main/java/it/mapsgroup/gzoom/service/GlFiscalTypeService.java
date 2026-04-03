package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.GlFiscalTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.GlFiscalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Leonardo Minaudo.
 */
@Service
public class GlFiscalTypeService {
    private static final Logger LOG = getLogger(GlFiscalTypeService.class);
    private final Configuration config;
    private final GlFiscalTypeDao glFiscalTypeDao;

    @Autowired
    public GlFiscalTypeService(Configuration config, GlFiscalTypeDao glFiscalTypeDao) {
        this.config = config;
        this.glFiscalTypeDao = glFiscalTypeDao;
    }

    public Result<GlFiscalType> getGlFiscalType() {
        List<GlFiscalType> list = this.glFiscalTypeDao.getGlFiscalTypeList();
        return new Result<>(list, list.size());
    }

    public Result<GlFiscalType> getGlFiscalTypeByIsIndicatorUsed(String isIndicatorUsed) {
        List<GlFiscalType> list = this.glFiscalTypeDao.getGlFiscalTypeByIsIndicatorUsed(isIndicatorUsed);
        return new Result<>(list, list.size());
    }

    public Result<GlFiscalType> getGlFiscalTypeByIsFinancialUsed(String isFinancialUsed) {
        List<GlFiscalType> list = this.glFiscalTypeDao.getGlFiscalTypeByIsFinancialUsed(isFinancialUsed);
        return new Result<>(list, list.size());
    }

    public Result<GlFiscalType> getGlFiscalTypeByIsAccountUsed(String isAccountUsed) {
        List<GlFiscalType> list = this.glFiscalTypeDao.getGlFiscalTypeByIsAccountUsed(isAccountUsed);
        return new Result<>(list, list.size());
    }

    public Result<GlFiscalType> getGlFiscalTypeByAccountTypeEnumId(String accountTypeEnumId) {
        List<GlFiscalType> list = new ArrayList<>();
        switch (accountTypeEnumId) {
            case "FINANCIAL":
                return this.getGlFiscalTypeByIsFinancialUsed("Y");
            case "ACCOUNT":
                return this.getGlFiscalTypeByIsAccountUsed("Y");
            case "INDICATOR":
                return this.getGlFiscalTypeByIsIndicatorUsed("Y");
            default:
                return new Result<>(list, list.size());
        }

    }

    public Result<GlFiscalType> getGlFiscalTypeForNewIndicatorMovement(String accountTypeEnumId, String glAccountId, String customTimePeriodId, String roleTypeId, String partyId, String voucherRef) {
        List<GlFiscalType> list = this.glFiscalTypeDao.getGlFiscalTypeForNewIndicatorMovement(accountTypeEnumId, glAccountId, customTimePeriodId, roleTypeId, partyId, voucherRef);
        return new Result<>(list, list.size());

    }

    public boolean createGlFiscalType(GlFiscalType req) {
        Validators.assertNotNull(req, Messages.GL_FISCAL_TYPE_REQUIRED);
        Validators.assertNotBlank(req.getGlFiscalTypeId(), Messages.GL_FISCAL_TYPE_ID_REQUIRED);
        GlFiscalType record = glFiscalTypeDao.get(req.getGlFiscalTypeId());
        Validators.assertTrue(record == null, Messages.GL_FISCAL_TYPE_ID_EXIST);
        Validators.assertNotBlank(req.getGlFiscalTypeEnumId(), Messages.GL_FISCAL_TYPE_ENUM_ID_REQUIRED);
        Validators.assertNotBlank(req.getDescription(), Messages.GL_FISCAL_TYPE__DESCRIPTION_REQUIRED);

        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getDescriptionLang(), Messages.GL_FISCAL_TYPE_DESCRIPTION_LANG_REQUIRED);
        }
        return glFiscalTypeDao.create(req, principal().getUserLoginId());
    }

    public boolean updateGlFiscalType(GlFiscalType req) {
        Validators.assertNotNull(req, Messages.GL_FISCAL_TYPE_REQUIRED);
        Validators.assertNotBlank(req.getGlFiscalTypeId(), Messages.GL_FISCAL_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(req.getGlFiscalTypeEnumId(), Messages.GL_FISCAL_TYPE_ENUM_ID_REQUIRED);
        GlFiscalType record = glFiscalTypeDao.get(req.getGlFiscalTypeId());
        Validators.assertNotNull(record, Messages.INVALID_GL_FISCAL_TYPE);
        Validators.assertNotBlank(req.getDescription(), Messages.GL_FISCAL_TYPE__DESCRIPTION_REQUIRED);

        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getDescriptionLang(), Messages.GL_FISCAL_TYPE_DESCRIPTION_LANG_REQUIRED);
        }
        return glFiscalTypeDao.update(req, principal().getUserLoginId());
    }

    public boolean deleteGlFiscalType(String id) {
        GlFiscalType record = glFiscalTypeDao.get(id);
        Validators.assertNotNull(record, Messages.INVALID_GL_FISCAL_TYPE);
        return glFiscalTypeDao.delete(id);
    }
}
