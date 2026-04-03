package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.commons.TableLazyLoad;
import it.mapsgroup.gzoom.mybatis.dto.AcctgTransEntry;
import it.mapsgroup.gzoom.mybatis.mapper.AcctgTransEntryMapper;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import it.mapsgroup.gzoom.mybatis.util.Permission;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class AcctgTransEntryDao extends AbstractDao{
    private static final Logger LOG = getLogger(AcctgTransEntryDao.class);
    private final SequenceGenerator sequenceGenerator;
    private final AcctgTransEntryMapper acctgTransEntryMapper;
    private final UserCtxPermissionViewDao userCtxPermissionViewDao;

    @Autowired
    public AcctgTransEntryDao(SequenceGenerator sequenceGenerator, AcctgTransEntryMapper acctgTransEntryMapper, UserCtxPermissionViewDao userCtxPermissionViewDao) {
        this.sequenceGenerator = sequenceGenerator;
        this.acctgTransEntryMapper = acctgTransEntryMapper;
        this.userCtxPermissionViewDao = userCtxPermissionViewDao;
    }

    @Transactional
    public List<AcctgTransEntry> getAll() {
        LOG.info("find all acctgTransInterface");
        List<AcctgTransEntry> acctgTransEntryList = this.acctgTransEntryMapper.selectAll();
        LOG.info("size = {}", acctgTransEntryList.size());
        return acctgTransEntryList;
    }

    @Transactional
    public List<AcctgTransEntry> getAcctgTransEntryByWorkEffortMeasureId(String workEffortMeasureId) {
        LOG.info("find all AcctgTransEntry by workEffortMeasureId");
        List<AcctgTransEntry> acctgTransEntryList = this.acctgTransEntryMapper.getAcctgTransEntryByWorkEffortMeasureId(workEffortMeasureId);
        LOG.info("size = {}", acctgTransEntryList.size());
        return acctgTransEntryList;
    }

    @Transactional
    public List<AcctgTransEntry> getIndicatorMovements(
            String accountTypeEnumId,
            String isReservedAccount,
            String organizationId,
            String inputEnumId,
            String detectOrgUnitIdFlag,
            String userLoginId,
            TableLazyLoad tableLazyLoad, String glAccountId, String uomId, String customTimePeriodId, String glFiscalTypeId) {
        LOG.info("getIndicatorMovements");
        boolean isAdmin = this.userCtxPermissionViewDao.hasPermission(userLoginId, ContextPermissionPrefixEnum.CTX_AC, Permission.ADMIN);
        List<AcctgTransEntry> acctgTransEntryList = this.acctgTransEntryMapper.getIndicatorMovements(
                accountTypeEnumId,
                isReservedAccount,
                organizationId,
                inputEnumId,
                detectOrgUnitIdFlag,
                userLoginId,
                isAdmin,
                tableLazyLoad,
                glAccountId,
                uomId,
                customTimePeriodId,
                glFiscalTypeId);
        LOG.info("size = {}", acctgTransEntryList.size());
        return acctgTransEntryList;
    }

    @Transactional
    public Integer countIndicatorMovements(
            String accountTypeEnumId,
            String isReservedAccount,
            String organizationId,
            String inputEnumId,
            String detectOrgUnitIdFlag,
            String userLoginId,
            TableLazyLoad tableLazyLoad,
            String glAccountId, String uomId, String customTimePeriodId, String glFiscalTypeId) {
        LOG.info("countIndicatorMovements");
        boolean isAdmin = this.userCtxPermissionViewDao.hasPermission(userLoginId, ContextPermissionPrefixEnum.CTX_AC, Permission.ADMIN);
        Integer total = this.acctgTransEntryMapper.countIndicatorMovements(accountTypeEnumId, isReservedAccount, organizationId, inputEnumId, detectOrgUnitIdFlag, userLoginId, isAdmin, tableLazyLoad, glAccountId,
                uomId,
                customTimePeriodId,
                glFiscalTypeId);
        LOG.info("size = {}", total);
        return total;
    }

    @Transactional
    public boolean create(AcctgTransEntry record, String userLoginId) {
        String id = sequenceGenerator.getNextSeqId("AcctgTransEntry");
        record.setAcctgTransEntrySeqId(id);
        record.setCreatedByUserLogin(userLoginId);
        setCreatedTimestamp(record);
        int result = this.acctgTransEntryMapper.insert(record);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(AcctgTransEntry acctgTransEntry) {
        LOG.info("delete acctgTransEntry");
        int result = this.acctgTransEntryMapper.deleteByPrimaryKey(acctgTransEntry.getAcctgTransId(), acctgTransEntry.getAcctgTransEntrySeqId());
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean updatePartialTable(AcctgTransEntry acctgTransEntry, String userLoginId) {
        LOG.info("updatePartialTable");
        setUpdateTimestamp(acctgTransEntry);
        acctgTransEntry.setLastModifiedByUserLogin(userLoginId);
        int result = this.acctgTransEntryMapper.updatePartialTable(acctgTransEntry);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public AcctgTransEntry getMovementByPrimaryKey(String acctgTransId, String acctgTransEntrySeqId, boolean isAdmin, String userLoginId) {
        LOG.info("getMovementByPrimaryKey");
        AcctgTransEntry acctgTransEntry = this.acctgTransEntryMapper.getMovementByPrimaryKey(acctgTransId, acctgTransEntrySeqId, isAdmin, userLoginId);
        LOG.info("acctgTransEntry = {}", (acctgTransEntry != null));
        return acctgTransEntry;
    }

    @Transactional
    public boolean updateDetail(AcctgTransEntry acctgTransEntry, String userLoginId) {
        LOG.info("updateDetail acctgTransEntry");
        setUpdateTimestamp(acctgTransEntry);
        acctgTransEntry.setLastModifiedByUserLogin(userLoginId);
        int result = this.acctgTransEntryMapper.updateDetail(acctgTransEntry);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
