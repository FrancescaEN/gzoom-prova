package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.UserCtxPermissionViewDao;
import it.mapsgroup.gzoom.mybatis.dto.TimesheetEx;
import it.mapsgroup.gzoom.mybatis.dto.UserCtxPermissionView;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import it.mapsgroup.gzoom.mybatis.util.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class UserCtxPermissionViewService {
    private final UserCtxPermissionViewDao userCtxPermissionViewDao;

    @Autowired
    public UserCtxPermissionViewService(UserCtxPermissionViewDao userCtxPermissionViewDao) {
        this.userCtxPermissionViewDao = userCtxPermissionViewDao;
    }

    public Result<UserCtxPermissionView> getUserCtxPermissions() {
        List<UserCtxPermissionView> list = this.userCtxPermissionViewDao.selectByUserLoginId(principal().getUserLoginId());
        return new Result<>(list, list.size());
    }

    public boolean isAdminByContext(String context) {
        return this.userCtxPermissionViewDao.hasPermission(principal().getUserLoginId(), ContextPermissionPrefixEnum.valueOf(context), Permission.ADMIN);
    }

}
