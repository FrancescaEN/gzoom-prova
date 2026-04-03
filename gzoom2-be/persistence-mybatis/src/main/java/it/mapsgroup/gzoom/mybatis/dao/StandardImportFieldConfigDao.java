package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.StandardImportFieldConfig;
import it.mapsgroup.gzoom.mybatis.dto.StandardImportFieldConfigEx;
import it.mapsgroup.gzoom.mybatis.mapper.StandardImportFieldConfigMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class StandardImportFieldConfigDao extends AbstractDao{

    private static final Logger LOG = getLogger(StandardImportFieldConfigDao.class);

    private final StandardImportFieldConfigMapper standardImportFieldConfigMapper;

    @Autowired
    public StandardImportFieldConfigDao(StandardImportFieldConfigMapper standardImportFieldConfigMapper) {
        this.standardImportFieldConfigMapper = standardImportFieldConfigMapper;
    }

    /**
     * This function gets a standardImportFieldConfig given its sequence name.
     *
     * @param dataSourceId data source id of the standardImportFieldConfig
     * @param standardInterface standardInterface of the standardImportFieldConfig
     * @param internalFieldName internalFieldName of the standardImportFieldConfig
     * @param interfaceSeq interfaceSeq of the standardImportFieldConfig
     * @return the corresponding record dataSource
     */
    @Transactional
    public StandardImportFieldConfig selectByPrimaryKey(String dataSourceId, String standardInterface, String internalFieldName, BigDecimal interfaceSeq) {
        LOG.info("find dataSource by id");

        StandardImportFieldConfig standardImportFieldConfig = this.standardImportFieldConfigMapper.selectByPrimaryKey(dataSourceId, standardInterface, internalFieldName, interfaceSeq );
        LOG.info("standardImportFieldConfig = {}", (standardImportFieldConfig != null));
        return standardImportFieldConfig;
    }

    /**
     * Gets a list of standardImportFieldConfig.
     *
     * @return
     */
    @Transactional
    public List<StandardImportFieldConfig> getStandardImportFieldConfigList() {
        LOG.info("find all StandardImportFieldConfig");

        List<StandardImportFieldConfig> standardImportFieldConfigList = this.standardImportFieldConfigMapper.selectAll();
        LOG.info("size = {}", standardImportFieldConfigList.size());
        return standardImportFieldConfigList;
    }

    /**
     * Gets a list of standardImportFieldConfigEx.
     *
     * @return
     */
    @Transactional
    public List<StandardImportFieldConfigEx> getStandardImportFieldConfigExList(String dataSourceId) {
        LOG.info("find all DataSourceEx");

        List<StandardImportFieldConfigEx> standardImportFieldConfigExList = this.standardImportFieldConfigMapper.getStandardImportFieldConfigEx(dataSourceId);
        LOG.info("size = {}", standardImportFieldConfigExList.size());
        return standardImportFieldConfigExList;
    }

    /**
     * This function creates a new record dataSource.
     *
     * @param standardImportFieldConfig standardImportFieldConfig to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */
    @Transactional
    public StandardImportFieldConfig create(StandardImportFieldConfig standardImportFieldConfig, String userLoginId) {
        LOG.info("create standardImportFieldConfig");
        setCreatedTimestamp(standardImportFieldConfig);
        standardImportFieldConfig.setCreatedByUserLogin(userLoginId);
        int result = this.standardImportFieldConfigMapper.insert(standardImportFieldConfig);
        LOG.info("result = {}", result);
        return standardImportFieldConfig;
    }

    /**
     * Update of dataSource.
     *
     * @param standardImportFieldConfig standardImportFieldConfig to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(StandardImportFieldConfig standardImportFieldConfig, String userLoginId) {
        LOG.info("update standardImportFieldConfig");
        setUpdateTimestamp(standardImportFieldConfig);
        standardImportFieldConfig.setLastModifiedByUserLogin(userLoginId);
        int result = this.standardImportFieldConfigMapper.updateByPrimaryKey(standardImportFieldConfig);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a StandardImportFieldConfig.
     *
     * @param dataSourceId data source id of the standardImportFieldConfig
     * @param standardInterface standardInterface of the standardImportFieldConfig
     * @param internalFieldName internalFieldName of the standardImportFieldConfig
     * @param interfaceSeq interfaceSeq of the standardImportFieldConfig
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String dataSourceId, String standardInterface, String internalFieldName, BigDecimal interfaceSeq) {
        LOG.info("delete StandardImportFieldConfig");
        int result = this.standardImportFieldConfigMapper.deleteByPrimaryKey(dataSourceId, standardInterface, internalFieldName, interfaceSeq);
        LOG.info("result = {}", result);
        return result > 0;
    }



}
