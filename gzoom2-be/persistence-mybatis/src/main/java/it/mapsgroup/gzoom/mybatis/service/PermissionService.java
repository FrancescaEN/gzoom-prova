package it.mapsgroup.gzoom.mybatis.service;

import it.mapsgroup.gzoom.mybatis.dao.SecurityGroupPermissionDao;
import it.mapsgroup.gzoom.mybatis.dao.UserCtxPermissionViewDao;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginSecurityGroupDao;
import it.mapsgroup.gzoom.mybatis.dao.UserPreferenceDaoMB;
import it.mapsgroup.gzoom.mybatis.dto.SecurityGroupPermission;
import it.mapsgroup.gzoom.mybatis.dto.UserLoginSecurityGroup;
import it.mapsgroup.gzoom.mybatis.dto.UserPreference;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import it.mapsgroup.gzoom.mybatis.util.Permission;
import it.mapsgroup.gzoom.mybatis.util.PermissionResp;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    private static String SUP_ADMIN = "SUP_ADMIN";
    private static String TOP_ADMIN = "TOP_ADMIN";
    private static String ORG_ADMIN = "ORG_ADMIN";
    private static String ROLE_ADMIN = "ROLE_ADMIN";
    private static String MGR_ADMIN = "MGR_ADMIN";
    private static String FULLADMIN = "FULLADMIN";

    private static String BSCPERF = "BSCPERF";

    private UserLoginSecurityGroupDao userLoginSecurityGroupDao;
    private UserPreferenceDaoMB userPreferenceDao;
    private SecurityGroupPermissionDao securityGroupPermissionDao;
    private UserCtxPermissionViewDao userCtxPermissionViewDao;

    @Autowired
    public PermissionService(UserLoginSecurityGroupDao userLoginSecurityGroupDao, UserPreferenceDaoMB userPreferenceDao, SecurityGroupPermissionDao securityGroupPermissionDao, UserCtxPermissionViewDao userCtxPermissionViewDao) {
        this.userLoginSecurityGroupDao = userLoginSecurityGroupDao;
        this.userPreferenceDao = userPreferenceDao;
        this.securityGroupPermissionDao = securityGroupPermissionDao;
        this.userCtxPermissionViewDao = userCtxPermissionViewDao;
    }

    /**
     * Data il localDispatcherName, ritorno il nome del gruppo di sicurezza
     * @param localDispatcherName
     * @return
     */
    public String permissionLocalDispatcherName(String localDispatcherName) {
        String permission = localDispatcherName.toUpperCase();
        if ("STRATPERF".equals(permission)) {
            permission = BSCPERF;
        }

        return permission;
    }

    /**
     * Get default ORGANIZATION_PARTY by Username
     * @param user
     * @return company
     */
    public String userPrefereceOrganizationUnitId(String user) {
        String company = "Company";
        UserPreference userPreference = this.userPreferenceDao.getUserPreference(user, "ORGANIZATION_PARTY");
        if(userPreference!=null) company = userPreference.getUserPrefValue();
        return company;
    }

    /**
     * Get default SECURITY GROUP by userLoginId AND groupId
     * @param userLoginId
     * @param groupId
     * @return company
     */
    public boolean hasSecurityGroup(String userLoginId, String groupId) {
        List<UserLoginSecurityGroup> list = userLoginSecurityGroupDao.getUserLoginSecurityGroups(userLoginId, groupId);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Get default PERMISSION by permissionId and userLoginId
     * permissionId
     * @param userLoginId
     * @return company
     */
    public boolean hasPermission(String permissionId, String userLoginId) {
        List<UserLoginSecurityGroup> list = userLoginSecurityGroupDao.getUserLoginSecurityGroups(userLoginId);
        for(UserLoginSecurityGroup ele: list) {
            SecurityGroupPermission securityGroupPermission = securityGroupPermissionDao.getSecurityGroupPermissions(ele.getGroupId(), permissionId);
            if (securityGroupPermission != null) return true;
        }
        return false;
    }

    public boolean isOrgMgr(String userLoginId, ContextPermissionPrefixEnum context) {
        return this.userCtxPermissionViewDao.hasPermissionResp(userLoginId, context, PermissionResp.IS_ORG);
    }

    public boolean isRole(String userLoginId, ContextPermissionPrefixEnum context) {
        return this.userCtxPermissionViewDao.hasPermissionResp(userLoginId, context, PermissionResp.IS_ROLE);
    }

    public boolean isSup(String userLoginId, ContextPermissionPrefixEnum context) {
        return this.userCtxPermissionViewDao.hasPermissionResp(userLoginId, context, PermissionResp.IS_SUP);
    }

    public boolean isTop(String userLoginId, ContextPermissionPrefixEnum context) {
        return this.userCtxPermissionViewDao.hasPermissionResp(userLoginId, context, PermissionResp.IS_TOP);
    }

    public boolean isFullAdmin(String userLoginId, ContextPermissionPrefixEnum context ) {
        return this.userCtxPermissionViewDao.hasPermission(userLoginId, context, Permission.ADMIN);
    }

    public boolean isResp(String userLoginId, ContextPermissionPrefixEnum context ) {
        return this.userCtxPermissionViewDao.hasPermission(userLoginId, context, Permission.RESP);
    }

    public boolean isView(String userLoginId, ContextPermissionPrefixEnum context ) {
        return this.userCtxPermissionViewDao.hasPermission(userLoginId, context, Permission.VIEW);
    }
}
