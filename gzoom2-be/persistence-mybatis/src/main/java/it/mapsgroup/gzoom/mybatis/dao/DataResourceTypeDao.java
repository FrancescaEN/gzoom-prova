package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.DataResourceType;
import it.mapsgroup.gzoom.mybatis.mapper.DataResourceTypeMapper;
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
public class DataResourceTypeDao extends AbstractDao {
    private static final Logger LOG = getLogger(DataResourceTypeDao.class);
    private final DataResourceTypeMapper dataResourceTypeMapper;

    @Autowired
    public DataResourceTypeDao(DataResourceTypeMapper dataResourceTypeMapper) {
        this.dataResourceTypeMapper = dataResourceTypeMapper;
    }

    @Transactional
    public List<DataResourceType> getDataResourceTypeList() {
        LOG.info("find all dataResourceType");

        List<DataResourceType> dataResourceTypes = this.dataResourceTypeMapper.selectAll();
        LOG.info("size = {}", dataResourceTypes.size());
        return dataResourceTypes;
    }

    /**
     * This function gets a dataResourceType given its sequence name.
     *
     * @param dataResourceTypeId data resource type id of the dataResourceType
     * @return the corresponding dataResourceType record
     */
    @Transactional
    public DataResourceType get(String dataResourceTypeId) {
        LOG.info("find dataResourceType by id");

        DataResourceType dataResourceType = this.dataResourceTypeMapper.selectByPrimaryKey(dataResourceTypeId);
        LOG.info("DataResourceType = {}", (dataResourceType != null));
        return dataResourceType;
    }

    /**
     * This function creates a new record dataResourceType.
     *
     * @param dataResourceType dataResourceType to add
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(DataResourceType dataResourceType) {
        LOG.info("create dataResourceType");
        setCreatedTimestamp(dataResourceType);
        int result = this.dataResourceTypeMapper.insert(dataResourceType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of dataResourceType.
     *
     * @param dataResourceType dataResourceType to update
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(DataResourceType dataResourceType) {
        LOG.info("update dataResourceType");
        setUpdateTimestamp(dataResourceType);
        int result = this.dataResourceTypeMapper.updateByPrimaryKey(dataResourceType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a dataResourceType.
     *
     * @param id id of the dataResourceType to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String id) {
        LOG.info("delete dataResourceType");
        int result = this.dataResourceTypeMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }


}
