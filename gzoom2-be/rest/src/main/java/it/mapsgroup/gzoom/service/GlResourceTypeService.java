package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.GlResourceTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.GlResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Leonardo Minaudo.
 */
@Service
public class GlResourceTypeService {
    private static final Logger LOG = getLogger(GlResourceTypeService.class);
    private final Configuration config;
    private final GlResourceTypeDao glResourceTypeDao;

    @Autowired
    public GlResourceTypeService(Configuration config, GlResourceTypeDao glResourceTypeDao) {
        this.config = config;
        this.glResourceTypeDao = glResourceTypeDao;
    }

    public Result<GlResourceType> getGlResourceType() {
        List<GlResourceType> list = this.glResourceTypeDao.getGlResourceTypeList();
        return new Result<>(list, list.size());
    }

    public Result<GlResourceType> getByGlAccountTypeId(String glAccountTypeId) {
        List<GlResourceType> list = this.glResourceTypeDao.getByGlAccountTypeId(glAccountTypeId);
        return new Result<>(list, list.size());
    }

    public boolean createGlResourceType(GlResourceType req) {
        Validators.assertNotNull(req, Messages.GL_RESOURCE_TYPE_REQUIRED);
        Validators.assertNotBlank(req.getGlResourceTypeId(), Messages.GL_RESOURCE_TYPE_ID_REQUIRED);
        GlResourceType record = glResourceTypeDao.get(req.getGlResourceTypeId());
        Validators.assertTrue(record == null, Messages.GL_RESOURCE_TYPE_ID_EXIST);
        Validators.assertNotBlank(req.getDescription(), Messages.GL_RESOURCE_TYPE_DESCRIPTION_REQUIRED);
        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getDescriptionLang(), Messages.GL_RESOURCE_TYPE_DESCRIPTION_LANG_REQUIRED);
        }
        return glResourceTypeDao.create(req, principal().getUserLoginId());
    }

    public boolean updateGlResourceType(GlResourceType req) {
        Validators.assertNotNull(req, Messages.GL_RESOURCE_TYPE_REQUIRED);
        Validators.assertNotBlank(req.getGlResourceTypeId(), Messages.GL_RESOURCE_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(req.getDescription(), Messages.GL_RESOURCE_TYPE_DESCRIPTION_REQUIRED);
        GlResourceType record = glResourceTypeDao.get(req.getGlResourceTypeId());
        Validators.assertNotNull(record, Messages.INVALID_GL_RESOURCE_TYPE);
        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getDescriptionLang(), Messages.GL_RESOURCE_TYPE_DESCRIPTION_LANG_REQUIRED);
        }
        return glResourceTypeDao.update(req, principal().getUserLoginId());
    }

    public boolean deleteGlResourceType(String id) {
        GlResourceType record = glResourceTypeDao.get(id);
        Validators.assertNotNull(record, Messages.INVALID_GL_RESOURCE_TYPE);
        return glResourceTypeDao.delete(id);
    }
}
