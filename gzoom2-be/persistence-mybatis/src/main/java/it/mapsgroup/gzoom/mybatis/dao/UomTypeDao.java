package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.UomType;
import it.mapsgroup.gzoom.mybatis.mapper.UomTypeMapper;
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
public class UomTypeDao extends AbstractDao {
    private static final Logger LOG = getLogger(UomTypeDao.class);
    private final UomTypeMapper uomTypeMapper;

    @Autowired
    public UomTypeDao(UomTypeMapper uomTypeMapper) {
        this.uomTypeMapper = uomTypeMapper;
    }

    @Transactional
    public List<UomType> getUomTypes() {
        LOG.info("getUomTypes");

        List<UomType> uomTypeList = this.uomTypeMapper.selectAll();
        LOG.info("size = {}", uomTypeList.size());
        return uomTypeList;
    }

    @Transactional
    public UomType getUomType(String uomTypeId) {
        LOG.info("getUom");

        UomType uomType = this.uomTypeMapper.selectByPrimaryKey(uomTypeId);
        LOG.info("UomType = {}", (uomType != null));
        return uomType;
    }

    /**
     * Create record
     *
     * @param uomType
     * @param userLoginId
     * @return
     */
    @Transactional
    public boolean create(UomType uomType, String userLoginId) {
        LOG.info("create uomType");
        setCreatedTimestamp(uomType);
        uomType.setCreatedByUserLogin(userLoginId);
        int result = this.uomTypeMapper.insert(uomType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update record with uomId = id
     *
     * @param uomType
     * @param userLoginId
     * @return
     */
    @Transactional
    public boolean update(UomType uomType, String userLoginId) {
        LOG.info("update uomType");
        setUpdateTimestamp(uomType);
        uomType.setLastModifiedByUserLogin(userLoginId);
        int result = this.uomTypeMapper.updateByPrimaryKey(uomType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Delete record with uomId = id
     *
     * @param uomTypeId
     * @return
     */
    @Transactional
    public boolean delete(String uomTypeId) {
        LOG.info("delete uomType");
        int result = this.uomTypeMapper.deleteByPrimaryKey(uomTypeId);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
