package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.Uom;
import it.mapsgroup.gzoom.mybatis.dto.UomEx;
import it.mapsgroup.gzoom.mybatis.mapper.UomMapper;
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
public class UomDao extends AbstractDao {
    private static final Logger LOG = getLogger(UomDao.class);
    private final UomMapper uomMapper;

    @Autowired
    public UomDao(UomMapper uomMapper) {
        this.uomMapper = uomMapper;
    }

    @Transactional
    public List<UomEx> getUoms() {
        LOG.info("getUoms");

        List<UomEx> uoms = this.uomMapper.getUoms();
        LOG.info("size = {}", uoms.size());
        return uoms;
    }

    @Transactional
    public List<Uom> selectAll() {
        LOG.info("selectAllUom");
        List<Uom> uoms = this.uomMapper.selectAll();
        LOG.info("size = {}", uoms.size());
        return uoms;
    }

    @Transactional
    public UomEx getUom(String uomId) {
        LOG.info("getUom");

        UomEx uom = this.uomMapper.getUom(uomId);
        LOG.info("UomEx = {}", (uom != null));
        return uom;
    }

    @Transactional
    public Uom selectByPrimaryKey(String uomId) {
        LOG.info("selectByPrimaryKey");

        Uom uom = this.uomMapper.selectByPrimaryKey(uomId);
        LOG.info("Uom = {}", (uom != null));
        return uom;
    }

    @Transactional
    public Uom selectByGlAccountId(String glAccountId) {
        LOG.info("selectByGlAccountId");
        Uom uom = this.uomMapper.selectByGlAccountId(glAccountId);
        LOG.info("Uom = {}", (uom != null));
        return uom;
    }

    @Transactional
    public boolean create(Uom uom, String userLoginId) {
        LOG.info("create uom");
        setCreatedTimestamp(uom);
        uom.setCreatedByUserLogin(userLoginId);
        int result = this.uomMapper.insert(uom);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update record with uomId = id
     *
     * @param record
     * @param userLoginId
     * @return
     */
    @Transactional
    public boolean update(Uom record, String userLoginId) {
        LOG.info("update uom");
        setUpdateTimestamp(record);
        record.setLastModifiedByUserLogin(userLoginId);
        int result = this.uomMapper.updateByPrimaryKey(record);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Delete record with uomId = id
     *
     * @param id
     * @return
     */
    @Transactional
    public boolean delete(String id) {
        LOG.info("delete uom");
        int result = this.uomMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
