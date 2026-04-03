package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountType;
import it.mapsgroup.gzoom.mybatis.mapper.GlAccountTypeMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GlAccountTypeDao extends AbstractDao {
    private static final Logger LOG = getLogger(GlAccountType.class);
    private final GlAccountTypeMapper glAccountTypeMapper;

    @Autowired
    public GlAccountTypeDao(GlAccountTypeMapper glAccountTypeMapper) {
        this.glAccountTypeMapper = glAccountTypeMapper;
    }

    @Transactional
    public List<GlAccountType> getGlAccountTypeList () {
        LOG.info("find all glAccountType");

        List<GlAccountType> glAccountTypes = this.glAccountTypeMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", glAccountTypes.size());
        return glAccountTypes;
    }

    @Transactional
    public List<GlAccountType> getGlAccountTypeList (String accountTypeEnumId) {
        LOG.info("find glAccountType by accountTypeEnumId");

        List<GlAccountType> glAccountTypes = this.glAccountTypeMapper.selectByAccountTypeEnumIdOrderByPrimaryKey(accountTypeEnumId);
        LOG.info("size = {}", glAccountTypes.size());
        return glAccountTypes;
    }

    @Transactional
    public GlAccountType getGlAccountTypeId(String glAccountTypeId) {
        LOG.info("find glAccountType by glAccountTypeId");

        GlAccountType glAccountType  = this.glAccountTypeMapper.selectByPrimaryKey(glAccountTypeId);
        LOG.info("glAccountType = {}", glAccountType != null );
        return glAccountType;
    }

    @Transactional
    public List<GlAccountType> getGlAccountTypeList (String accountTypeEnumId, String isReservedAccount) {
        LOG.info("find glAccountType by accountTypeEnumId and isReservedAccount");

        List<GlAccountType> glAccountTypes = this.glAccountTypeMapper.selectByAccountTypeEnumIdAndIsReservedAccountOrdByPK(accountTypeEnumId, isReservedAccount);
        LOG.info("size = {}", glAccountTypes.size());
        return glAccountTypes;
    }
    /**
     * This function gets a GlAccountType given its id.
     *
     * @param id uom range id of the glAccountType
     * @return the corresponding GlAccountType record
     */
    @Transactional
    public GlAccountType get(String id) {
        LOG.info("find glAccountType by id");

        GlAccountType glAccountType = this.glAccountTypeMapper.selectByPrimaryKey(id);
        LOG.info("GlAccountType = {}", (glAccountType != null));
        return glAccountType;
    }

    /**
     * This function creates a new record glAccountType.
     *
     * @param glAccountType glAccountType to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(GlAccountType glAccountType, String userLoginId) {
        LOG.info("create glAccountType");
        setCreatedTimestamp(glAccountType);
        glAccountType.setCreatedByUserLogin(userLoginId);
        int result = this.glAccountTypeMapper.insert(glAccountType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of glAccountType.
     *
     * @param glAccountType glAccountType to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(GlAccountType glAccountType, String userLoginId) {
        LOG.info("update glAccountType");
        setUpdateTimestamp(glAccountType);
        glAccountType.setLastModifiedByUserLogin(userLoginId);
        int result = this.glAccountTypeMapper.updateByPrimaryKey(glAccountType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a glAccountType.
     *
     * @param id id of the glAccountType to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String id) {
        LOG.info("delete glAccountType");
        int result = this.glAccountTypeMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
