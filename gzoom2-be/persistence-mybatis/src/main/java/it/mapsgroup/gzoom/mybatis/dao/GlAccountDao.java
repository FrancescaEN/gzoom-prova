package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.GlAccount;
import it.mapsgroup.gzoom.mybatis.mapper.GlAccountMapper;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GlAccountDao extends AbstractDao {
    private static final Logger LOG = getLogger(GlAccountDao.class);
    private final GlAccountMapper glAccountMapper;
    private final SequenceGenerator sequenceGenerator;

    @Autowired
    public GlAccountDao(GlAccountMapper glAccountMapper, SequenceGenerator sequenceGenerator) {
        this.glAccountMapper = glAccountMapper;
        this.sequenceGenerator = sequenceGenerator;
    }

    /**
     * This function gets decimal precision.
     *
     * @return BigInteger.
     */
    @Transactional
    public BigInteger getDecimalPrecision(String glAccountId) {
        LOG.info("getDecimalPrecision");

        BigInteger decimalPrec = this.glAccountMapper.getDecimalPrecision(glAccountId);
        LOG.info("Decimal Precision = {}", (decimalPrec != null));
        return decimalPrec;
    }

    @Transactional
    public GlAccount getGlAccount(String glAccountId) {
        LOG.info("getGlAccount");
        GlAccount glAccount = this.glAccountMapper.selectByPrimaryKey(glAccountId);
        LOG.info("GlAccount = {}", (glAccount != null));
        return glAccount;
    }

    @Transactional
    public List<GlAccount> getGlAccountByOrganizationPartyId(String organizationId) {
        LOG.info("getGlAccountByOrganizationPartyId");
        List<GlAccount> glAccounts = this.glAccountMapper.selectByOrganizationPartyId(organizationId);
        LOG.info("size = {}", glAccounts.size());
        return glAccounts;
    }

    @Transactional
    public List<GlAccount> selectGlAccount(String userLoginId, boolean isAdmin,
                                           String organizationId,
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
        LOG.info("getGlAccount");

        List<GlAccount> glAccounts = this.glAccountMapper.selectGlAccount(
                userLoginId,
                isAdmin,
                organizationId,
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
        LOG.info("size = {}", glAccounts.size());
        return glAccounts;
    }

    @Transactional
    public List<GlAccount> selectGlAccountByFilterParams(
            boolean isSecondaryLang,
            String search,
            String matchModeSearch,
            String[] workEffortPurposeTypeId,
            String glAccountTypeId) {
        LOG.info("selectGlAccountByFilterParams");

        List<GlAccount> glAccounts = this.glAccountMapper.selectGlAccountByFilterParams(
                isSecondaryLang,
                search,
                matchModeSearch,
                workEffortPurposeTypeId,
                glAccountTypeId);
        LOG.info("size = {}", glAccounts.size());
        return glAccounts;
    }

    @Transactional
    public List<GlAccount> getGlAccountMovements(
            String userLoginId,
            boolean isAdmin,
            String accountTypeEnumId,
            String isReservedAccount,
            String organizationId,
            String inputEnumId,
            String detectOrgUnitIdFlag
    ) {
        LOG.info("getGlAccountMovements");
        List<GlAccount> glAccounts = this.glAccountMapper.getGlAccountMovements(
                userLoginId,
                isAdmin,
                accountTypeEnumId,
                isReservedAccount,
                organizationId,
                inputEnumId,
                detectOrgUnitIdFlag
        );
        LOG.info("size = {}", glAccounts.size());
        return glAccounts;
    }

    @Transactional
    public GlAccount create(GlAccount glAccount, String userLoginId) {
        LOG.info("create glAccount");
        String newId = this.sequenceGenerator.getNextSeqId("GlAccount");
        glAccount.setGlAccountId(newId);
        if (glAccount.getAccountCode() == null || glAccount.getAccountCode().isEmpty()) glAccount.setAccountCode(newId);
        setCreatedTimestamp(glAccount);
        glAccount.setCreatedByUserLogin(userLoginId);
        int result = this.glAccountMapper.insert(glAccount);
        LOG.info("result = {}", result);
        return (result > 0) ? glAccount : null;
    }

    @Transactional
    public boolean update(GlAccount glAccount, String userLogin) {
        LOG.info("update glAccount");
        setUpdateTimestamp(glAccount);
        glAccount.setLastModifiedByUserLogin(userLogin);
        int result = this.glAccountMapper.updateByPrimaryKey(glAccount);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean updateCalcCustomMethodAndPrioCalc(String glAccountId, String calcCustomMethodId, BigDecimal prioCalc, String userLogin) {
        LOG.info("updateCalcCustomMethodAndPrioCalc");
        GlAccount glAccount = new GlAccount();
        glAccount.setGlAccountId(glAccountId);
        glAccount.setCalcCustomMethodId(calcCustomMethodId);
        glAccount.setPrioCalc(prioCalc);
        setUpdateTimestamp(glAccount);
        glAccount.setLastModifiedByUserLogin(userLogin);
        int result = this.glAccountMapper.updateCalcCustomMethodAndPrioCalc(glAccount);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String id) {
        LOG.info("delete glAccountType");
        int result = this.glAccountMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public List<GlAccount> selectGlAccountByOrgId(
            boolean isAdmin,
            String userLoginId,
            String organizationId){

        LOG.info("selectGlAccountByOrgId");
        List<GlAccount> glAccounts = this.glAccountMapper.selectGlAccountByOrgId(isAdmin, organizationId, userLoginId);
        LOG.info("size = {}", glAccounts.size());
        return glAccounts;
    }

}
