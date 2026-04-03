package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.SecurityGroupContent;
import it.mapsgroup.gzoom.mybatis.mapper.SecurityGroupContentMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class SecurityGroupContentDao extends AbstractDao {
    private static final Logger LOG = getLogger(SecurityGroupContentDao.class);
    private final SecurityGroupContentMapper securityGroupContentMapper;

    @Autowired
    public SecurityGroupContentDao(SecurityGroupContentMapper securityGroupContentMapper) {
        this.securityGroupContentMapper = securityGroupContentMapper;
    }

    @Transactional
    public boolean deleteByGroupId(String groupId) {
        LOG.info("delete securityGroupContent");
        int result = this.securityGroupContentMapper.deleteByGroupId(groupId);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public List<SecurityGroupContent> getSecurityGroupContentByGroupId(String groupId) {
        LOG.info("find securityGroupContent by groupId");

        List<SecurityGroupContent> securityGroupContents = this.securityGroupContentMapper.selectByGroupIdOrdByContentIdAndFromDate(groupId);
        LOG.info("size = {}", securityGroupContents.size());
        return securityGroupContents;
    }

    @Transactional
    public SecurityGroupContent getSecurityGroupContent(String groupId, String contentId, Instant fromDate) {
        LOG.info("find securityGroupContent by id");

        SecurityGroupContent securityGroupContent = this.securityGroupContentMapper.selectByPrimaryKey(groupId, contentId, fromDate);
        LOG.info("SecurityGroupContent = {}", (securityGroupContent != null));
        return securityGroupContent;
    }

    @Transactional
    public boolean update(SecurityGroupContent securityGroupContent) {
        LOG.info("update securityGroupContent");
        setUpdateTimestamp(securityGroupContent);
        int result = this.securityGroupContentMapper.updateByPrimaryKey(securityGroupContent);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String groupId, String contentId, Instant fromDate) {
        LOG.info("delete securityGroupContent");
        int result = this.securityGroupContentMapper.deleteByPrimaryKey(groupId, contentId, fromDate);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean create(SecurityGroupContent securityGroupContent) {
        LOG.info("create securityGroupContent");
        setCreatedTimestamp(securityGroupContent);
        int result = this.securityGroupContentMapper.insert(securityGroupContent);
        LOG.info("result = {}", result);
        return result > 0;
    }



}
