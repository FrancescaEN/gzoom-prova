package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.GlAccountOrganizationDao;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountOrganization;
import it.mapsgroup.gzoom.mybatis.dto.StatusValidChange;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPurposeAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class GlAccountOrganizationService {
    private final GlAccountOrganizationDao glAccountOrganizationDao;

    @Autowired
    public GlAccountOrganizationService(GlAccountOrganizationDao glAccountOrganizationDao) {
        this.glAccountOrganizationDao = glAccountOrganizationDao;
    }

    public Result<GlAccountOrganization> getGlAccountOrganizationByGlAccountId(String glAccountId) {
        List<GlAccountOrganization> list = this.glAccountOrganizationDao.selectByGlAccountId(glAccountId);
        return new Result<>(list, list.size());
    }

    @Transactional
    public boolean createGlAccountOrganization(String glAccountId, String[] organizationPartyId, Instant fromDate, Instant thruDate) {
        Messages msg = new Messages();
        for (int i = 0; i < organizationPartyId.length ; i++) {
            GlAccountOrganization glAccountOrganization = new GlAccountOrganization();
            glAccountOrganization.setGlAccountId(glAccountId);
            glAccountOrganization.setOrganizationPartyId(organizationPartyId[i]);
            glAccountOrganization.setFromDate(fromDate);
            glAccountOrganization.setThruDate(thruDate);

            boolean result = this.glAccountOrganizationDao.create(glAccountOrganization, principal().getUserLoginId());
            Validators.assertTrue(result, msg.getMessageTable(Messages.GL_ACCOUNT_ORGANIZATION, "Cannot create record"));

        }
        return true;
    }
    @Transactional
    public boolean updateGlAccountOrganization(GlAccountOrganization[] req) {
        Messages msg = new Messages();
        for (int i = 0; i < req.length; i++) {
            Validators.assertNotBlank(req[i].getGlAccountId(), msg.getMessageColumn(Messages.GL_ACCOUNT_ORGANIZATION, Messages.GL_ACCOUNT_ID, Messages.IS_REQUIRED));
            Validators.assertNotBlank(req[i].getOrganizationPartyId(), msg.getMessageColumn(Messages.GL_ACCOUNT_ORGANIZATION, Messages.ORGANIZATION_PARTY_ID, Messages.IS_REQUIRED));
            this.glAccountOrganizationDao.update(req[i], principal().getUserLoginId());
        }

        return true;
    }

    @Transactional
    public boolean deleteGlAccountOrganization(String glAccountId, String[] organizationId) {
        Messages msg = new Messages();
        for (int i = 0; i < organizationId.length ; i++) {
            boolean result = this.glAccountOrganizationDao.delete(glAccountId, organizationId[i]);
            Validators.assertTrue(result, msg.getMessageTable(Messages.GL_ACCOUNT_ORGANIZATION, "Cannot delete records"));
        }
        return true;
    }
}
