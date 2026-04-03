package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.UomRangeValues;
import it.mapsgroup.gzoom.mybatis.dto.UomRangeValuesExt;
import it.mapsgroup.gzoom.mybatis.mapper.UomRangeValuesMapper;
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
public class UomRangeValuesDao extends AbstractDao{
    private static final Logger LOG = getLogger(UomRangeValuesDao.class);
    private final UomRangeValuesMapper uomRangeValuesMapper;

    @Autowired
    public UomRangeValuesDao(UomRangeValuesMapper uomRangeValuesMapper) {
        this.uomRangeValuesMapper = uomRangeValuesMapper;
    }

    @Transactional
    public List<UomRangeValues> getUomRangeValues(String uomRangeId) {
        LOG.info("getUomRangeValues");

        List<UomRangeValues> uomRangeValuesList = this.uomRangeValuesMapper.selectAllByUomRangeIdOrderByComments(uomRangeId);
        LOG.info("size = {}", uomRangeValuesList.size());
        return uomRangeValuesList;
    }

    @Transactional
    public List<UomRangeValues> getUomRangeValuesList(String uomRangeId) {
        LOG.info("getUomRangeValuesList");

        List<UomRangeValues> uomRangeValuesList = this.uomRangeValuesMapper.selectAllByUomRangeIdOrderByPK(uomRangeId);
        LOG.info("size = {}", uomRangeValuesList.size());
        return uomRangeValuesList;
    }

    /**
     * This function gets the maximum range value.
     *
     * @param uomRangeId rangeDefault from comments.
     * @return List of BigDecimal.
     */
    @Transactional
    public List<BigDecimal> getUomRangeValuesMax(String uomRangeId) {
        LOG.info("getUomRangeValuesMax");

        List<BigDecimal> bigDecimalList = this.uomRangeValuesMapper.getUomRangeValuesMax(uomRangeId);
        LOG.info("size = {}", bigDecimalList.size());
        return bigDecimalList;
    }

    /**
     * This function gets the minimum range value.
     *
     * @param uomRangeId rangeDefault from comments.
     * @return A BigDecimal.
     */
    @Transactional
    public BigDecimal getUomRangeValuesMin(String uomRangeId) {
        LOG.info("getUomRangeValuesMin");

        List<BigDecimal> bigDecimalList = this.uomRangeValuesMapper.getUomRangeValuesMin(uomRangeId);
        LOG.info("size = {}", bigDecimalList.size());
        return bigDecimalList.isEmpty()? null : bigDecimalList.get(0);
    }


    /**
     * This function gets the emoticon path based on amount.
     *
     * @param rangeDefault rangeDefault from comments
     * @param amount Amount value.
     * @return List of UomRangeValuesExt.
     */
    @Transactional
    public List<UomRangeValuesExt> getPathEmoticon(String rangeDefault, Float amount) {
        LOG.info("getPathEmoticon");

        List<UomRangeValuesExt> uomRangeValuesExts = this.uomRangeValuesMapper.getPathEmoticon(rangeDefault, amount);
        LOG.info("size = {}", uomRangeValuesExts.size());
        return uomRangeValuesExts;
    }

    /**
     * This function gets a UomRangeValues given its id.
     *
     * @param id uom range values id of the uomRangeValues
     * @return the corresponding UomRangeValues record
     */
    @Transactional
    public UomRangeValues get(String id) {
        LOG.info("find uomRangeValues by id");

        UomRangeValues uomRangeValues = this.uomRangeValuesMapper.selectByPrimaryKey(id);
        LOG.info("UomRangeValues = {}", (uomRangeValues != null));
        return uomRangeValues;
    }

    /**
     * This function creates a new record uomRangeValues.
     *
     * @param uomRangeValues uomRangeValues to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(UomRangeValues uomRangeValues, String userLoginId) {
        LOG.info("create uomRangeValues");
        setCreatedTimestamp(uomRangeValues);
        uomRangeValues.setCreatedByUserLogin(userLoginId);
        int result = this.uomRangeValuesMapper.insert(uomRangeValues);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of uomRangeValues.
     *
     * @param uomRangeValues uomRangeValues to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(UomRangeValues uomRangeValues, String userLoginId) {
        LOG.info("update uomRangeValues");
        setUpdateTimestamp(uomRangeValues);
        uomRangeValues.setLastModifiedByUserLogin(userLoginId);
        int result = this.uomRangeValuesMapper.updateByPrimaryKey(uomRangeValues);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a uomRangeValues.
     *
     * @param id id of the uomRangeValues to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String id) {
        LOG.info("delete uomRangeValues");
        int result = this.uomRangeValuesMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean deleteWithUomRangeId(String uomRangeId) {
        LOG.info("delete uomRangeValues by uomRangeId");
        int result = this.uomRangeValuesMapper.deleteByUomRangeId(uomRangeId);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
