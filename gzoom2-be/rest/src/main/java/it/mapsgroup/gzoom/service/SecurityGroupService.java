package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.SecurityGroupContentDao;
import it.mapsgroup.gzoom.mybatis.dao.SecurityGroupDao;
import it.mapsgroup.gzoom.mybatis.dao.SecurityGroupPermissionDao;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginSecurityGroupDao;
import it.mapsgroup.gzoom.mybatis.dto.SecurityGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class SecurityGroupService {
    private final SecurityGroupDao securityGroupDao;
    private final SecurityGroupPermissionDao securityGroupPermissionDao;
    private final SecurityGroupContentDao securityGroupContentDao;
    private final UserLoginSecurityGroupDao userLoginSecurityGroupDao;


    @Autowired
    public SecurityGroupService(SecurityGroupDao securityGroupDao, SecurityGroupPermissionDao securityGroupPermissionDao, SecurityGroupContentDao securityGroupContentDao, UserLoginSecurityGroupDao userLoginSecurityGroupDao) {
        this.securityGroupDao = securityGroupDao;
        this.securityGroupPermissionDao = securityGroupPermissionDao;
        this.securityGroupContentDao = securityGroupContentDao;
        this.userLoginSecurityGroupDao = userLoginSecurityGroupDao;
    }

    public SecurityGroup getSecurityGroupById(String groupId) {
        SecurityGroup securityGroup = this.securityGroupDao.getSecurityGroupById(groupId);
        return securityGroup;
    }

    public Result<SecurityGroup> getSecurityGroupList() {
        List<SecurityGroup> list = this.securityGroupDao.getSecurityGroups();
        return new Result<>(list, list.size());
    }

    public boolean createSecurityGroup(SecurityGroup req) {
        this.validateSecurityGroup(req, "CREATE");
        return this.securityGroupDao.create(req, principal().getUserLoginId());
    }

    public boolean updateSecurityGroup(SecurityGroup req) {
        this.validateSecurityGroup(req, "UPDATE");
        return this.securityGroupDao.update(req, principal().getUserLoginId());
    }

    @Transactional
    public boolean deleteSecurityGroup (String groupId) {
        Messages msg = new Messages();
        Validators.assertNotBlank(groupId, msg.getMessageColumn(Messages.SECURITY_GROUP, Messages.GROUP_ID, Messages.IS_REQUIRED));
        SecurityGroup record = this.securityGroupDao.getSecurityGroup(groupId);
        Validators.assertFalse(record == null, msg.getMessageColumn(Messages.SECURITY_GROUP, Messages.GROUP_ID, Messages.INVALID));

        this.userLoginSecurityGroupDao.deleteByGroupId(groupId);
        this.securityGroupContentDao.deleteByGroupId(groupId);
        this.securityGroupPermissionDao.deleteByGroupId(groupId);
        return this.securityGroupDao.delete(groupId);
    }

    private void validateSecurityGroup(SecurityGroup req, String method) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.SECURITY_GROUP, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getGroupId(), msg.getMessageColumn(Messages.SECURITY_GROUP, Messages.GROUP_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getDescription(), msg.getMessageColumn(Messages.SECURITY_GROUP, Messages.DESCRIPTION, Messages.IS_REQUIRED));
        SecurityGroup record = this.securityGroupDao.getSecurityGroup(req.getGroupId());
        if ( method.equalsIgnoreCase("UPDATE")) {
            Validators.assertFalse(record == null, msg.getMessageColumn(Messages.STATUS_TYPE, Messages.GROUP_ID, Messages.INVALID));
        }
        if ( method.equalsIgnoreCase("CREATE")) {
            Validators.assertFalse(record != null, msg.getMessageColumn(Messages.STATUS_TYPE, Messages.GROUP_ID, Messages.EXISTING));
        }

    }
}
