package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.UserLoginValidPartyRoleEx;
import it.mapsgroup.gzoom.mybatis.mapper.UserLoginValidPartyRoleMapper;
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
public class UserLoginValidPartyRoleDao extends AbstractDao {
    private static final Logger LOG = getLogger(UserLoginValidPartyRoleDao.class);
    private final UserLoginValidPartyRoleMapper userLoginValidPartyRoleMapper;

    @Autowired
    public UserLoginValidPartyRoleDao(UserLoginValidPartyRoleMapper userLoginValidPartyRoleMapper) {
        this.userLoginValidPartyRoleMapper = userLoginValidPartyRoleMapper;
    }

    /**
     * This function gets the User Login Valid Party Role Ex.
     * @param user
     * @return List of UserLoginValidPartyRoleEx.
     */
    @Transactional
    public List<UserLoginValidPartyRoleEx> getUserLoginValidPartyRoleList(String user) {
        LOG.info("getUserLoginValidPartyRoleList");

        List<UserLoginValidPartyRoleEx> userLoginList = this.userLoginValidPartyRoleMapper.getUserLoginValidPartyRoleList(user);
        LOG.info("size {}", userLoginList.size());
        return userLoginList;
    }



}
