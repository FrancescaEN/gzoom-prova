package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.DataSourceType;
import it.mapsgroup.gzoom.mybatis.mapper.DataSourceTypeMapper;
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
public class DataSourceTypeDao extends AbstractDao{

    private static final Logger LOG = getLogger(DataSourceTypeDao.class);

    private final DataSourceTypeMapper dataSourceTypeMapper;

    @Autowired
    public DataSourceTypeDao(DataSourceTypeMapper dataSourceTypeMapper) {
        this.dataSourceTypeMapper = dataSourceTypeMapper;
    }

    /**
     * This function gets a dataSourceType given its sequence name.
     *
     * @param id data resource type id of the dataSourceType
     * @return the corresponding record dataSourceType
     */
    @Transactional
    public DataSourceType get(String id) {
        LOG.info("find dataSourceType by id");

        DataSourceType dataSourceType = this.dataSourceTypeMapper.selectByPrimaryKey(id);
        LOG.info("DataSourceType = {}", (dataSourceType != null));
        return dataSourceType;
    }

    /**
     * Gets a list of dataSourceType.
     *
     * @return
     */
    @Transactional
    public List<DataSourceType> getDataSourceTypeList() {
        LOG.info("find all dataSourceType");

        List<DataSourceType> dataSourceTypes = this.dataSourceTypeMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", dataSourceTypes.size());
        return dataSourceTypes;
    }

    /**
     * This function creates a new record dataSourceType.
     *
     * @param dataSourceType dataSourceType to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(DataSourceType dataSourceType, String userLoginId) {
        LOG.info("create dataSourceType");
        setCreatedTimestamp(dataSourceType);
        setCreatedByUserLogin(dataSourceType, userLoginId);
        int result = this.dataSourceTypeMapper.insert(dataSourceType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of dataSourceType.
     *
     * @param dataSourceType dataSourceType to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(DataSourceType dataSourceType, String userLoginId) {
        LOG.info("update dataSourceType");
        setUpdateTimestamp(dataSourceType);
        setLastModifiedByUserLogin(dataSourceType, userLoginId);
        int result = this.dataSourceTypeMapper.updateByPrimaryKey(dataSourceType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a dataSourceType.
     *
     * @param id id of the dataSourceType to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String id) {
        LOG.info("delete dataSourceType");
        int result = this.dataSourceTypeMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

    private void setCreatedByUserLogin(DataSourceType dataSourceType, String userLoginId) {
        dataSourceType.setCreatedByUserLogin(userLoginId);
    }

    private void setLastModifiedByUserLogin(DataSourceType dataSourceType, String userLoginId) {
        dataSourceType.setLastModifiedByUserLogin(userLoginId);
    }

}
