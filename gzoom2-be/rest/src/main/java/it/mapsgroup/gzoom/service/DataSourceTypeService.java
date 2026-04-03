package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.DataSourceTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Leonardo Minaudo.
 */
@Service
public class DataSourceTypeService {
    private static final Logger LOG = getLogger(DataSourceTypeService.class);
    private final DataSourceTypeDao dataSourceTypeDao;

    @Autowired
    public DataSourceTypeService(DataSourceTypeDao dataSourceTypeDao) {
        this.dataSourceTypeDao = dataSourceTypeDao;
    }

    public Result<DataSourceType> getDataSourceType() {
        List<DataSourceType> list = this.dataSourceTypeDao.getDataSourceTypeList();
        return new Result<>(list, list.size());
    }

    public boolean createDataSourceType(DataSourceType req) {
        Validators.assertNotNull(req, Messages.DATA_SOURCE_TYPE_REQUIRED);
        Validators.assertNotBlank(req.getDataSourceTypeId(), Messages.DATA_SOURCE_TYPE_ID_REQUIRED);
        DataSourceType record = dataSourceTypeDao.get(req.getDataSourceTypeId());
        Validators.assertTrue(record == null, Messages.DATA_SOURCE_TYPE_ID_EXIST);
        Validators.assertNotBlank(req.getDescription(), Messages.DATA_SOURCE_TYPE_DESCRIPTION_REQUIRED);
        return dataSourceTypeDao.create(req, principal().getUserLoginId());
    }

    public boolean updateDataSourceType(DataSourceType req) {
        Validators.assertNotNull(req, Messages.DATA_SOURCE_TYPE_REQUIRED);
        Validators.assertNotBlank(req.getDataSourceTypeId(), Messages.DATA_SOURCE_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(req.getDescription(), Messages.DATA_SOURCE_TYPE_DESCRIPTION_REQUIRED);
        DataSourceType record = dataSourceTypeDao.get(req.getDataSourceTypeId());
        Validators.assertNotNull(record, Messages.INVALID_DATA_SOURCE_TYPE);
        return dataSourceTypeDao.update(req, principal().getUserLoginId());
    }

    public boolean deleteDataSourceType(String id) {
        DataSourceType record = dataSourceTypeDao.get(id);
        Validators.assertNotNull(record, Messages.INVALID_DATA_SOURCE_TYPE);
        return dataSourceTypeDao.delete(id);
    }
}
