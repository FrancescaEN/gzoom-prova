package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.GlResourceType;
import it.mapsgroup.gzoom.mybatis.mapper.GlResourceTypeMapper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GlResourceTypeDao extends AbstractDao{
    private static final Logger LOG = getLogger(GlResourceTypeDao.class);

    private final GlResourceTypeMapper glResourceTypeMapper;

    public GlResourceTypeDao(GlResourceTypeMapper glResourceTypeMapper) {
        this.glResourceTypeMapper = glResourceTypeMapper;
    }

    /**
     * This function gets a glResourceType given its sequence name.
     *
     * @param glResourceTypeId data resource type id of the glResourceType
     * @return the corresponding record glResourceType
     */
    @Transactional
    public GlResourceType get(String glResourceTypeId) {
        LOG.info("find glResourceType by id");

        GlResourceType glResourceType = this.glResourceTypeMapper.selectByPrimaryKey(glResourceTypeId);
        LOG.info("GlResourceType = {}", (glResourceType != null));
        return glResourceType;
    }

    /**
     * Gets a list of glResourceType.
     *
     * @return
     */
    @Transactional
    public List<GlResourceType> getGlResourceTypeList() {
        LOG.info("find all glResourceType");

        List<GlResourceType> glResourceTypes = this.glResourceTypeMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", glResourceTypes.size());
        return glResourceTypes;
    }

    @Transactional
    public List<GlResourceType> getByGlAccountTypeId(String glAccountTypeId) {
        LOG.info("find glResourceType by glAccountTypeId");
        List<GlResourceType> glResourceTypes = this.glResourceTypeMapper.selectByGlAccountTypeId(glAccountTypeId);
        LOG.info("size = {}", glResourceTypes.size());
        return glResourceTypes;
    }

    /**
     * This function creates a new record glResourceType.
     *
     * @param glResourceType glResourceType to add
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(GlResourceType glResourceType, String userLoginId) {
        LOG.info("create glResourceType");
        setCreatedTimestamp(glResourceType);
        glResourceType.setCreatedByUserLogin(userLoginId);
        int result = this.glResourceTypeMapper.insert(glResourceType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of glResourceType.
     *
     * @param glResourceType glResourceType to update
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(GlResourceType glResourceType, String userLoginId) {
        LOG.info("update glResourceType");
        setUpdateTimestamp(glResourceType);
        glResourceType.setLastModifiedByUserLogin(userLoginId);
        int result = this.glResourceTypeMapper.updateByPrimaryKey(glResourceType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a glResourceType.
     *
     * @param id id of the glResourceType to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String id) {
        LOG.info("delete glResourceType");
        int result = this.glResourceTypeMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
