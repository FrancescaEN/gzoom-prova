package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.SecurityPermission;
import it.mapsgroup.gzoom.mybatis.mapper.SecurityPermissionMapper;
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
public class SecurityPermissionDao extends AbstractDao {
    private static final Logger LOG = getLogger(SecurityPermissionDao.class);
    private final SecurityPermissionMapper securityPermissionMapper;

    @Autowired
    public SecurityPermissionDao(SecurityPermissionMapper securityPermissionMapper) {
        this.securityPermissionMapper = securityPermissionMapper;
    }

    @Transactional
    public List<SecurityPermission> getPermission(String userLoginId) {
        LOG.info("getPermission");

        List<SecurityPermission> securityPermissionList = this.securityPermissionMapper.getPermission(userLoginId);
        LOG.info("size = {}", securityPermissionList.size());
        return securityPermissionList;
    }

    @Transactional
    public List<SecurityPermission> findByEnabledOrderByPrimaryKey(String enabled) {
        LOG.info("findByEnabledOrderByPrimaryKey");

        List<SecurityPermission> securityPermissionList = this.securityPermissionMapper.selectByEnabledOrderByPrimaryKey(enabled);
        LOG.info("size = {}", securityPermissionList.size());
        return securityPermissionList;
    }
}
