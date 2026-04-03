package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.GlAccountResourceDao;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class GlAccountResourceService {
    private final GlAccountResourceDao glAccountResourceDao;

    @Autowired
    public GlAccountResourceService(GlAccountResourceDao glAccountResourceDao) {
        this.glAccountResourceDao = glAccountResourceDao;
    }

    public Result<GlAccountResource> getGlAccountResourceList(String uomRangeId) {
        List<GlAccountResource> list = glAccountResourceDao.getGlAccountResourceList(uomRangeId);
        return new Result<>(list, list.size());
    }

    public boolean createGlAccountResource(GlAccountResource req) {
        this.validateGlAccountResource(req, "CREATE");
        return glAccountResourceDao.create(req, principal().getUserLoginId());
    }

    public boolean updateGlAccountResource(GlAccountResource req) {
        this.validateGlAccountResource(req, "UPDATE");
        return glAccountResourceDao.update(req, principal().getUserLoginId());
    }

    public boolean deleteGlAccountResource (String glAccountTypeId, String glResourceTypeId) {
        Messages msg = new Messages();
        Validators.assertNotBlank(glAccountTypeId, msg.getMessageColumn(Messages.GL_ACCOUNT_RESOURCE, Messages.GL_ACCOUNT_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(glResourceTypeId, msg.getMessageColumn(Messages.GL_ACCOUNT_RESOURCE, Messages.GL_RESOURCE_TYPE_ID, Messages.IS_REQUIRED));
        GlAccountResource record = glAccountResourceDao.get(glAccountTypeId, glResourceTypeId);
        Validators.assertFalse(record == null, msg.getMessageColumn(Messages.GL_ACCOUNT_TYPE, Messages.GL_ACCOUNT_TYPE_ID, Messages.INVALID));

        return glAccountResourceDao.delete(glAccountTypeId, glResourceTypeId);
    }

    private void validateGlAccountResource(GlAccountResource req, String method) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.GL_ACCOUNT_RESOURCE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getGlAccountTypeId(), msg.getMessageColumn(Messages.GL_ACCOUNT_RESOURCE, Messages.GL_ACCOUNT_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getGlResourceTypeId(), msg.getMessageColumn(Messages.GL_ACCOUNT_RESOURCE, Messages.GL_RESOURCE_TYPE_ID, Messages.IS_REQUIRED));

        GlAccountResource record = glAccountResourceDao.get(req.getGlAccountTypeId(), req.getGlResourceTypeId());
        if ( method.equalsIgnoreCase("CREATE")) {
            Validators.assertTrue(record == null, msg.getMessageTable(Messages.GL_ACCOUNT_TYPE, Messages.EXISTING));
        }
        if ( method.equalsIgnoreCase("UPDATE")) {
            Validators.assertFalse(record == null, msg.getMessageColumn(Messages.GL_ACCOUNT_RESOURCE, Messages.GL_ACCOUNT_TYPE_ID + " and " + Messages.GL_RESOURCE_TYPE_ID, Messages.INVALID));
        }

    }
}
