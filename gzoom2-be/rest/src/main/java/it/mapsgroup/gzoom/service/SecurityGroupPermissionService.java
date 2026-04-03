package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.SecurityGroupPermissionDao;
import it.mapsgroup.gzoom.mybatis.dto.SecurityGroupPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SecurityGroupPermissionService {
    private final SecurityGroupPermissionDao securityGroupPermissionDao;

    @Autowired
    public SecurityGroupPermissionService(SecurityGroupPermissionDao securityGroupPermissionDao) {
        this.securityGroupPermissionDao = securityGroupPermissionDao;
    }

    public Result<SecurityGroupPermission> getSecurityGroupPermissionList(String groupId) {
        List<SecurityGroupPermission> list = this.securityGroupPermissionDao.getSecurityGroupPermissions(groupId);
        return new Result<>(list, list.size());
    }

    public boolean createSecurityGroupPermission(SecurityGroupPermission req) {
        this.validateSecurityGroupPermission(req, "CREATE");
        return this.securityGroupPermissionDao.create(req);
    }

    public boolean updateSecurityGroupPermission(SecurityGroupPermission req) {
        this.validateSecurityGroupPermission(req, "UPDATE");
        return this.securityGroupPermissionDao.update(req);
    }

    public boolean deleteSecurityGroupPermission (String groupId, String permissionId) {
        SecurityGroupPermission securityGroupPermission = new SecurityGroupPermission();
        securityGroupPermission.setGroupId(groupId);
        securityGroupPermission.setPermissionId(permissionId);
        this.validateSecurityGroupPermission(securityGroupPermission, "DELETE");
        return this.securityGroupPermissionDao.delete(groupId, permissionId);
    }

    private void validateSecurityGroupPermission(SecurityGroupPermission req, String method) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.SECURITY_GROUP_PERMISSION, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getGroupId(), msg.getMessageColumn(Messages.SECURITY_GROUP_PERMISSION, Messages.GROUP_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getPermissionId(), msg.getMessageColumn(Messages.SECURITY_GROUP_PERMISSION, Messages.PERMISSION_ID, Messages.IS_REQUIRED));
        SecurityGroupPermission record = this.securityGroupPermissionDao.getSecurityGroupPermissions(req.getGroupId(), req.getPermissionId());
        if ( method.equalsIgnoreCase("UPDATE") ||  method.equalsIgnoreCase("DELETE")) {
            Validators.assertFalse(record == null, msg.getMessageTable(Messages.SECURITY_GROUP_PERMISSION, Messages.INVALID));
        }
        if ( method.equalsIgnoreCase("CREATE")) {
            Validators.assertFalse(record != null, msg.getMessageTable(Messages.SECURITY_GROUP_PERMISSION, Messages.EXISTING));
        }

    }
}
