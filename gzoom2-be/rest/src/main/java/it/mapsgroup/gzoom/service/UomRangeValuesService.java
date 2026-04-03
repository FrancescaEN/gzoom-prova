package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.UomRangeValuesDao;
import it.mapsgroup.gzoom.mybatis.dto.UomRangeValues;
import it.mapsgroup.gzoom.mybatis.dto.UomRangeValuesExt;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UomRangeValuesService {

    private static final Logger LOG = getLogger(UomRangeValuesService.class);
    private final Configuration config;
    private final UomRangeValuesDao uomRangeValuesDao;

    @Autowired
    public UomRangeValuesService(Configuration config, UomRangeValuesDao uomRangeValuesDao) {
        this.config = config;
        this.uomRangeValuesDao = uomRangeValuesDao;
    }

    public Result<UomRangeValues> getUomRangeValues(String uomRangeId) {
        List<UomRangeValues> list = uomRangeValuesDao.getUomRangeValues(uomRangeId);
        return new Result<>(list, list.size());
    }

    public Result<UomRangeValues> getUomRangeValuesList(String uomRangeId) {
        List<UomRangeValues> list = uomRangeValuesDao.getUomRangeValuesList(uomRangeId);
        return new Result<>(list, list.size());
    }

    public Result<BigDecimal> getUomRangeValuesMax(String uomRangeId) {
        List<BigDecimal> list = uomRangeValuesDao.getUomRangeValuesMax(uomRangeId);
        return new Result<>(list, list.size());
    }

    public BigDecimal getUomRangeValuesMin(String uomRangeId){
        return uomRangeValuesDao.getUomRangeValuesMin(uomRangeId);
    }

    public Result<UomRangeValuesExt> getPathEmoticon(String rangeDefault, Float amount) {
        List<UomRangeValuesExt> list = uomRangeValuesDao.getPathEmoticon(rangeDefault, amount);
        return new Result<>(list, list.size());
    }

    public boolean createUomRangeValues(UomRangeValues req) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.UOM_RANGE_VALUES, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomRangeValuesId(), msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.UOM_RANGE_VALUES_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomRangeId(), msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.UOM_RANGE_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getComments(), msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.COMMENTS, Messages.IS_REQUIRED));
        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getCommentsLang(), msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.COMMENTS_LANG, Messages.IS_REQUIRED));
        }
        Validators.assertNotNull(req.getIsPositive(), msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.IS_POSITIVE, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getAlert(), msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.ALERT, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getProrateRange(), msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.PRORATE_RANGE, Messages.IS_REQUIRED));

        UomRangeValues record = uomRangeValuesDao.get(req.getUomRangeValuesId());
        Validators.assertTrue(record == null, msg.getMessageTable(Messages.UOM_RANGE_VALUES, Messages.EXISTING));

        return uomRangeValuesDao.create(req, principal().getUserLoginId());
    }

    public boolean updateUomRangeValues(UomRangeValues req) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.UOM_RANGE_VALUES, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomRangeValuesId(), msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.UOM_RANGE_VALUES_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUomRangeId(), msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.UOM_RANGE_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getComments(), msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.COMMENTS, Messages.IS_REQUIRED));
        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getCommentsLang(), msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.COMMENTS_LANG, Messages.IS_REQUIRED));
        }
        Validators.assertNotNull(req.getIsPositive(), msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.IS_POSITIVE, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getAlert(), msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.ALERT, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getProrateRange(), msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.PRORATE_RANGE, Messages.IS_REQUIRED));

        UomRangeValues record = uomRangeValuesDao.get(req.getUomRangeValuesId());

        Validators.assertFalse(record == null, msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.UOM_RANGE_VALUES_ID, Messages.INVALID));

        return uomRangeValuesDao.update(req, principal().getUserLoginId());
    }

    public boolean deleteUomRangeValues (String uomRangeValuesId) {
        Messages msg = new Messages();
        Validators.assertNotBlank(uomRangeValuesId, msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.UOM_RANGE_VALUES_ID, Messages.IS_REQUIRED));
        UomRangeValues record = uomRangeValuesDao.get(uomRangeValuesId);
        Validators.assertFalse(record == null, msg.getMessageColumn(Messages.UOM_RANGE_VALUES, Messages.UOM_RANGE_VALUES_ID, Messages.INVALID));

        return uomRangeValuesDao.delete(uomRangeValuesId);
    }
}
