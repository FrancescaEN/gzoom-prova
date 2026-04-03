package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.UomRange;
import it.mapsgroup.gzoom.mybatis.mapper.UomRangeMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UomRangeDao extends AbstractDao {
    private static final Logger LOG = getLogger(UomRangeDao.class);
    private final UomRangeMapper uomRangeMapper;

    @Autowired
    public UomRangeDao(UomRangeMapper uomRangeMapper) {
        this.uomRangeMapper = uomRangeMapper;
    }

    @Transactional
    public List<UomRange> getUomRangeList () {
        LOG.info("find all uomRange");

        List<UomRange> uomRanges = this.uomRangeMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", uomRanges.size());
        return uomRanges;
    }

    /**
     * This function gets a UomRange given its id.
     *
     * @param id uom range id of the uomRange
     * @return the corresponding UomRange uomRange
     */
    @Transactional
    public UomRange get(String id) {
        LOG.info("find uomRange by id");

        UomRange uomRange = this.uomRangeMapper.selectByPrimaryKey(id);
        LOG.info("UomRange = {}", (uomRange != null));
        return uomRange;
    }

    /**
     * This function creates a new uomRange uomRange.
     *
     * @param uomRange uomRange to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(UomRange uomRange, String userLoginId) {
        LOG.info("create uomRange");
        setCreatedTimestamp(uomRange);
        uomRange.setCreatedByUserLogin(userLoginId);
        int result = this.uomRangeMapper.insert(uomRange);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of uomRange.
     *
     * @param uomRange uomRange to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(UomRange uomRange, String userLoginId) {
        LOG.info("update uomRange");
        setUpdateTimestamp(uomRange);
        uomRange.setLastModifiedByUserLogin(userLoginId);
        int result = this.uomRangeMapper.updateByPrimaryKey(uomRange);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a uomRange.
     *
     * @param id id of the uomRange to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String id) {
        LOG.info("delete uomRange");
        int result = this.uomRangeMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
