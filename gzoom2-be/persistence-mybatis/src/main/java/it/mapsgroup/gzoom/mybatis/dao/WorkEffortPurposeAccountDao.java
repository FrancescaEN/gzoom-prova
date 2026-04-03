package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountType;
import it.mapsgroup.gzoom.mybatis.dto.StatusValidChange;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPurposeAccount;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortPurposeAccountMapper;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortPurposeAccountDao extends AbstractDao {
    private static final Logger LOG = getLogger(WorkEffortPurposeAccountDao.class);
    private final WorkEffortPurposeAccountMapper workEffortPurposeAccountMapper;
    @Autowired
    public WorkEffortPurposeAccountDao(WorkEffortPurposeAccountMapper workEffortPurposeAccountMapper) {
        this.workEffortPurposeAccountMapper = workEffortPurposeAccountMapper;
    }

    @Transactional
    public boolean create(WorkEffortPurposeAccount workEffortPurposeAccount, String userLoginId) {
        LOG.info("create workEffortPurposeAccount");
        setCreatedTimestamp(workEffortPurposeAccount);
        workEffortPurposeAccount.setCreatedByUserLogin(userLoginId);
        int result = this.workEffortPurposeAccountMapper.insert(workEffortPurposeAccount);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean deleteByGlAccountId(String glAccountId) {
        LOG.info("delete workEffortPurposeAccount by glAccountId");
        int result = this.workEffortPurposeAccountMapper.deleteByGlAccountId(glAccountId);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean deleteByPrimaryKey(String workEffortPurposeTypeId,  String glAccountId) {
        LOG.info("delete workEffortPurposeAccount");
        int result = this.workEffortPurposeAccountMapper.deleteByPrimaryKey(workEffortPurposeTypeId, glAccountId);
        LOG.info("result = {}", result);
        return result > 0;
    }


    @Transactional
    public int countByGlAccountId(String glAccountId) {
        LOG.info("count workEffortPurposeAccount by glAccountId");
        int result = this.workEffortPurposeAccountMapper.countByGlAccountId(glAccountId);
        LOG.info("result = {}", result);
        return result;
    }

    @Transactional
    public WorkEffortPurposeAccount getWorkEffortPurposeAccount(String workEffortPurposeTypeId, String glAccountId) {
        LOG.info("find workEffortPurposeAccount by id");
        WorkEffortPurposeAccount workEffortPurposeAccount = this.workEffortPurposeAccountMapper.selectByPrimaryKey(workEffortPurposeTypeId, glAccountId);
        LOG.info("WorkEffortPurposeAccount = {}", (workEffortPurposeAccount != null));
        return workEffortPurposeAccount;
    }

    @Transactional
    public boolean update(WorkEffortPurposeAccount workEffortPurposeAccount, String userLoginId) {
        LOG.info("update WorkEffortPurposeAccount");
        setUpdateTimestamp(workEffortPurposeAccount);
        workEffortPurposeAccount.setLastModifiedByUserLogin(userLoginId);
        int result = this.workEffortPurposeAccountMapper.updateByPrimaryKey(workEffortPurposeAccount);
        LOG.info("result = {}", result);
        return result > 0;
    }


}
