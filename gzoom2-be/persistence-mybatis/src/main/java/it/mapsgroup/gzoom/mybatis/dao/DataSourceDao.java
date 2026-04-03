package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.DataSource;
import it.mapsgroup.gzoom.mybatis.dto.DataSourceEx;
import it.mapsgroup.gzoom.mybatis.mapper.DataSourceMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class DataSourceDao extends AbstractDao{

    private static final Logger LOG = getLogger(DataSourceDao.class);

    private final DataSourceMapper dataSourceMapper;

    @Autowired
    public DataSourceDao(DataSourceMapper dataSourceMapper) {
        this.dataSourceMapper = dataSourceMapper;
    }

    /**
     * This function gets a dataSource given its sequence name.
     *
     * @param id data resource type id of the dataSource
     * @return the corresponding record dataSource
     */
    @Transactional
    public DataSource selectByPrimaryKey(String id) {
        LOG.info("find dataSource by id");

        DataSource dataSource = this.dataSourceMapper.selectByPrimaryKey(id);
        LOG.info("DataSource = {}", (dataSource != null));
        return dataSource;
    }

    /**
     * Gets  dataSource by dataSourceId.
     *
     * @return
     */
    @Transactional
    public DataSource getDataSourceById(String dataSourceId) {
        LOG.info("find dataSource by dataSourceId");
        DataSource dataSource = this.dataSourceMapper.selectByPrimaryKey(dataSourceId);
        LOG.info("dataSource = {}", dataSource != null);
        return dataSource;
    }

    /**
     * Gets a list of dataSource.
     *
     * @return
     */
    @Transactional
    public List<DataSource> getDataSourceList() {
        LOG.info("find all dataSource");

        List<DataSource> dataSourceList = this.dataSourceMapper.selectAll();
        LOG.info("size = {}", dataSourceList.size());
        return dataSourceList;
    }

    /**
     * Gets a list of DataSourceEx.
     *
     * @return
     */
    @Transactional
    public List<DataSourceEx> getDataSourceExList() {
        LOG.info("find all DataSourceEx");

        List<DataSourceEx> dataSourceList = this.dataSourceMapper.getDataSourceEx();
        LOG.info("size = {}", dataSourceList.size());
        return dataSourceList;
    }

    /**
     * This function creates a new record dataSource.
     *
     * @param dataSource dataSource to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */
    @Transactional
    public DataSource create(DataSource dataSource, String userLoginId) {
        LOG.info("create dataSource");
        setCreatedTimestamp(dataSource);
        dataSource.setCreatedByUserLogin(userLoginId);
        int result = this.dataSourceMapper.insert(dataSource);
        LOG.info("result = {}", result);
        return dataSource;
    }

    /**
     * Update of dataSource.
     *
     * @param dataSource dataSource to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(DataSource dataSource, String userLoginId) {
        LOG.info("update dataSource");
        setUpdateTimestamp(dataSource);
        dataSource.setLastModifiedByUserLogin(userLoginId);
        int result = this.dataSourceMapper.updateByPrimaryKey(dataSource);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a dataSource.
     *
     * @param id id of the dataSource to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String id) {
        LOG.info("delete dataSource");
        int result = this.dataSourceMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }



}
