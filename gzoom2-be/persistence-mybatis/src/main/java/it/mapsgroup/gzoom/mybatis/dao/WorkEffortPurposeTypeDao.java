package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPurposeType;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPurposeTypeEx;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortPurposeTypeMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortPurposeTypeDao extends AbstractDao{
    private static final Logger LOG = getLogger(WorkEffortPurposeTypeDao.class);
    private final WorkEffortPurposeTypeMapper workEffortPurposeTypeMapper;

    @Autowired
    public WorkEffortPurposeTypeDao(WorkEffortPurposeTypeMapper workEffortPurposeTypeMapper) {
        this.workEffortPurposeTypeMapper = workEffortPurposeTypeMapper;
    }

    /**
     * This function gets a workEffortPurposeType given its id.
     *
     * @param id id of the workEffortPurposeType
     * @return the corresponding workEffortPurposeType record
     */
    @Transactional
    public WorkEffortPurposeType get(String id) {
        LOG.info("find workEffortPurposeType by id");

        WorkEffortPurposeType workEffortPurposeType = this.workEffortPurposeTypeMapper.selectByPrimaryKey(id);
        LOG.info("WorkEffortPurposeType = {}", (workEffortPurposeType != null));
        return workEffortPurposeType;
    }


    /**
     * Gets a list of workEffortPurposeType.
     *
     * @return
     */
    @Transactional
    public List<WorkEffortPurposeType> getWorkEffortPurposeTypeList() {
        LOG.info("find all workEffortPurposeType");
        List<WorkEffortPurposeType> workEffortPurposeTypes = this.workEffortPurposeTypeMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", workEffortPurposeTypes.size());
        return workEffortPurposeTypes;
    }

    /**
     * Gets a list of workEffortPurposeType by purposeTypeEnumId.
     *
     * @return
     */
    @Transactional
    public List<WorkEffortPurposeType> selectByPurposeTypeEnumId(String purposeTypeEnumId) {
        LOG.info("find all workEffortPurposeType by purposeTypeEnumId");
        List<WorkEffortPurposeType> workEffortPurposeTypes = this.workEffortPurposeTypeMapper.selectByPurposeTypeEnumId(purposeTypeEnumId);
        LOG.info("size = {}", workEffortPurposeTypes.size());
        return workEffortPurposeTypes;
    }

    @Transactional
    public List<WorkEffortPurposeTypeEx> getPurposeTabType(String glAccountId, boolean in) {
        LOG.info("getPurposeTabType");
        List<WorkEffortPurposeTypeEx> workEffortPurposeTypeExes = this.workEffortPurposeTypeMapper.getPurposeTabType(glAccountId, in);
        LOG.info("size = {}", workEffortPurposeTypeExes.size());
        return workEffortPurposeTypeExes;
    }

    /**
     * This function creates a new record workEffortPurposeType.
     *
     * @param workEffortPurposeType workEffortPurposeType to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(WorkEffortPurposeType workEffortPurposeType, String userLoginId) {
        LOG.info("create workEffortPurposeType");
        setCreatedTimestamp(workEffortPurposeType);
        workEffortPurposeType.setCreatedByUserLogin(userLoginId);
        int result = this.workEffortPurposeTypeMapper.insert(workEffortPurposeType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of workEffortPurposeType.
     *
     * @param workEffortPurposeType workEffortPurposeType to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(WorkEffortPurposeType workEffortPurposeType, String userLoginId) {
        LOG.info("update workEffortPurposeType");
        setUpdateTimestamp(workEffortPurposeType);
        workEffortPurposeType.setLastModifiedByUserLogin(userLoginId);
        int result = this.workEffortPurposeTypeMapper.updateByPrimaryKey(workEffortPurposeType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a workEffortPurposeType.
     *
     * @param id id of the workEffortPurposeType to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String id) {
        LOG.info("delete workEffortPurposeType");
        int result = this.workEffortPurposeTypeMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
