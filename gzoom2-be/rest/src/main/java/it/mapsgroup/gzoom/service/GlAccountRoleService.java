package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.GlAccountRoleDao;
import it.mapsgroup.gzoom.mybatis.dto.GlAccount;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountInputCalc;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountRole;
import it.mapsgroup.gzoom.mybatis.dto.UomEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class GlAccountRoleService {
    private final GlAccountRoleDao glAccountRoleDao;
    private final GlAccountService glAccountService;

    @Autowired
    public GlAccountRoleService(GlAccountRoleDao glAccountRoleDao, GlAccountService glAccountService) {
        this.glAccountRoleDao = glAccountRoleDao;
        this.glAccountService = glAccountService;
    }

    public boolean showWarningUODetected(String glAccountId) {
        GlAccount glAccount = this.glAccountService.getGlAccount(glAccountId);
        if (glAccount.getDetectOrgUnitIdFlag().equals("Y") && glAccount.getInputEnumId().equals("ACCINP_UO")) {
            List<GlAccountRole> glAccountRoleList = this.glAccountRoleDao.selectByGlAccountId(glAccountId);
            return glAccountRoleList.isEmpty();
        }
        return false;
    }

    public Result<GlAccountRole> getUoDetectedByGlAccountId(String glAccountId) {
        List<GlAccountRole> list = this.glAccountRoleDao.getUoDetectedByGlAccountId(glAccountId);
        return new Result<>(list, list.size());
    }

    @Transactional
    public boolean updateGlAccountRole(GlAccountRole[] glAccountRoles) {
        Messages msg = new Messages();
        for (int i = 0; i < glAccountRoles.length; i++) {
            Validators.assertNotBlank(glAccountRoles[i].getGlAccountId(), msg.getMessageColumn(Messages.GL_ACCOUNT_ROLE, Messages.GL_ACCOUNT_ID, Messages.IS_REQUIRED));
            Validators.assertNotBlank(glAccountRoles[i].getPartyId(), msg.getMessagesWithSpace(Messages.GL_ACCOUNT_ROLE, Messages.PARTY_ID_REQUIRED));
            Validators.assertNotBlank(glAccountRoles[i].getRoleTypeId(), msg.getMessagesWithSpace(Messages.GL_ACCOUNT_ROLE, Messages.ROLE_TYPE_ID_REQUIRED));
            Validators.assertNotNull(glAccountRoles[i].getFromDate(), msg.getMessageColumn(Messages.GL_ACCOUNT_ROLE, Messages.FROM_DATE, Messages.IS_REQUIRED));
            if (glAccountRoles[i].getThruDate() != null)
                Validators.assertIsBefore(glAccountRoles[i].getFromDate(), glAccountRoles[i].getThruDate(), msg.getMessagesWithSpace("From date is greater than thru date"));
            this.glAccountRoleDao.update(glAccountRoles[i], principal().getUserLoginId());
        }

        return true;
    }

    @Transactional
    public boolean createGlAccountRole(GlAccountRole[] glAccountRoles) {
        Messages msg = new Messages();
        for (int i = 0; i < glAccountRoles.length; i++) {
            Validators.assertNotBlank(glAccountRoles[i].getGlAccountId(), msg.getMessageColumn(Messages.GL_ACCOUNT_ROLE, Messages.GL_ACCOUNT_ID, Messages.IS_REQUIRED));
            Validators.assertNotBlank(glAccountRoles[i].getPartyId(), msg.getMessagesWithSpace(Messages.GL_ACCOUNT_ROLE, Messages.PARTY_ID_REQUIRED));
            Validators.assertNotBlank(glAccountRoles[i].getRoleTypeId(), msg.getMessagesWithSpace(Messages.GL_ACCOUNT_ROLE, Messages.ROLE_TYPE_ID_REQUIRED));
            Validators.assertNotNull(glAccountRoles[i].getFromDate(), msg.getMessageColumn(Messages.GL_ACCOUNT_ROLE, Messages.FROM_DATE, Messages.IS_REQUIRED));
            if (glAccountRoles[i].getThruDate() != null)
                Validators.assertIsBefore(glAccountRoles[i].getFromDate(), glAccountRoles[i].getThruDate(), msg.getMessagesWithSpace("From date is greater than thru date"));
            this.glAccountRoleDao.create(glAccountRoles[i], principal().getUserLoginId());
        }
        return true;
    }

    @Transactional
    public boolean deleteGlAccountRole(GlAccountRole[] glAccountRoles) {
        for (int i = 0; i < glAccountRoles.length; i++) {
           this.glAccountRoleDao.delete(glAccountRoles[i]);
        }
        return true;
    }
}
