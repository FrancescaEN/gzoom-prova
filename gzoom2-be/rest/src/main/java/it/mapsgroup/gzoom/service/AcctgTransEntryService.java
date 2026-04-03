package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.commons.TableLazyLoad;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.*;
import it.mapsgroup.gzoom.mybatis.dto.AcctgTrans;
import it.mapsgroup.gzoom.mybatis.dto.AcctgTransEntry;
import it.mapsgroup.gzoom.mybatis.dto.GlAccount;
import it.mapsgroup.gzoom.mybatis.service.PermissionService;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import it.mapsgroup.gzoom.mybatis.util.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class AcctgTransEntryService {
    private final AcctgTransEntryDao acctgTransEntryDao;
    private final AcctgTransDao acctgTransDao;
    private final PermissionService permissionService;
    private final GlAccountService glAccountService;
    private final CustomTimePeriodDao customTimePeriodDao;
    private final WorkEffortMeasureDao workEffortMeasureDao;
    private final UserCtxPermissionViewDao userCtxPermissionViewDao;

    @Autowired
    public AcctgTransEntryService(AcctgTransEntryDao acctgTransEntryDao, AcctgTransDao acctgTransDao, PermissionService permissionService, GlAccountService glAccountService, CustomTimePeriodDao customTimePeriodDao, WorkEffortMeasureDao workEffortMeasureDao, UserCtxPermissionViewDao userCtxPermissionViewDao) {
        this.acctgTransEntryDao = acctgTransEntryDao;
        this.acctgTransDao = acctgTransDao;
        this.permissionService = permissionService;
        this.glAccountService = glAccountService;
        this.customTimePeriodDao = customTimePeriodDao;
        this.workEffortMeasureDao = workEffortMeasureDao;
        this.userCtxPermissionViewDao = userCtxPermissionViewDao;
    }

    public Result<AcctgTransEntry> getIndicatorMovements(
            String accountTypeEnumId,
            String isReservedAccount,
            String inputEnumId,
            String detectOrgUnitIdFlag,
            TableLazyLoad tableLazyLoad,
            String glAccountId,
            String uomId,
            String customTimePeriodId,
            String glFiscalTypeId) {
        String userLoginId = principal().getUserLoginId();
        String organizationId = this.permissionService.userPrefereceOrganizationUnitId(userLoginId);
        List<AcctgTransEntry> list = this.acctgTransEntryDao.getIndicatorMovements(
                accountTypeEnumId,
                isReservedAccount,
                organizationId,
                inputEnumId,
                detectOrgUnitIdFlag,
                userLoginId,
                tableLazyLoad,
                glAccountId,
                uomId,
                customTimePeriodId,
                glFiscalTypeId
        );
        return new Result<>(list, list.size());
    }

    public Integer countIndicatorMovements(
            String accountTypeEnumId,
            String isReservedAccount,
            String inputEnumId,
            String detectOrgUnitIdFlag,
            TableLazyLoad tableLazyLoad,
            String glAccountId, String uomId, String customTimePeriodId, String glFiscalTypeId) {
        String userLoginId = principal().getUserLoginId();
        String organizationId = this.permissionService.userPrefereceOrganizationUnitId(userLoginId);
        return this.acctgTransEntryDao.countIndicatorMovements(
                accountTypeEnumId,
                isReservedAccount,
                organizationId,
                inputEnumId,
                detectOrgUnitIdFlag,
                userLoginId,
                tableLazyLoad,
                glAccountId,
                uomId,
                customTimePeriodId,
                glFiscalTypeId);
    }

    @Transactional
    public boolean updateAcctgTransEntry(AcctgTransEntry[] acctgTransEntries) {
        String userLoginId = principal().getUserLoginId();
        for (int i = 0; i < acctgTransEntries.length; i++) {
            this.acctgTransEntryDao.updatePartialTable(acctgTransEntries[i], userLoginId);
            AcctgTrans acctgTrans = new AcctgTrans();
            acctgTrans.setAcctgTransId(acctgTransEntries[i].getAcctgTrans().getAcctgTransId());
            acctgTrans.setGlFiscalTypeId(acctgTransEntries[i].getGlFiscalTypeId());
            acctgTrans.setPartyId(acctgTransEntries[i].getAcctgTrans().getPartyId());
            acctgTrans.setRoleTypeId(acctgTransEntries[i].getAcctgTrans().getRoleTypeId());
            this.acctgTransDao.updatePartialTable(acctgTrans, userLoginId);
        }
        return true;
    }

    @Transactional
    public boolean updateDetailAcctgTransEntry(AcctgTransEntry acctgTransEntry) {
        String userLoginId = principal().getUserLoginId();
        acctgTransEntry.setOrigAmount(acctgTransEntry.getAmount());
        this.acctgTransEntryDao.updateDetail(acctgTransEntry, userLoginId);
        if (acctgTransEntry.getVoucherRef() != null) {
            acctgTransEntry.getAcctgTrans().setWorkEffortId(this.workEffortMeasureDao.getWorkEffortMeasureExUomById(acctgTransEntry.getVoucherRef()).getWorkEffortId());
        }
        this.acctgTransDao.updateDetail(acctgTransEntry.getAcctgTrans(), userLoginId);
        return true;
    }

    @Transactional
    public boolean createAcctgTransEntryEx(AcctgTransEntry acctgTransEntry, String customTimePeriodId) {
        String userLoginId = principal().getUserLoginId();
        GlAccount glAccount = this.glAccountService.getGlAccount(acctgTransEntry.getGlAccountId());

        AcctgTrans acctgTrans = new AcctgTrans();
        acctgTrans.setAcctgTransTypeId(ContextPermissionPrefixEnum.CTX_OR.getCode());
        acctgTrans.setTransactionDate(this.customTimePeriodDao.get(customTimePeriodId).getThruDate());
        acctgTrans.setGlFiscalTypeId(acctgTransEntry.getGlFiscalTypeId());
        acctgTrans.setVoucherRef(acctgTransEntry.getVoucherRef());
        acctgTrans.setIsPosted("N");
        if (acctgTransEntry.getAcctgTrans() != null) {
        acctgTrans.setPartyId(acctgTransEntry.getAcctgTrans().getPartyId());
        acctgTrans.setRoleTypeId(acctgTransEntry.getAcctgTrans().getRoleTypeId());
        acctgTransEntry.setPartyId(null);
        }
        if (acctgTransEntry.getVoucherRef() != null) {
            acctgTrans.setWorkEffortId(this.workEffortMeasureDao.getWorkEffortMeasureExUomById(acctgTransEntry.getVoucherRef()).getWorkEffortId());
        }
        String acctgTransID = this.acctgTransDao.create(acctgTrans, userLoginId);

        acctgTransEntry.setAcctgTransId(acctgTransID);
        acctgTransEntry.setOrganizationPartyId(this.permissionService.userPrefereceOrganizationUnitId(userLoginId));
        acctgTransEntry.setDebitCreditFlag(glAccount.getDebitCreditDefault());
        acctgTransEntry.setCurrencyUomId(glAccount.getDefaultUomId());
        acctgTransEntry.setAmountLocked("N");
        acctgTransEntry.setOrigAmount(acctgTransEntry.getAmount());
        acctgTransEntry.setPerfAmountCalc(Double.valueOf(0));
        acctgTransEntry.setOrigCurrencyUomId(acctgTransEntry.getCurrencyUomId());
        acctgTransEntry.setIsSummary("N");

        this.acctgTransEntryDao.create(acctgTransEntry, userLoginId);


        return true;
    }

    @Transactional
    public boolean deleteAcctgTransEntries(AcctgTransEntry[] acctgTransEntries) {
        for (int i = 0; i < acctgTransEntries.length; i++) {
            this.acctgTransEntryDao.delete(acctgTransEntries[i]);
            this.acctgTransDao.deleteByPrimaryKey(acctgTransEntries[i].getAcctgTransId());
        }

        return true;
    }

    public AcctgTransEntry getMovementByPrimaryKey(String acctgTransId, String acctgTransEntrySeqId) {
        boolean isAdmin = this.userCtxPermissionViewDao.hasPermission(principal().getUserLoginId(), ContextPermissionPrefixEnum.CTX_AC, Permission.ADMIN);
        String userLoginId = principal().getUserLoginId();
        AcctgTransEntry acctgTransEntry = this.acctgTransEntryDao.getMovementByPrimaryKey(acctgTransId,acctgTransEntrySeqId, isAdmin, userLoginId);
        acctgTransEntry.setAcctgTrans(this.acctgTransDao.selectByPrimaryKey(acctgTransId));
        return acctgTransEntry;
    }


}
