package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.DataResourceTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.DataResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.List;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Leonardo Minaudo.
 */
@Service
public class DataResourceTypeService {
    private static final Logger LOG = getLogger(DataResourceTypeService.class);
    private final DataResourceTypeDao dataResourceTypeDao;

    @Autowired
    public DataResourceTypeService(DataResourceTypeDao dataResourceTypeDao) {
        this.dataResourceTypeDao = dataResourceTypeDao;
    }

    public Result<DataResourceType> getDataResourceType() {
        List<DataResourceType> list = this.dataResourceTypeDao.getDataResourceTypeList();
        return new Result<>(list, list.size());
    }

    public boolean createDataResourceType(DataResourceType req) {
        Validators.assertNotNull(req, Messages.DATA_RESOURCE_TYPE_REQUIRED);
        Validators.assertNotBlank(req.getDataResourceTypeId(), Messages.DATA_RESOURCE_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(req.getDescription(), Messages.DATA_RESOURCE_TYPE_DESCRIPTION_REQUIRED);
        return dataResourceTypeDao.create(req);
    }

    public boolean updateDataResourceType(DataResourceType req) {
        Validators.assertNotNull(req, Messages.DATA_RESOURCE_TYPE_REQUIRED);
        Validators.assertNotBlank(req.getDataResourceTypeId(), Messages.DATA_RESOURCE_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(req.getDescription(), Messages.DATA_RESOURCE_TYPE_DESCRIPTION_REQUIRED);
        DataResourceType record = dataResourceTypeDao.get(req.getDataResourceTypeId());
        Validators.assertNotNull(record, Messages.INVALID_DATA_RESOURCE_TYPE);
        return dataResourceTypeDao.update(req);
    }

    public boolean deleteDataResourceType(String id) {
        DataResourceType record = dataResourceTypeDao.get(id);
        Validators.assertNotNull(record, Messages.INVALID_DATA_RESOURCE_TYPE);
        return dataResourceTypeDao.delete(id);
    }
}
