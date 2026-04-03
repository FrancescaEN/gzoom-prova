package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountTypeGlFiscalType;
import it.mapsgroup.gzoom.mybatis.mapper.GlAccountTypeGlFiscalTypeMapper;
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
public class GlAccountTypeGlFiscalTypeDao extends AbstractDao {
    private static final Logger LOG = getLogger(GlAccountTypeGlFiscalTypeDao.class);
    private final GlAccountTypeGlFiscalTypeMapper glAccountTypeGlFiscalTypeMapper;

    @Autowired
    public GlAccountTypeGlFiscalTypeDao(GlAccountTypeGlFiscalTypeMapper glAccountTypeGlFiscalTypeMapper) {
        this.glAccountTypeGlFiscalTypeMapper = glAccountTypeGlFiscalTypeMapper;
    }

    @Transactional
    public List<GlAccountTypeGlFiscalType> getGlAccountTypeGlFiscalTypeList(String glAccountTypeId) {
        LOG.info("find glAccountTypeGlFiscalType by glAccountTypeId");

        List<GlAccountTypeGlFiscalType> glAccountTypeGlFiscalTypes = this.glAccountTypeGlFiscalTypeMapper.selectByGlAccountTypeId(glAccountTypeId);
        LOG.info("size = {}", glAccountTypeGlFiscalTypes.size());
        return glAccountTypeGlFiscalTypes;
    }

    /**
     * This function gets a GlAccountTypeGlFiscalType given its id.
     *
     * @param glAccountTypeId glAccountTypeId of the glAccountTypeGlFiscalType
     * @param glFiscalTypeId glFiscalTypeId of the glAccountTypeGlFiscalType
     * @return the corresponding GlAccountTypeGlFiscalType record
     */
    @Transactional
    public GlAccountTypeGlFiscalType get(String glAccountTypeId, String glFiscalTypeId) {
        LOG.info("find glAccountTypeGlFiscalType by id");

        GlAccountTypeGlFiscalType glAccountTypeGlFiscalType = this.glAccountTypeGlFiscalTypeMapper.selectByPrimaryKey(glAccountTypeId, glFiscalTypeId);
        LOG.info("GlAccountTypeGlFiscalType = {}", (glAccountTypeGlFiscalType != null));
        return glAccountTypeGlFiscalType;
    }

    /**
     * This function creates a new record glAccountTypeGlFiscalType.
     *
     * @param record glAccountTypeGlFiscalType to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(GlAccountTypeGlFiscalType record, String userLoginId) {
        LOG.info("create glAccountTypeGlFiscalType");
        setCreatedTimestamp(record);
        record.setCreatedByUserLogin(userLoginId);
        int result = this.glAccountTypeGlFiscalTypeMapper.insert(record);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of glAccountTypeGlFiscalType.
     *
     * @param record glAccountTypeGlFiscalType to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(GlAccountTypeGlFiscalType record, String userLoginId) {
        LOG.info("update glAccountTypeGlFiscalType");
        setUpdateTimestamp(record);
        record.setLastModifiedByUserLogin(userLoginId);
        int result = this.glAccountTypeGlFiscalTypeMapper.updateByPrimaryKey(record);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a glAccountTypeGlFiscalType.
     *
     * @param glAccountTypeId glAccountTypeId of the glAccountTypeGlFiscalType to be deleted
     * @param glFiscalTypeId glFiscalTypeId of the glAccountTypeGlFiscalType to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String glAccountTypeId, String glFiscalTypeId) {
        LOG.info("delete glAccountTypeGlFiscalType");
        int result = this.glAccountTypeGlFiscalTypeMapper.deleteByPrimaryKey(glAccountTypeId, glFiscalTypeId);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean deleteWithGlAccountTypeId(String glAccountTypeId) {
        LOG.info("delete glAccountTypeGlFiscalType");
        int result = this.glAccountTypeGlFiscalTypeMapper.deleteByGlAccountTypeId(glAccountTypeId);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
