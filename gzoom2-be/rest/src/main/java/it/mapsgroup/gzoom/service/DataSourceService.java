package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.DataSourceDao;
import it.mapsgroup.gzoom.mybatis.dto.DataSource;
import it.mapsgroup.gzoom.mybatis.dto.DataSourceEx;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Leonardo Minaudo.
 */
@Service
public class DataSourceService {
    private static final Logger LOG = getLogger(DataSourceService.class);
    private final DataSourceDao dataSourceDao;

    @Autowired
    public DataSourceService(DataSourceDao dataSourceDao) {
        this.dataSourceDao = dataSourceDao;
    }

    public DataSource getDataSourceById(String dataSourceId) {
        DataSource dataSource = this.dataSourceDao.getDataSourceById(dataSourceId);
        return dataSource;
    }

    public Result<DataSource> getDataSource() {
        List<DataSource> list = this.dataSourceDao.getDataSourceList();
        return new Result<>(list, list.size());
    }

    public Result<DataSourceEx> getDataSourceEx() {
        List<DataSourceEx> list = this.dataSourceDao.getDataSourceExList();
        return new Result<>(list, list.size());
    }

    public DataSource createDataSource(DataSource req) {
        Validators.assertNotNull(req, Messages.DATA_SOURCE_REQUIRED);
        Validators.assertNotBlank(req.getDataSourceTypeId(), Messages.DATA_SOURCE_ID_REQUIRED);
        DataSource record = dataSourceDao.selectByPrimaryKey(req.getDataSourceId());
        Validators.assertTrue(record == null, Messages.DATA_SOURCE_ID_EXIST);
        Validators.assertNotBlank(req.getDescription(), Messages.DATA_SOURCE_DESCRIPTION_REQUIRED);
        return dataSourceDao.create(req, principal().getUserLoginId());
    }

    public boolean updateDataSource(DataSource req) {
        Validators.assertNotNull(req, Messages.DATA_SOURCE_REQUIRED);
        Validators.assertNotNull(req.getDataSourceId(), Messages.DATA_SOURCE_ID_REQUIRED);
        Validators.assertNotBlank(req.getDataSourceTypeId(), Messages.DATA_SOURCE_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(req.getDescription(), Messages.DATA_SOURCE_DESCRIPTION_REQUIRED);
        DataSource record = dataSourceDao.selectByPrimaryKey(req.getDataSourceId());
        Validators.assertNotNull(record, Messages.INVALID_DATA_SOURCE);
        return dataSourceDao.update(req, principal().getUserLoginId());
    }

    public boolean deleteDataSource(String id) {
        DataSource record = dataSourceDao.selectByPrimaryKey(id);
        Validators.assertNotNull(record, Messages.INVALID_DATA_SOURCE_TYPE);
        return dataSourceDao.delete(id);
    }
}
