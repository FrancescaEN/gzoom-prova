package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.PortalPage;
import it.mapsgroup.gzoom.mybatis.dto.UserLoginSecurityGroup;
import it.mapsgroup.gzoom.mybatis.mapper.UserLoginSecurityGroupMapper;
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
public class UserLoginSecurityGroupDao extends AbstractDao {
    private static final Logger LOG = getLogger(UserLoginSecurityGroupDao.class);
    private final UserLoginSecurityGroupMapper userLoginSecurityGroupMapper;

    @Autowired
    public UserLoginSecurityGroupDao(UserLoginSecurityGroupMapper userLoginSecurityGroupMapper) {
        this.userLoginSecurityGroupMapper = userLoginSecurityGroupMapper;
    }


    /**
     * This function gets the User Login Security Groups.
     * @param userLoginId
     * @param groupId
     * @return Record of UserLoginSecurityGroups.
     */
    @Transactional
    public  List<UserLoginSecurityGroup> getUserLoginSecurityGroups(String userLoginId, String groupId) {
        LOG.info("getUserLoginSecurityGroups");

        List<UserLoginSecurityGroup> userLoginList = this.userLoginSecurityGroupMapper.getUserLoginSecurityGroups(userLoginId, groupId);
        LOG.info("size {}", userLoginList.size());
        return userLoginList;
    }

    /**
     * This function gets the Default Portal Page.
     * @param userLoginId
     * @return Record of DefaultPortalPage.
     */
    @Transactional
    public String getDefaultPortalPage(String userLoginId) {
        LOG.info("getDefaultPortalPage");

        List<PortalPage> portalPageList = this.userLoginSecurityGroupMapper.getDefaultPortalPage(userLoginId);
        LOG.info("size {}", portalPageList.size());
        return portalPageList.isEmpty() ? null : portalPageList.get(0).getSecurityGroupId();
    }

    /**
     * This function gets the User Login Security Groups.
     * @param userLoginId
     * @return Record of UserLoginSecurityGroups.
     */
    @Transactional
    public List<UserLoginSecurityGroup> getUserLoginSecurityGroups(String userLoginId) {
        LOG.info("getUserLoginSecurityGroups");

        List<UserLoginSecurityGroup> userLoginSecurityGroupList = this.userLoginSecurityGroupMapper.getUserLoginSecurityGroupsByUserLoginId(userLoginId);
        LOG.info("size {}", userLoginSecurityGroupList.size());
        return userLoginSecurityGroupList;
    }

    @Transactional
    public UserLoginSecurityGroup getUserLoginSecurityGroup (String userLoginId, String groupId, Instant fromDate){
        LOG.info("getUserLoginSecurityGroup ");

        UserLoginSecurityGroup securityGroupPermission = this.userLoginSecurityGroupMapper.selectByPrimaryKey(userLoginId, groupId, fromDate);
        LOG.info("UserLoginSecurityGroup = {}", (securityGroupPermission != null));
        return securityGroupPermission;
    }

    @Transactional
    public boolean deleteByGroupId(String groupId) {
        LOG.info("delete UserLoginSecurityGroup");
        int result = this.userLoginSecurityGroupMapper.deleteByGroupId(groupId);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public List<UserLoginSecurityGroup> getUserLoginSecurityGroupByGroupId(String groupId) {
        LOG.info("find all userLoginSecurityGroup");

        List<UserLoginSecurityGroup> userLoginSecurityGroups = this.userLoginSecurityGroupMapper.selectByGroupIdOrderByPrimaryKey(groupId);
        LOG.info("size = {}", userLoginSecurityGroups.size());
        return userLoginSecurityGroups;
    }

    @Transactional
    public boolean update(UserLoginSecurityGroup userLoginSecurityGroup) {
        LOG.info("update userLoginSecurityGroup");
        setUpdateTimestamp(userLoginSecurityGroup);
        int result = this.userLoginSecurityGroupMapper.updateByPrimaryKey(userLoginSecurityGroup);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String userLoginId, String groupId, Instant fromDate) {
        LOG.info("delete userLoginSecurityGroup");
        int result = this.userLoginSecurityGroupMapper.deleteByPrimaryKey(userLoginId, groupId, fromDate);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean create(UserLoginSecurityGroup userLoginSecurityGroup) {
        LOG.info("create userLoginSecurityGroup");
        setCreatedTimestamp(userLoginSecurityGroup);
        int result = this.userLoginSecurityGroupMapper.insert(userLoginSecurityGroup);
        LOG.info("result = {}", result);
        return result > 0;
    }

}
