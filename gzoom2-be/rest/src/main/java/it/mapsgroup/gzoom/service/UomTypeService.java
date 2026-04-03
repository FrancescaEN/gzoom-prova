package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.UomTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.UomType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Profile service.
 *
 */
@Service
public class UomTypeService {
    private static final Logger LOG = getLogger(UomTypeService.class);

    private final UomTypeDao uomTypeDao;

    @Autowired
    public UomTypeService(UomTypeDao uomTypeDao) {
        this.uomTypeDao = uomTypeDao;
    }

    public Result<UomType> getUomTypes() {
        List<UomType> list = uomTypeDao.getUomTypes();
        return new Result<>(list, list.size());
    }

    public String createUomType(UomType req) {
        Validators.assertNotNull(req, Messages.UOM_TYPE_REQUIRED);
        Validators.assertNotBlank(req.getUomTypeId(), Messages.UOM_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(req.getDescription(), Messages.UOM_TYPE_DESCRIPTION_REQUIRED);
        uomTypeDao.create(req, principal().getUserLoginId());
        return req.getUomTypeId();
    }

    public String updateUomType(String id, UomType req) {
        Validators.assertNotNull(req, Messages.UOM_TYPE_REQUIRED);
        Validators.assertNotBlank(req.getUomTypeId(), Messages.UOM_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(req.getDescription(), Messages.UOM_TYPE_DESCRIPTION_REQUIRED);
        UomType record = uomTypeDao.getUomType(id);
        Validators.assertNotNull(record, Messages.INVALID_UOM_TYPE);
        uomTypeDao.update(req, principal().getUserLoginId());
        return req.getUomTypeId();
    }

    public String deleteUomType(String id) {
        Validators.assertNotBlank(id, Messages.UOM_TYPE_ID_REQUIRED);
        UomType record = uomTypeDao.getUomType(id);
        Validators.assertNotNull(record, Messages.INVALID_UOM_TYPE);
        uomTypeDao.delete(id);
        return id;
    }
}
