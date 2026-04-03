package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortContentType;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortContentTypeMapper;
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
public class WorkEffortContentTypeDao extends AbstractDao {
    private static final Logger LOG = getLogger(WorkEffortContentTypeDao.class);
    private final WorkEffortContentTypeMapper workEffortContentTypeMapper;

    @Autowired
    public WorkEffortContentTypeDao(WorkEffortContentTypeMapper workEffortContentTypeMapper) {
        this.workEffortContentTypeMapper = workEffortContentTypeMapper;
    }

    /**
     * This function gets a workEffortContentType given its id.
     *
     * @param workEffortContentTypeId data resource type id of the workEffortContentType
     * @return the corresponding workEffortContentType record
     */
    @Transactional
    public WorkEffortContentType get(String workEffortContentTypeId) {
        LOG.info("find workEffortContentType by id");

        WorkEffortContentType workEffortContentType = this.workEffortContentTypeMapper.selectByPrimaryKey(workEffortContentTypeId);
        LOG.info("WorkEffortContentType = {}", (workEffortContentType != null));
        return workEffortContentType;
    }


    /**
     * Gets a list of workEffortContentType.
     *
     * @return
     */
    @Transactional
    public List<WorkEffortContentType> getWorkEffortContentTypeList() {
        LOG.info("find all workEffortContentType");

        List<WorkEffortContentType> workEffortContentTypes = this.workEffortContentTypeMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", workEffortContentTypes.size());
        return workEffortContentTypes;
    }

    /**
     * This function creates a new record workEffortContentType.
     *
     * @param workEffortContentType workEffortContentType to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(WorkEffortContentType workEffortContentType, String userLoginId) {
        LOG.info("create workEffortContentType");
        setCreatedTimestamp(workEffortContentType);
        workEffortContentType.setCreatedByUserLogin(userLoginId);
        int result = this.workEffortContentTypeMapper.insert(workEffortContentType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of workEffortContentType.
     *
     * @param workEffortContentType workEffortContentType to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(WorkEffortContentType workEffortContentType, String userLoginId) {
        LOG.info("update workEffortContentType");
        setUpdateTimestamp(workEffortContentType);
        workEffortContentType.setLastModifiedByUserLogin(userLoginId);
        int result = this.workEffortContentTypeMapper.updateByPrimaryKey(workEffortContentType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a workEffortContentType.
     *
     * @param id id of the workEffortContentType to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String id) {
        LOG.info("delete workEffortContentType");
        int result = this.workEffortContentTypeMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public List<WorkEffortContentType> getContentTypeList(String workEffortId) {
        LOG.info("getContentTypeList");

        List<WorkEffortContentType> workEffortContentTypes = this.workEffortContentTypeMapper.getContentTypeList(workEffortId);
        LOG.info("size = {}", workEffortContentTypes.size());
        return workEffortContentTypes;
    }

    @Transactional
    public WorkEffortContentType getContentTypeId(String workEffortId, String workEffortContentTypeId) {
        LOG.info("getContentTypeId");

        WorkEffortContentType workEffortContentTypes = this.workEffortContentTypeMapper.getContentTypeId(workEffortId, workEffortContentTypeId);
        LOG.info("size = {}", (workEffortContentTypes != null));
        return workEffortContentTypes;
    }

}
