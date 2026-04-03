package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Permissions;
import it.mapsgroup.gzoom.mybatis.dao.SecurityPermissionDao;
import it.mapsgroup.gzoom.mybatis.dto.SecurityPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

import static it.mapsgroup.gzoom.security.Principals.principal;

/**
 * Profile service.
 *
 */
@Service
public class ProfileService {

    private final SecurityPermissionDao securityPermissionDao;
    private final Configuration config;

    @Autowired
    public ProfileService(SecurityPermissionDao securityPermissionDao,Configuration config) {
        this.securityPermissionDao = securityPermissionDao;
        this.config = config;
    }

    public Permissions getUserPermission() {
        List<SecurityPermission> listSecurityPermission = securityPermissionDao.getPermission(principal().getUserLoginId());
        Permissions permissions = new Permissions();
        
        String permRegExp = "(((?i)(MGR|ROLE|ORG|SUP|TOP)?)_)";
        Pattern permPattern = Pattern.compile(permRegExp);
        
        if(listSecurityPermission != null) {
            listSecurityPermission.forEach(r -> {
                String permissionId = r.getPermissionId();
                
                String perm = "";
                String[] permArray = permPattern.split(permissionId);
                for (int i = 0; i < permArray.length-1; i++) {
                    perm = perm.concat(permArray[i]);
                }
                permissions.addPermission(perm, permArray[permArray.length-1]);
            });
        }
        return permissions;
    }

    public String getOrganizationMultiType() {
        return config.getOrganizationMultiType();
    }
}
