package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountResource;
import it.mapsgroup.gzoom.mybatis.mapper.GlAccountResourceMapper;
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
public class GlAccountResourceDao extends AbstractDao {
    private static final Logger LOG = getLogger(GlAccountResourceDao.class);
    private final GlAccountResourceMapper glAccountResourceMapper;

    @Autowired
    public GlAccountResourceDao(GlAccountResourceMapper glAccountResourceMapper) {
        this.glAccountResourceMapper = glAccountResourceMapper;
    }


    @Transactional
    public List<GlAccountResource> getGlAccountResourceList(String glAccountTypeId) {
        LOG.info("find glAccountResource by glAccountTypeId");

        List<GlAccountResource> glAccountResources = this.glAccountResourceMapper.selectByGlAccountTypeIdOrderByGlResourceTypeId(glAccountTypeId);
        LOG.info("size = {}", glAccountResources.size());
        return glAccountResources;
    }

    /**
     * This function gets a GlAccountResource given its id.
     *
     * @param glAccountTypeId glAccountTypeId of the glAccountResource
     * @param glResourceTypeId glResourceTypeId of the glAccountResource
     * @return the corresponding GlAccountResource record
     */
    @Transactional
    public GlAccountResource get(String glAccountTypeId, String glResourceTypeId) {
        LOG.info("find glAccountResource by id");

        GlAccountResource glAccountResource = this.glAccountResourceMapper.selectByPrimaryKey(glAccountTypeId, glResourceTypeId);
        LOG.info("GlAccountResource = {}", (glAccountResource != null));
        return glAccountResource;
    }

    /**
     * This function creates a new record glAccountResource.
     *
     * @param glAccountResource glAccountResource to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(GlAccountResource glAccountResource, String userLoginId) {
        LOG.info("create glAccountResource");
        setCreatedTimestamp(glAccountResource);
        glAccountResource.setCreatedByUserLogin(userLoginId);
        int result = this.glAccountResourceMapper.insert(glAccountResource);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of glAccountResource.
     *
     * @param glAccountResource glAccountResource to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(GlAccountResource glAccountResource, String userLoginId) {
        LOG.info("update glAccountResource");
        setUpdateTimestamp(glAccountResource);
        glAccountResource.setLastModifiedByUserLogin(userLoginId);
        int result = this.glAccountResourceMapper.updateByPrimaryKey(glAccountResource);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a glAccountResource.
     *
     * @param glAccountTypeId glAccountTypeId of the glAccountResource to be deleted
     * @param glResourceTypeId glResourceTypeId of the glAccountResource to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String glAccountTypeId, String glResourceTypeId) {
        LOG.info("delete glAccountResource");
        int result = this.glAccountResourceMapper.deleteByPrimaryKey(glAccountTypeId, glResourceTypeId);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean deleteWithGlAccountTypeId(String glAccountTypeId) {
        LOG.info("delete glAccountResource");
        int result = this.glAccountResourceMapper.deleteByGlAccountTypeId(glAccountTypeId);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
