package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.UomRatingScale;
import it.mapsgroup.gzoom.mybatis.dto.UomRatingScaleEx;
import it.mapsgroup.gzoom.mybatis.mapper.UomRatingScaleMapper;
import org.apache.ibatis.annotations.Result;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UomRatingScaleDao extends AbstractDao{
    private static final Logger LOG = getLogger(UomRatingScaleDao.class);
    private final UomRatingScaleMapper uomRatingScaleMapper;

    @Autowired
    public UomRatingScaleDao(UomRatingScaleMapper uomRatingScaleMapper) {
        this.uomRatingScaleMapper = uomRatingScaleMapper;
    }

    /**
     * This function gets a UomRatingScale given its id.
     *
     * @param uomId uom id of the uom
     * @return the corresponding UomRange uomRange
     */
    @Transactional
    public List<UomRatingScale> getUomRatingScales(String uomId) {
        LOG.info("getUomRatingScales");

        List<UomRatingScale> uomRatingScaleList = this.uomRatingScaleMapper.getUomRatingScales(uomId);
        LOG.info("size = {}", uomRatingScaleList.size());
        return uomRatingScaleList;
    }

    /**
     * This function gets a UomRatingScale given its id.
     *
     * @param uomId uom id of the uom
     * @param uomRatingValue uomRatingValue of the uom
     * @return the corresponding UomRange uomRange
     */
    @Transactional
    public UomRatingScaleEx getUomRatingScaleEx(String uomId, BigDecimal uomRatingValue) {
        LOG.info("getUomRatingScaleEx");

        UomRatingScaleEx uomRatingScaleEx = this.uomRatingScaleMapper.getUomRatingScaleEx(uomId, uomRatingValue);
        LOG.info("id = {}", uomRatingScaleEx.getUomId());
        return uomRatingScaleEx;
    }

    /**
     * This function gets a UomRatingScale given its id.
     *
     * @param uomId uom id of the uom
     * @return the corresponding UomRange uomRange
     */
    @Transactional
    public List<UomRatingScaleEx> getUomRatingScalesEx(String uomId) {
        LOG.info("getUomRatingScalesEx");

        List<UomRatingScaleEx> uomRatingScaleList = this.uomRatingScaleMapper.getUomRatingScalesEx(uomId);
        LOG.info("size = {}", uomRatingScaleList.size());
        return uomRatingScaleList;
    }

    /**
     * This function gets a UomRatingScale given its id.
     *
     * @param uomId uom id of the uom
     * @param uomRatingValue uomRatingValue of the uom
     * @return the corresponding UomRange uomRange
     */
    @Transactional
    public UomRatingScale findById(String uomId, Double uomRatingValue) {
        LOG.info("getUomRatingScale");

        UomRatingScale uomRatingScale = this.uomRatingScaleMapper.selectByPrimaryKey(uomId, uomRatingValue);
        LOG.info("id = {}", uomRatingScale.getUomId());
        return uomRatingScale;
    }

    /**
     * This function creates a new record uomRatingScale.
     *
     * @param uomRatingScale uomRangeValues to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(UomRatingScale uomRatingScale, String userLoginId) {
        LOG.info("create uomRatingScale");
        setCreatedTimestamp(uomRatingScale);
        uomRatingScale.setCreatedByUserLogin(userLoginId);
        int result = this.uomRatingScaleMapper.insert(uomRatingScale);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of uomRatingScale.
     *
     * @param uomRatingScale uomRangeValues to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(UomRatingScale uomRatingScale, String userLoginId) {
        LOG.info("update uomRatingScale");
        setUpdateTimestamp(uomRatingScale);
        uomRatingScale.setLastModifiedByUserLogin(userLoginId);
        int result = this.uomRatingScaleMapper.updateByPrimaryKey(uomRatingScale);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a uomRatingScale.
     *
     * @param id id of the uomRangeValues to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String id,  Double uomRatingValue) {
        LOG.info("delete uomRatingScale");
        int result = this.uomRatingScaleMapper.deleteByPrimaryKey(id, uomRatingValue);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public List<UomRatingScale> getByGlAccountId(String glAccountId) {
        LOG.info("getByGlAccountId");
        List<UomRatingScale> uomRatingScaleList = this.uomRatingScaleMapper.selectByGlAccountId(glAccountId);
        LOG.info("size = {}", uomRatingScaleList.size());
        return uomRatingScaleList;
    }

    @Transactional
    public List<UomRatingScale> getByGlAccountIdOnGlAccountMeasRatSc(String glAccountId) {
        LOG.info("getByGlAccountIdOnGlAccountMeasRatSc");
        List<UomRatingScale> uomRatingScaleList = this.uomRatingScaleMapper.selectByGlAccountIdOnGlAccountMeasRatSc(glAccountId);
        LOG.info("size = {}", uomRatingScaleList.size());
        return uomRatingScaleList;
    }

    @Transactional
    public List<UomRatingScale> getUomRatingScalesExcludingGlAccount(String glAccountId) {
        LOG.info("getUomRatingScalesExcludingGlAccount");
        List<UomRatingScale> uomRatingScaleList = this.uomRatingScaleMapper.getUomRatingScalesExcludingGlAccount(glAccountId);
        LOG.info("size = {}", uomRatingScaleList.size());
        return uomRatingScaleList;
    }

    @Transactional
    public UomRatingScale getUomRatingScaleByPrimaryKey(String uomId, Double uomRatingValue) {
        LOG.info("getUomRatingScaleByUomIdAndUomRatingValue");
        UomRatingScale uomRatingScale = this.uomRatingScaleMapper.selectByPrimaryKey(uomId, uomRatingValue);
        LOG.info("uomRatingScale = {}", uomRatingScale != null);
        return uomRatingScale;
    }

    public List<UomRatingScale> getAllUomRatingScale() {
        LOG.info("getAllUomRatingScale");
        List<UomRatingScale> uomRatingScaleList = this.uomRatingScaleMapper.selectAll();
        LOG.info("size = {}", uomRatingScaleList.size());
        return uomRatingScaleList;
    }
}
