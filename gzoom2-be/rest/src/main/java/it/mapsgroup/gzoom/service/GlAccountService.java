package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.*;
import it.mapsgroup.gzoom.mybatis.dto.*;
import it.mapsgroup.gzoom.mybatis.service.FilterService;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import it.mapsgroup.gzoom.mybatis.util.Permission;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GlAccountService {
    private static final Logger LOG = getLogger(GlAccountService.class);

    private final GlAccountDao glAccountDao;
    private final Configuration config;
    private final WorkEffortPurposeAccountDao workEffortPurposeAccountDao;
    private final GlAccountOrganizationDao glAccountOrganizationDao;
    private final UserPreferenceService userPreferenceService;
    private final GlAccountInputCalcDao glAccountInputCalcDao;
    private final GlAccountRoleDao glAccountRoleDao;
    private final GlAccountMeasRatScDao glAccountMeasRatScDao;
    private final WorkEffortMeasureDao workEffortMeasureDao;
    private final FilterService filterService;
    private final UserCtxPermissionViewDao userCtxPermissionViewDao;
    private final UomService uomService;
    private final UomRatingScaleDao uomRatingScaleDao;

    @Autowired
    public GlAccountService(GlAccountDao glAccountDao, Configuration config, WorkEffortPurposeAccountDao workEffortPurposeAccountDao, GlAccountOrganizationDao glAccountOrganizationDao, UserPreferenceService userPreferenceService, GlAccountInputCalcDao glAccountInputCalcDao, GlAccountRoleDao glAccountRoleDao, GlAccountMeasRatScDao glAccountMeasRatScDao, WorkEffortMeasureDao workEffortMeasureDao, FilterService filterService, UserCtxPermissionViewDao userCtxPermissionViewDao, UomService uomService, UomRatingScaleDao uomRatingScaleDao) {
        this.glAccountDao = glAccountDao;
        this.config = config;
        this.workEffortPurposeAccountDao = workEffortPurposeAccountDao;
        this.glAccountOrganizationDao = glAccountOrganizationDao;
        this.userPreferenceService = userPreferenceService;
        this.glAccountInputCalcDao = glAccountInputCalcDao;
        this.glAccountRoleDao = glAccountRoleDao;
        this.glAccountMeasRatScDao = glAccountMeasRatScDao;
        this.workEffortMeasureDao = workEffortMeasureDao;
        this.filterService = filterService;
        this.userCtxPermissionViewDao = userCtxPermissionViewDao;
        this.uomService = uomService;
        this.uomRatingScaleDao = uomRatingScaleDao;
    }

    public BigInteger getDecimalPrecision(String glAccountId) {
        return glAccountDao.getDecimalPrecision(glAccountId);
    }

    public GlAccount getGlAccount(String glAccountId) {
        return this.glAccountDao.getGlAccount(glAccountId);
    }

    public Result<GlAccount> getGlAccount(

            String accountTypeEnumId,
            String isReservedAccount,
            boolean isSecondaryLang,
            String search,
            String matchModeSearch,
            String[] workEffortPurposeTypeId,
            String orderBy,
            String orderType,
            String glAccountTypeId,
            String inputEnumId,
            String detectOrgUnitIdFlag,
            String debitCreditDefault,
            String periodTypeId,
            String currentStatusId,
            String respCenterId) {
    boolean isAdmin = this.userCtxPermissionViewDao.hasPermission(principal().getUserLoginId(), ContextPermissionPrefixEnum.CTX_AC, Permission.ADMIN);

    List<GlAccount> list = this.glAccountDao.selectGlAccount(
        principal().getUserLoginId(),
                isAdmin,
                this.userPreferenceService.getOrganizationId(),
                accountTypeEnumId,
                isReservedAccount,
                isSecondaryLang,
                search,
                matchModeSearch,
                workEffortPurposeTypeId,
                orderBy,
                orderType,
                glAccountTypeId,
                inputEnumId,
                detectOrgUnitIdFlag,
                debitCreditDefault,
                periodTypeId,
                currentStatusId,
                respCenterId);
        return new Result<>(list, list.size());
    }

    public Result<GlAccount> getGlAccountByFilterParams(
            boolean isSecondaryLang,
            String search,
            String matchModeSearch,
            String[] workEffortPurposeTypeId,
            String glAccountTypeId
    ) {
        List<GlAccount> list = this.glAccountDao.selectGlAccountByFilterParams(
                isSecondaryLang,
                search,
                matchModeSearch,
                workEffortPurposeTypeId,
                glAccountTypeId
        );
        return new Result<>(list, list.size());
    }

    public Result<GlAccount> getGlAccountMovements(
            String accountTypeEnumId,
            String isReservedAccount,
            String inputEnumId,
            String detectOrgUnitIdFlag
    ) {
        String userLoginId = principal().getUserLoginId();
        boolean isAdmin = this.userCtxPermissionViewDao.hasPermission(userLoginId, ContextPermissionPrefixEnum.CTX_AC, Permission.ADMIN);
        List<GlAccount> list = this.glAccountDao.getGlAccountMovements(
                userLoginId,
                isAdmin,
                accountTypeEnumId,
                isReservedAccount,
                this.userPreferenceService.getOrganizationId(),
                inputEnumId,
                detectOrgUnitIdFlag
        );
        return new Result<>(list, list.size());
    }

    public Result<GlAccount> getGlAccountByOrganizationPartyId() {
        List<GlAccount> list = this.glAccountDao.getGlAccountByOrganizationPartyId(this.userPreferenceService.getOrganizationId());
        return new Result<>(list, list.size());
    }

    public Result<GlAccount> selectGlAccountByOrgId() {
        boolean isAdmin = this.userCtxPermissionViewDao.hasPermission(principal().getUserLoginId(), ContextPermissionPrefixEnum.CTX_WE, Permission.ADMIN);
        List<GlAccount> list = this.glAccountDao.selectGlAccountByOrgId(isAdmin, principal().getUserLoginId(),this.userPreferenceService.getOrganizationId());
        return new Result<>(list, list.size());
    }

    @Transactional
    public boolean updateGlAccount(GlAccount req) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.GL_ACCOUNT, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getAccountTypeEnumId(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.ACCOUNT_TYPE_ENUM_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getAccountName(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.ACCOUNT_NAME, Messages.IS_REQUIRED));
        if (config.getLanguageType().equals("BILING"))
            Validators.assertNotBlank(req.getAccountNameLang(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.ACCOUNT_NAME_LANG, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getAccountCode(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.ACCOUNT_CODE, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getGlAccountTypeId(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.GL_ACCOUNT_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getInputEnumId(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.INPUT_ENUM_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getDetectOrgUnitIdFlag(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.DETECT_ORG_UNIT_ID_FLAG, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getDefaultUomId(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.DEFAULT_UOM_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getPeriodTypeId(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.PERIOD_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getCurrentStatusId(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.CURRENT_STATUS_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getDebitCreditDefault(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.DEBIT_CREDIT_DEFAULT, Messages.IS_REQUIRED));
        if (this.userCtxPermissionViewDao.hasPermission(principal().getUserLoginId(), ContextPermissionPrefixEnum.CTX_AC, Permission.RESP)) {
            Validators.assertNotNull(req.getRespCenterId(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.RESP_CENTER_ID, Messages.IS_REQUIRED));
            Validators.assertNotNull(req.getRespCenterRoleTypeId(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.RESP_CENTER_ROLE_TYPE_ID, Messages.IS_REQUIRED));
        }
        if (req.getCalcCustomMethodId() == null) this.glAccountInputCalcDao.deleteByGlAccountId(req.getGlAccountId());

        GlAccount actualGlAccount = this.glAccountDao.getGlAccount(req.getGlAccountId());
        boolean reqIsRatingScale = this.uomService.isRatingScale(req.getDefaultUomId());
        if(reqIsRatingScale) {
            if(!actualGlAccount.getDefaultUomId().equals(req.getDefaultUomId())){
                this.glAccountMeasRatScDao.deleteByGlAccountId(req.getGlAccountId());
                this.insertValueList(req.getGlAccountId(), req.getDefaultUomId());
            }
        }
        else {
            this.glAccountMeasRatScDao.deleteByGlAccountId(req.getGlAccountId());
        }

        this.workEffortMeasureDao.updateWEMeasureFromGlAccount(req, principal().getUserLoginId());

        return this.glAccountDao.update(req, principal().getUserLoginId());
    }

    @Transactional
    public boolean updateCalcCustomMethodAndPrioCalc(String glAccountId, String calcCustomMethodId, BigDecimal prioCalc) {
        if (calcCustomMethodId == null) this.glAccountInputCalcDao.deleteByGlAccountId(glAccountId);
        return this.glAccountDao.updateCalcCustomMethodAndPrioCalc(glAccountId, calcCustomMethodId, prioCalc, principal().getUserLoginId());
    }

    @Transactional
    public GlAccount createGlAccount(GlAccount req, String[] workEffortPurposeTypeId) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.GL_ACCOUNT, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getAccountTypeEnumId(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.ACCOUNT_TYPE_ENUM_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getAccountName(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.ACCOUNT_NAME, Messages.IS_REQUIRED));
        if (config.getLanguageType().equals("BILING"))
            Validators.assertNotBlank(req.getAccountNameLang(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.ACCOUNT_NAME_LANG, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getGlAccountTypeId(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.GL_ACCOUNT_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getInputEnumId(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.INPUT_ENUM_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getDetectOrgUnitIdFlag(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.DETECT_ORG_UNIT_ID_FLAG, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getDefaultUomId(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.DEFAULT_UOM_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getPeriodTypeId(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.PERIOD_TYPE_ID, Messages.IS_REQUIRED));
        if (this.userCtxPermissionViewDao.hasPermission(principal().getUserLoginId(), ContextPermissionPrefixEnum.CTX_AC, Permission.RESP)) {
            Validators.assertNotNull(req.getRespCenterId(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.RESP_CENTER_ID, Messages.IS_REQUIRED));
            Validators.assertNotNull(req.getRespCenterRoleTypeId(), msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.RESP_CENTER_ROLE_TYPE_ID, Messages.IS_REQUIRED));
        }

        req.setCurrentStatusId("GLACC_ACTIVE");
        req.setDebitCreditDefault("D");
        req.setWeMeasureTypeEnumId("WEMT_PERF");
        req.setTargetPeriodEnumId("TARGET_EXEC_PERIOD");
        req.setWeWithoutTarget("WEWITHTARG_NO_CALC");
        req.setPeriodicalAbsoluteEnumId("PRDABS_ALL");
        req.setWeWithoutPerf("WEWITHPERF_PERF_0");
        req.setWeScoreConvEnumId("WECONVER_PERCENTWRK");
        req.setWeScoreRangeEnumId("WESCORE_MAXRANGE");

        String userLoginId = principal().getUserLoginId();

        GlAccount glAccount = this.glAccountDao.create(req, userLoginId);

        //Inserisco l'organizzazione
        GlAccountOrganization glAccountOrganization = new GlAccountOrganization();
        glAccountOrganization.setGlAccountId(glAccount.getGlAccountId());
        glAccountOrganization.setOrganizationPartyId(this.userPreferenceService.getOrganizationId());
        this.glAccountOrganizationDao.create(glAccountOrganization, principal().getUserLoginId());

        //Inserisco le finalità
        if (workEffortPurposeTypeId != null) {
            for (String s : workEffortPurposeTypeId) {
                WorkEffortPurposeAccount workEffortPurposeAccount = new WorkEffortPurposeAccount();
                workEffortPurposeAccount.setGlAccountId(glAccount.getGlAccountId());
                workEffortPurposeAccount.setWorkEffortPurposeTypeId(s);
                this.workEffortPurposeAccountDao.create(workEffortPurposeAccount, userLoginId);

            }
        }

        //Inserisco la lista valori se l'unità di misura è RATING_SCALE
        if(this.uomService.isRatingScale(glAccount.getDefaultUomId())) {
            this.insertValueList(glAccount.getGlAccountId(), glAccount.getDefaultUomId());
        }

        return glAccount;
    }

    private void insertValueList(String glAccountId, String uomId) {
        List<UomRatingScale> list = this.uomRatingScaleDao.getUomRatingScales(uomId);
        list.forEach(item -> {
            GlAccountMeasRatSc glAccountMeasRatSc = new GlAccountMeasRatSc();
            glAccountMeasRatSc.setGlAccountId(glAccountId);
            glAccountMeasRatSc.setUomId(uomId);
            glAccountMeasRatSc.setUomRatingValue(item.getUomRatingValue());
            glAccountMeasRatSc.setUomCode(item.getDescription());
            glAccountMeasRatSc.setUomCodeLang(item.getDescriptionLang());
            glAccountMeasRatSc.setUomDescr(item.getDescription());
            glAccountMeasRatSc.setUomDescrLang(item.getDescriptionLang());

            this.glAccountMeasRatScDao.create(glAccountMeasRatSc, principal().getUserLoginId());
        });
    }

    @Transactional
    public boolean deleteGlAccount(String[] glAccountIds) {
        Messages msg = new Messages();

        for (String glAccountId : glAccountIds) {
            Validators.assertNotBlank(glAccountId, msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.GL_ACCOUNT_ID, Messages.IS_REQUIRED));
            GlAccount record = this.glAccountDao.getGlAccount(glAccountId);
            Validators.assertFalse(record == null, msg.getMessageColumn(Messages.GL_ACCOUNT, Messages.GL_ACCOUNT_ID, Messages.INVALID));
            this.glAccountOrganizationDao.deleteByGlAccountId(glAccountId);
            this.workEffortPurposeAccountDao.deleteByGlAccountId(glAccountId);
            this.glAccountRoleDao.deleteByGlAccountId(glAccountId);
            this.glAccountInputCalcDao.deleteByGlAccountId(glAccountId);
            this.glAccountMeasRatScDao.deleteByGlAccountId(glAccountId);
            this.glAccountDao.delete(glAccountId);
        }
        return true;

    }

    public boolean isByInputEnumIdAndDetectOrgUnitIdFlag(String glAccountId, String inputEnumId, String detectOrgUnitIdFlag) {
        GlAccount glAccount = this.glAccountDao.getGlAccount(glAccountId);
        if (glAccount != null)
            return glAccount.getInputEnumId().equals(inputEnumId) && glAccount.getDetectOrgUnitIdFlag().equals(detectOrgUnitIdFlag);
        return false;
    }
}
