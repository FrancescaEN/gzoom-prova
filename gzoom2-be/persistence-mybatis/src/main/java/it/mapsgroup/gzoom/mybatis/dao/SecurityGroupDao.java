package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.SecurityGroup;
import it.mapsgroup.gzoom.mybatis.mapper.SecurityGroupMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class SecurityGroupDao extends AbstractDao {
    private static final Logger LOG = getLogger(SecurityGroupDao.class);
    private final SecurityGroupMapper securityGroupMapper;

    @Autowired
    public SecurityGroupDao(SecurityGroupMapper securityGroupMapper) {
        this.securityGroupMapper = securityGroupMapper;
    }

    @Transactional
    public SecurityGroup getSecurityGroupById(String groupId) {
        LOG.info("find securityGroup by id");
        SecurityGroup  securityGroup = this.securityGroupMapper.selectByPrimaryKey(groupId);
        LOG.info("securityGroup = {}", securityGroup != null);
        return securityGroup;
    }

    @Transactional
    public List<SecurityGroup> getSecurityGroups() {
        LOG.info("find all securityGroup");

        List<SecurityGroup> securityGroups = this.securityGroupMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", securityGroups.size());
        return securityGroups;
    }

    @Transactional
    public SecurityGroup getSecurityGroup(String groupId) {
        LOG.info("find securityGroup by id");

        SecurityGroup securityGroup = this.securityGroupMapper.selectByPrimaryKey(groupId);
        LOG.info("SecurityGroup = {}", (securityGroup != null));
        return securityGroup;
    }

    @Transactional
    public boolean update(SecurityGroup securityGroup, String userLoginId) {
        LOG.info("update securityGroup");
        setUpdateTimestamp(securityGroup);
        securityGroup.setLastModifiedByUserLogin(userLoginId);
        int result = this.securityGroupMapper.updateByPrimaryKey(securityGroup);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String id) {
        LOG.info("delete securityGroup");
        int result = this.securityGroupMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean create(SecurityGroup securityGroup, String userLoginId) {
        LOG.info("create securityGroup");
        setCreatedTimestamp(securityGroup);
        securityGroup.setCreatedByUserLogin(userLoginId);
        int result = this.securityGroupMapper.insert(securityGroup);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
