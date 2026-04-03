package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortSequence;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortSequenceMapper;
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
public class WorkEffortSequenceDao extends AbstractDao{
    private static final Logger LOG = getLogger(WorkEffortSequenceDao.class);
    private final WorkEffortSequenceMapper workEffortSequenceMapper;

    @Autowired
    public WorkEffortSequenceDao(WorkEffortSequenceMapper workEffortSequenceMapper) {
        this.workEffortSequenceMapper = workEffortSequenceMapper;
    }

    /**
     * This function gets a workEffortSequence given its sequence name.
     *
     * @param id sequence name of the workEffortSequence
     * @return the corresponding workEffortSequence record
     */
    @Transactional
    public WorkEffortSequence get(String id) {
        LOG.info("find workEffortSequence by id");

        WorkEffortSequence workEffortSequence = this.workEffortSequenceMapper.selectByPrimaryKey(id);
        LOG.info("WorkEffortSequence = {}", (workEffortSequence != null));
        return workEffortSequence;
    }

    /**
     * Gets a list of workEffortSequence.
     *
     * @return
     */
    @Transactional
    public List<WorkEffortSequence> selectAllOrderByPrimaryKey() {
        LOG.info("find all workEffortSequence");

        List<WorkEffortSequence> workEffortSequences = this.workEffortSequenceMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", workEffortSequences.size());
        return workEffortSequences;
    }

    /**
     * This function creates a new record workEffortSequence.
     *
     * @param record workEffortSequence to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(WorkEffortSequence record, String userLoginId) {
        LOG.info("create workEffortSequence");
        setCreatedTimestamp(record);
        setCreatedByUserLogin(record, userLoginId);
        int result = this.workEffortSequenceMapper.insert(record);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of workEffortSequence.
     *
     * @param record workEffortSequence to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(WorkEffortSequence record, String userLoginId) {
        LOG.info("update workEffortSequence");
        setUpdateTimestamp(record);
        setLastModifiedByUserLogin(record, userLoginId);
        int result = this.workEffortSequenceMapper.updateByPrimaryKey(record);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a workEffortSequence.
     *
     * @param id id of the workEffortSequence to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String id) {
        LOG.info("delete workEffortSequence");
        int result = this.workEffortSequenceMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

    private void setCreatedByUserLogin(WorkEffortSequence record, String userLoginId) {
        record.setCreatedByUserLogin(userLoginId);
    }

    private void setLastModifiedByUserLogin(WorkEffortSequence record, String userLoginId) {
        record.setLastModifiedByUserLogin(userLoginId);
    }

}
