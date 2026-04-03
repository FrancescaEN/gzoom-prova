package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.GlFiscalType;
import it.mapsgroup.gzoom.mybatis.mapper.GlFiscalTypeMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GlFiscalTypeDao extends AbstractDao {
    private static final Logger LOG = getLogger(GlFiscalTypeDao.class);

    private final GlFiscalTypeMapper glFiscalTypeMapper;
    @Autowired
    public GlFiscalTypeDao(GlFiscalTypeMapper glFiscalTypeMapper) {
        this.glFiscalTypeMapper = glFiscalTypeMapper;
    }

    /**
     * This function gets a glFiscalType given its sequence name.
     *
     * @param id sequence name of the glFiscalType
     * @return the corresponding glFiscalType glFiscalType
     */
    @Transactional
    public GlFiscalType get(String id) {
        LOG.info("find glFiscalType by id");

        GlFiscalType glFiscalType = this.glFiscalTypeMapper.selectByPrimaryKey(id);
        LOG.info("GlFiscalType = {}", (glFiscalType != null));
        return glFiscalType;
    }
    

    /**
     * Gets a list of glFiscalType.
     *
     * @return
     */
    @Transactional
    public List<GlFiscalType> getGlFiscalTypeList() {
        LOG.info("find all glFiscalType");

        List<GlFiscalType> glFiscalTypes = this.glFiscalTypeMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", glFiscalTypes.size());
        return glFiscalTypes;
    }


    @Transactional
    public List<GlFiscalType> getGlFiscalTypeForNewIndicatorMovement( String accountTypeEnumId, String glAccountId, String customTimePeriodId, String roleTypeId, String partyId, String voucherRef) {
        LOG.info("getGlFiscalTypeForNewIndicatorMovement");
        List<GlFiscalType> glFiscalTypes = this.glFiscalTypeMapper.getGlFiscalTypeForNewIndicatorMovement(
                "null".equals(accountTypeEnumId) ? null : accountTypeEnumId,
                "null".equals(glAccountId) ? null : glAccountId,
                "null".equals(customTimePeriodId) ? null : customTimePeriodId,
                "null".equals(roleTypeId) ? null : roleTypeId,
                "null".equals(partyId) ? null : partyId,
                "null".equals(voucherRef) ? null : voucherRef
        );
        LOG.info("size = {}", glFiscalTypes.size());
        return glFiscalTypes;
    }

    @Transactional
    public List<GlFiscalType> getGlFiscalTypeByIsIndicatorUsed(String isIndicatorUsed) {
        LOG.info("getGlFiscalTypeByIsIndicatorUsed");
        List<GlFiscalType> glFiscalTypes = this.glFiscalTypeMapper.getGlFiscalTypeByIsIndicatorUsed(isIndicatorUsed);
        LOG.info("size = {}", glFiscalTypes.size());
        return glFiscalTypes;
    }

    @Transactional
    public List<GlFiscalType> getGlFiscalTypeByIsFinancialUsed(String isFinancialUsed) {
        LOG.info("getGlFiscalTypeByIsFinancialUsed");
        List<GlFiscalType> glFiscalTypes = this.glFiscalTypeMapper.getGlFiscalTypeByIsFinancialUsed(isFinancialUsed);
        LOG.info("size = {}", glFiscalTypes.size());
        return glFiscalTypes;
    }

    @Transactional
    public List<GlFiscalType> getGlFiscalTypeByIsAccountUsed(String isAccountUsed) {
        LOG.info("getGlFiscalTypeByIsAccountUsed");
        List<GlFiscalType> glFiscalTypes = this.glFiscalTypeMapper.getGlFiscalTypeByIsAccountUsed(isAccountUsed);
        LOG.info("size = {}", glFiscalTypes.size());
        return glFiscalTypes;
    }

    /**
     * This function creates a new record glFiscalType.
     *
     * @param glFiscalType glFiscalType to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(GlFiscalType glFiscalType, String userLoginId) {
        LOG.info("create glFiscalType");
        setCreatedTimestamp(glFiscalType);
        setCreatedByUserLogin(glFiscalType, userLoginId);
        int result = this.glFiscalTypeMapper.insert(glFiscalType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of glFiscalType.
     *
     * @param glFiscalType glFiscalType to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(GlFiscalType glFiscalType, String userLoginId) {
        LOG.info("update glFiscalType");
        setUpdateTimestamp(glFiscalType);
        setLastModifiedByUserLogin(glFiscalType, userLoginId);
        int result = this.glFiscalTypeMapper.updateByPrimaryKey(glFiscalType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a glFiscalType.
     *
     * @param id id of the glFiscalType to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String id) {
        LOG.info("delete glFiscalType");
        int result = this.glFiscalTypeMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

    private void setCreatedByUserLogin(GlFiscalType glFiscalType, String userLoginId) {
        glFiscalType.setCreatedByUserLogin(userLoginId);
    }

    private void setLastModifiedByUserLogin(GlFiscalType glFiscalType, String userLoginId) {
        glFiscalType.setLastModifiedByUserLogin(userLoginId);
    }
}
