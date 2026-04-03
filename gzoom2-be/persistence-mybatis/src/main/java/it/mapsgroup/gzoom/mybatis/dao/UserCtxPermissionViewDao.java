package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.UserCtxPermissionView;
import it.mapsgroup.gzoom.mybatis.mapper.UserCtxPermissionViewMapper;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import it.mapsgroup.gzoom.mybatis.util.Permission;
import it.mapsgroup.gzoom.mybatis.util.PermissionResp;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UserCtxPermissionViewDao {
    private static final Logger LOG = getLogger(UserCtxPermissionViewDao.class);
    private final UserCtxPermissionViewMapper userCtxPermissionViewMapper;

    @Autowired
    public UserCtxPermissionViewDao(UserCtxPermissionViewMapper userCtxPermissionViewMapper) {
        this.userCtxPermissionViewMapper = userCtxPermissionViewMapper;
    }

    @Transactional
    public List<UserCtxPermissionView> selectByUserLoginId (String userLoginId) {
        LOG.info("get UserCtxPermissionView by userLoginId");
        List<UserCtxPermissionView> userCtxPermissionViewList = this.userCtxPermissionViewMapper.selectByUserLoginId(userLoginId);
        LOG.info("size = {}", userCtxPermissionViewList.size());
        return userCtxPermissionViewList;
    }

    @Transactional
    public boolean hasPermission (String userLoginId, ContextPermissionPrefixEnum context, Permission ctxPermission) {
        LOG.info("userLoginId has permission");
        int results = this.userCtxPermissionViewMapper.hasPermission(userLoginId, context.getCode(), ctxPermission.getCode());
        LOG.info("size = {}", results);
        return results > 0;
    }

    @Transactional
    public boolean hasPermissionResp (String userLoginId, ContextPermissionPrefixEnum context, PermissionResp permissionResp) {
        LOG.info("userLoginId has resp permission");
        int results = this.userCtxPermissionViewMapper.hasPermissionResp(userLoginId, context.getCode(), permissionResp.getCode());
        LOG.info("size = {}", results);
        return results > 0;
    }
}
