package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.StandardImportFieldConfigDao;
import it.mapsgroup.gzoom.mybatis.dto.StandardImportFieldConfig;
import it.mapsgroup.gzoom.mybatis.dto.StandardImportFieldConfigEx;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Leonardo Minaudo.
 */
@Service
public class StandardImportFieldConfigService {
    private static final Logger LOG = getLogger(StandardImportFieldConfigService.class);
    private final StandardImportFieldConfigDao standardImportFieldConfigDao;

    @Autowired
    public StandardImportFieldConfigService(StandardImportFieldConfigDao standardImportFieldConfigDao) {
        this.standardImportFieldConfigDao = standardImportFieldConfigDao;
    }

    public Result<StandardImportFieldConfig> getStandardImportFieldConfig() {
        List<StandardImportFieldConfig> list = this.standardImportFieldConfigDao.getStandardImportFieldConfigList();
        return new Result<>(list, list.size());
    }

    public Result<StandardImportFieldConfigEx> getStandardImportFieldConfigEx(String dataSourceId) {
        List<StandardImportFieldConfigEx> list = this.standardImportFieldConfigDao.getStandardImportFieldConfigExList(dataSourceId);
        return new Result<>(list, list.size());
    }

    public StandardImportFieldConfig createStandardImportFieldConfig(StandardImportFieldConfig standardImportFieldConfig) {
        Validators.assertNotNull(standardImportFieldConfig, Messages.STANDARD_IMPORT_FIELD_CONFIG_REQUIRED);
        Validators.assertNotBlank(standardImportFieldConfig.getDataSourceId(), Messages.DATA_SOURCE_ID_REQUIRED);
        Validators.assertNotBlank(standardImportFieldConfig.getStandardInterface(), Messages.STANDARD_INTERFACE_REQUIRED);
        Validators.assertNotBlank(standardImportFieldConfig.getInternalFieldName(), Messages.INTERNAL_FIELD_NAME_REQUIRED);
        Validators.assertNotBlank(standardImportFieldConfig.getInterfaceSeq().toString(), Messages.INTERNAL_SEQ_REQUIRED);
        StandardImportFieldConfig record = standardImportFieldConfigDao.selectByPrimaryKey(standardImportFieldConfig.getDataSourceId(),standardImportFieldConfig.getStandardInterface(), standardImportFieldConfig.getInternalFieldName(), standardImportFieldConfig.getInterfaceSeq());
        Validators.assertTrue(record == null, Messages.STANDARD_IMPORT_FIELD_CONFIG_EXIST);
        return standardImportFieldConfigDao.create(standardImportFieldConfig, principal().getUserLoginId());
    }

    public boolean updateStandardImportFieldConfig(StandardImportFieldConfig standardImportFieldConfig) {
        Validators.assertNotNull(standardImportFieldConfig, Messages.STANDARD_IMPORT_FIELD_CONFIG_REQUIRED);
        Validators.assertNotBlank(standardImportFieldConfig.getDataSourceId(), Messages.DATA_SOURCE_ID_REQUIRED);
        Validators.assertNotBlank(standardImportFieldConfig.getStandardInterface(), Messages.STANDARD_INTERFACE_REQUIRED);
        Validators.assertNotBlank(standardImportFieldConfig.getInternalFieldName(), Messages.INTERNAL_FIELD_NAME_REQUIRED);
        Validators.assertNotBlank(standardImportFieldConfig.getInterfaceSeq().toString(), Messages.INTERNAL_SEQ_REQUIRED);
        StandardImportFieldConfig record = standardImportFieldConfigDao.selectByPrimaryKey(standardImportFieldConfig.getDataSourceId(),standardImportFieldConfig.getStandardInterface(), standardImportFieldConfig.getInternalFieldName(), standardImportFieldConfig.getInterfaceSeq());
        Validators.assertNotNull(record, Messages.STANDARD_IMPORT_FIELD_CONFIG_EXIST);
        return standardImportFieldConfigDao.update(standardImportFieldConfig, principal().getUserLoginId());
    }

    public boolean deleteStandardImportFieldConfig(String dataSourceId, String standardInterface, String internalFieldName, BigDecimal interfaceSeq) {
        StandardImportFieldConfig record = standardImportFieldConfigDao.selectByPrimaryKey(dataSourceId, standardInterface, internalFieldName, interfaceSeq);
        Validators.assertNotNull(record, Messages.STANDARD_IMPORT_FIELD_CONFIG_EXIST);
        return standardImportFieldConfigDao.delete(dataSourceId, standardInterface, internalFieldName, interfaceSeq);
    }
}
