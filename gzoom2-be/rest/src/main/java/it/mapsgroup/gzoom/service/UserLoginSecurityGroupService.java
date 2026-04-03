package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginSecurityGroupDao;
import it.mapsgroup.gzoom.mybatis.dto.UserLoginSecurityGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class UserLoginSecurityGroupService {
    private final UserLoginSecurityGroupDao userLoginSecurityGroupDao;

    @Autowired
    public UserLoginSecurityGroupService(UserLoginSecurityGroupDao userLoginSecurityGroupDao) {
        this.userLoginSecurityGroupDao = userLoginSecurityGroupDao;
    }

    public Result<UserLoginSecurityGroup> getUserLoginSecurityGroupByGroupId(String groupId) {
        List<UserLoginSecurityGroup> list = this.userLoginSecurityGroupDao.getUserLoginSecurityGroupByGroupId(groupId);
        return new Result<>(list, list.size());
    }

    public boolean createUserLoginSecurityGroup(UserLoginSecurityGroup req) {
        this.validateUserLoginSecurityGroup(req, "CREATE");
        return this.userLoginSecurityGroupDao.create(req);
    }

    public boolean updateUserLoginSecurityGroup(UserLoginSecurityGroup req) {
        this.validateUserLoginSecurityGroup(req, "UPDATE");
        return this.userLoginSecurityGroupDao.update(req);
    }

    public boolean deleteUserLoginSecurityGroup (String userLoginId, String groupId, Instant fromDate) {
        UserLoginSecurityGroup userLoginSecurityGroup = new UserLoginSecurityGroup();
        userLoginSecurityGroup.setUserLoginId(userLoginId);
        userLoginSecurityGroup.setGroupId(groupId);
        userLoginSecurityGroup.setFromDate(fromDate);
        this.validateUserLoginSecurityGroup(userLoginSecurityGroup, "DELETE");
        return this.userLoginSecurityGroupDao.delete(userLoginId, groupId, fromDate);
    }

    private void validateUserLoginSecurityGroup(UserLoginSecurityGroup req, String method) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.USER_LOGIN_SECURITY_GROUP, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getUserLoginId(), msg.getMessagesWithSpace(Messages.USER_LOGIN_SECURITY_GROUP, Messages.USER_LOGIN_ID_REQUIRED));
        Validators.assertNotBlank(req.getGroupId(), msg.getMessageColumn(Messages.USER_LOGIN_SECURITY_GROUP, Messages.GROUP_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getFromDate(), msg.getMessageColumn(Messages.USER_LOGIN_SECURITY_GROUP, Messages.FROM_DATE, Messages.IS_REQUIRED));
        UserLoginSecurityGroup record = this.userLoginSecurityGroupDao.getUserLoginSecurityGroup(req.getUserLoginId(), req.getGroupId(), req.getFromDate());
        if ( method.equalsIgnoreCase("UPDATE") ||  method.equalsIgnoreCase("DELETE")) {
            Validators.assertFalse(record == null, msg.getMessageTable(Messages.USER_LOGIN_SECURITY_GROUP, Messages.INVALID));
        }
        if ( method.equalsIgnoreCase("CREATE")) {
            Validators.assertFalse(record != null, msg.getMessageTable(Messages.USER_LOGIN_SECURITY_GROUP, Messages.EXISTING));
        }

    }
}
