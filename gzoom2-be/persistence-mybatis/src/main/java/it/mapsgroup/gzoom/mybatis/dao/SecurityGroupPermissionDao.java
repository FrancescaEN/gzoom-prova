package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.SecurityGroupPermission;
import it.mapsgroup.gzoom.mybatis.mapper.SecurityGroupPermissionMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class SecurityGroupPermissionDao extends AbstractDao {
    private static final Logger LOG = getLogger(SecurityGroupPermissionDao.class);
    private final SecurityGroupPermissionMapper securityGroupPermissionMapper;

    @Autowired
    public SecurityGroupPermissionDao(SecurityGroupPermissionMapper securityGroupPermissionMapper) {
        this.securityGroupPermissionMapper = securityGroupPermissionMapper;
    }

    /**
     * This function gets the Security Group Permission.
     * @param groupId
     * @param permissionId
     * @return Record of SecurityGroupPermission.
     */
    @Transactional
    public SecurityGroupPermission getSecurityGroupPermissions(String groupId, String permissionId){
        LOG.info("getSecurityGroupPermissions");

        SecurityGroupPermission securityGroupPermission = this.securityGroupPermissionMapper.selectByPrimaryKey(groupId, permissionId);
        LOG.info("SecurityGroupPermission = {}", (securityGroupPermission != null));
        return securityGroupPermission;
    }

    @Transactional
    public boolean deleteByGroupId(String groupId) {
        LOG.info("delete securityGroupPermission");
        int result = this.securityGroupPermissionMapper.deleteByGroupId(groupId);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public List<SecurityGroupPermission> getSecurityGroupPermissions(String groupId) {
        LOG.info("find all securityGroupPermission");

        List<SecurityGroupPermission> securityGroupPermissions = this.securityGroupPermissionMapper.selectByGroupIdOrderByPrimaryKey(groupId);
        LOG.info("size = {}", securityGroupPermissions.size());
        return securityGroupPermissions;
    }

    @Transactional
    public boolean update(SecurityGroupPermission securityGroupPermission) {
        LOG.info("update securityGroupPermission");
        setUpdateTimestamp(securityGroupPermission);
        int result = this.securityGroupPermissionMapper.updateByPrimaryKey(securityGroupPermission);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String groupId, String permissionId) {
        LOG.info("delete securityGroupPermission");
        int result = this.securityGroupPermissionMapper.deleteByPrimaryKey(groupId, permissionId);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean create(SecurityGroupPermission securityGroupPermission) {
        LOG.info("create securityGroupPermission");
        setCreatedTimestamp(securityGroupPermission);
        int result = this.securityGroupPermissionMapper.insert(securityGroupPermission);
        LOG.info("result = {}", result);
        return result > 0;
    }

}
