package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.SecurityPermissionDao;
import it.mapsgroup.gzoom.mybatis.dto.SecurityPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityPermissionService {
    private final SecurityPermissionDao securityPermissionDao;

    @Autowired
    public SecurityPermissionService(SecurityPermissionDao securityPermissionDao) {
        this.securityPermissionDao = securityPermissionDao;
    }

    public Result<SecurityPermission> findByEnabledOrderByPrimaryKey(String enabled) {
        List<SecurityPermission> list = this.securityPermissionDao.findByEnabledOrderByPrimaryKey(enabled);
        return new Result<>(list, list.size());
    }
}
