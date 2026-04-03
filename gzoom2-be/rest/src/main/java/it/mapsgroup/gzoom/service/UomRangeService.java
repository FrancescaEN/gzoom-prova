package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.UomRangeDao;
import it.mapsgroup.gzoom.mybatis.dao.UomRangeValuesDao;
import it.mapsgroup.gzoom.mybatis.dto.UomRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class UomRangeService {
    private final UomRangeDao uomRangeDao;
    private final UomRangeValuesDao uomRangeValuesDao;

    @Autowired
    public UomRangeService(UomRangeDao uomRangeDao, UomRangeValuesDao uomRangeValuesDao) {
        this.uomRangeDao = uomRangeDao;
        this.uomRangeValuesDao = uomRangeValuesDao;
    }

    public Result<UomRange> getUomRangeList() {
        List<UomRange> list = this.uomRangeDao.getUomRangeList();
        return new Result<>(list, list.size());
    }

    public boolean createUomRange(UomRange req) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.UOM_RANGE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomRangeId(), msg.getMessageColumn(Messages.UOM_RANGE, Messages.UOM_RANGE_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getDescription(), msg.getMessageColumn(Messages.UOM_RANGE, Messages.DESCRIPTION, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomId(), msg.getMessageColumn(Messages.UOM_RANGE, Messages.UOM_ID, Messages.IS_REQUIRED));

        UomRange record = uomRangeDao.get(req.getUomRangeId());
        Validators.assertTrue(record == null, msg.getMessageTable(Messages.UOM_RANGE, Messages.EXISTING));

        return uomRangeDao.create(req, principal().getUserLoginId());
    }

    public boolean updateUomRange(UomRange req) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.UOM_RANGE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomRangeId(), msg.getMessageColumn(Messages.UOM_RANGE, Messages.UOM_RANGE_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getDescription(), msg.getMessageColumn(Messages.UOM_RANGE, Messages.DESCRIPTION, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomId(), msg.getMessageColumn(Messages.UOM_RANGE, Messages.UOM_ID, Messages.IS_REQUIRED));

        UomRange record = uomRangeDao.get(req.getUomRangeId());
        Validators.assertFalse(record == null, msg.getMessageColumn(Messages.UOM_RANGE, Messages.UOM_RANGE_ID, Messages.INVALID));

        return uomRangeDao.update(req, principal().getUserLoginId());
    }

    public boolean deleteUomRange (String uomRangeId) {
        Messages msg = new Messages();
        Validators.assertNotBlank(uomRangeId, msg.getMessageColumn(Messages.UOM_RANGE, Messages.UOM_RANGE_ID, Messages.IS_REQUIRED));
        UomRange record = uomRangeDao.get(uomRangeId);
        Validators.assertFalse(record == null, msg.getMessageColumn(Messages.UOM_RANGE, Messages.UOM_RANGE_ID, Messages.INVALID));
        this.uomRangeValuesDao.deleteWithUomRangeId(uomRangeId);
        return uomRangeDao.delete(uomRangeId);
    }

}
