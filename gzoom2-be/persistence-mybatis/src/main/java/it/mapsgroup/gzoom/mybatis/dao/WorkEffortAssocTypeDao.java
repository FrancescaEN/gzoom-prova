package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssocType;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortAssocTypeMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortAssocTypeDao extends AbstractDao{
    private static final Logger LOG = getLogger(WorkEffortAssocTypeDao.class);
    private final WorkEffortAssocTypeMapper workEffortAssocTypeMapper;

    @Autowired
    public WorkEffortAssocTypeDao(WorkEffortAssocTypeMapper workEffortAssocTypeMapper) {
        this.workEffortAssocTypeMapper = workEffortAssocTypeMapper;
    }

    /**
     * This function creates a new record workEffortAssocType.
     *
     * @param record workEffortAssocType to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(WorkEffortAssocType record, String userLoginId) {
        LOG.info("create workEffortAssocType");
        setCreatedTimestamp(record);
        setCreatedByUserLogin(record, userLoginId);
        int result = this.workEffortAssocTypeMapper.insert(record);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * This function gets a workEffortAssocType given its id.
     *
     * @param id id of the workEffortAssocType
     * @return the corresponding workEffortAssocType record
     */
    @Transactional
    public WorkEffortAssocType get(String id) {
        LOG.info("find workEffortAssocType by id");

        WorkEffortAssocType workEffortAssocType = this.workEffortAssocTypeMapper.selectByPrimaryKey(id);
        LOG.info("WorkEffortAssocType = {}", (workEffortAssocType != null));
        return workEffortAssocType;
    }

    /**
     * Gets a list of workEffortAssocType.
     *
     * @return
     */
    @Transactional
    public List<WorkEffortAssocType> selectAllOrderByPrimaryKey() {
        LOG.info("find all workEffortAssocType");

        List<WorkEffortAssocType> workEffortAssocTypes = this.workEffortAssocTypeMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", workEffortAssocTypes.size());
        return workEffortAssocTypes;
    }

    /**
     * Update of workEffortAssocType.
     *
     * @param record workEffortAssocType to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(WorkEffortAssocType record, String userLoginId) {
        LOG.info("update workEffortAssocType");
        setUpdateTimestamp(record);
        setLastModifiedByUserLogin(record, userLoginId);
        int result = this.workEffortAssocTypeMapper.updateByPrimaryKey(record);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a workEffortAssocType.
     *
     * @param id id of the workEffortAssocType to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String id) {
        LOG.info("delete workEffortAssocType");
        int result = this.workEffortAssocTypeMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

    private void setCreatedByUserLogin(WorkEffortAssocType record, String userLoginId) {
        record.setCreatedByUserLogin(userLoginId);
    }

    private void setLastModifiedByUserLogin(WorkEffortAssocType record, String userLoginId) {
        record.setLastModifiedByUserLogin(userLoginId);
    }
}
