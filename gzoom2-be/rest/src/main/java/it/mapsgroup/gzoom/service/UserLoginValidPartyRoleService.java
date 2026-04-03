package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginValidPartyRoleDao;
import it.mapsgroup.gzoom.mybatis.dto.UserLoginValidPartyRoleEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLoginValidPartyRoleService {

    private final UserLoginValidPartyRoleDao userLoginValidPartyRoleDao;

    @Autowired
    public UserLoginValidPartyRoleService(UserLoginValidPartyRoleDao userLoginValidPartyRoleDao) {
        this.userLoginValidPartyRoleDao=userLoginValidPartyRoleDao;
    }

    public Result<UserLoginValidPartyRoleEx> getUserLoginValidPartyRoleList(String user) {
        List<UserLoginValidPartyRoleEx> list = userLoginValidPartyRoleDao.getUserLoginValidPartyRoleList(user);
        return new Result<>(list, list.size());
    }
}
