package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.UomRatingScaleDao;
import it.mapsgroup.gzoom.mybatis.dto.UomRatingScale;
import it.mapsgroup.gzoom.mybatis.dto.UomRatingScaleEx;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Profile service.
 *
 */
@Service
public class UomRatingScaleService {
    private static final Logger LOG = getLogger(UomRatingScaleService.class);

    private final UomRatingScaleDao uomRatingScaleDao;

    @Autowired
    public UomRatingScaleService(UomRatingScaleDao uomRatingScaleDao) {
        this.uomRatingScaleDao = uomRatingScaleDao;
    }

    public Result<UomRatingScaleEx> getUomRatingScales(String uomId) {
        List<UomRatingScaleEx> list = uomRatingScaleDao.getUomRatingScalesEx(uomId);
        return new Result<>(list, list.size());
    }
    
    public UomRatingScaleEx getUomRatingScale(String uomId, BigDecimal value) {
        UomRatingScaleEx record = uomRatingScaleDao.getUomRatingScaleEx(uomId, value);
        return record;
    }

    public Result<UomRatingScale> getUomRatingScale(String uomId) {
        List<UomRatingScale> list = this.uomRatingScaleDao.getUomRatingScales(uomId);
        return new Result<>(list, list.size());
    }

    public Result<UomRatingScale> getByGlAccountId(String glAccountId) {
        List<UomRatingScale> list = this.uomRatingScaleDao.getByGlAccountId(glAccountId);
        return new Result<>(list, list.size());
    }

    public Result<UomRatingScale> getByGlAccountIdOnGlAccountMeasRatSc(String glAccountId) {
        List<UomRatingScale> list = this.uomRatingScaleDao.getByGlAccountIdOnGlAccountMeasRatSc(glAccountId);
        return new Result<>(list, list.size());
    }

    public Result<UomRatingScale> getUomRatingScalesExcludingGlAccount(String glAccountId) {
        List<UomRatingScale> list = this.uomRatingScaleDao.getUomRatingScalesExcludingGlAccount(glAccountId);
        return new Result<>(list, list.size());
    }

    public UomRatingScale createUomRatingScale(UomRatingScale req) {
        Validators.assertNotNull(req, Messages.UOM_RATING_SCALE_REQUIRED);
        Validators.assertNotBlank(req.getUomId(), Messages.UOM_ID_REQUIRED);
        Validators.assertNotNull(req.getUomRatingValue(), Messages.UOM_RATING_VALUE_REQUIRED);
        Validators.assertNotBlank(req.getDescription(), Messages.UOM_RATING_SCALE_DESCRIPTION_REQUIRED);
        uomRatingScaleDao.create(req, principal().getUserLoginId());
        return req;
    }

    public String updateUomRatingScale(UomRatingScale uomRatingScale) {
        Validators.assertNotNull(uomRatingScale, Messages.UOM_RATING_SCALE_REQUIRED);
        Validators.assertNotBlank(uomRatingScale.getUomId(), Messages.UOM_ID_REQUIRED);
        Validators.assertNotNull(uomRatingScale.getUomRatingValue(), Messages.UOM_RATING_VALUE_REQUIRED);
        Validators.assertNotBlank(uomRatingScale.getDescription(), Messages.UOM_DESCRIPTION_REQUIRED);
        UomRatingScale record = uomRatingScaleDao.findById(uomRatingScale.getUomId(), uomRatingScale.getUomRatingValue());
        Validators.assertNotNull(record, Messages.INVALID_UOM_RATING_SCALE);
        uomRatingScaleDao.update(uomRatingScale, principal().getUserLoginId());
        return uomRatingScale.getUomId();
    }

    public String deleteUomRatingScale(String id, Double value) {
        Validators.assertNotBlank(id, Messages.UOM_ID_REQUIRED);
        Validators.assertNotNull(value, Messages.UOM_RATING_VALUE_REQUIRED);
        UomRatingScale record = uomRatingScaleDao.findById(id, value);
        Validators.assertNotNull(record, Messages.INVALID_UOM_RATING_SCALE);
        uomRatingScaleDao.delete(id, value);
        return id;
    }

    public UomRatingScale getUomRatingScaleByPrimaryKey(String uomId, Double uomRatingValue) {
        return this.uomRatingScaleDao.getUomRatingScaleByPrimaryKey(uomId, uomRatingValue);
    }

    public Result<UomRatingScale> getAllUomRatingScale() {
        List<UomRatingScale> list = this.uomRatingScaleDao.getAllUomRatingScale();
        return new Result<>(list, list.size());
    }
}
