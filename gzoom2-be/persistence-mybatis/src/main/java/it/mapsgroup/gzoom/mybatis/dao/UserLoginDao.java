package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import it.mapsgroup.gzoom.mybatis.dto.UserLoginPersistent;
import it.mapsgroup.gzoom.mybatis.mapper.UserLoginMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UserLoginDao extends AbstractDao {
    private static final Logger LOG = getLogger(UserLoginDao.class);
    private final UserLoginMapper userLoginMapper;

    @Autowired
    public UserLoginDao(UserLoginMapper userLoginMapper) {
        this.userLoginMapper = userLoginMapper;
    }

    @Transactional
    public UserLogin getUserLogin(String username) {
        LOG.info("getUserLogin");

        List<UserLogin> userLoginList = this.userLoginMapper.getUserLogin(username);
        LOG.info("size {}", userLoginList.size());
        return userLoginList.isEmpty() ? null : userLoginList.get(0);
    }

    @Transactional
    public UserLogin selectByUserLoginId(String username) {
        LOG.info("getUserLogin");

        UserLogin userLogin = this.userLoginMapper.selectByUserLoginId(username);
        LOG.info("result {}", userLogin);
        return userLogin;
    }

    @Transactional
    public List<UserLogin> findAllOrderByPrimaryKey() {
        LOG.info("findByContentTypeId");

        List<UserLogin> userLoginList = this.userLoginMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", userLoginList.size());
        return userLoginList;
    }

    @Transactional
    public boolean update(UserLoginPersistent userLoginPersistent) {
        LOG.info("update userLoginPersistent");
        setUpdateTimestamp(userLoginPersistent);
        int result = this.userLoginMapper.updateByPrimaryKey(userLoginPersistent);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public int anonymizeUserLogin(Instant expirationDate) {
        LOG.info("anonymizeUserLogin update");
        int result = this.userLoginMapper.anonymizeUserLogin(expirationDate);
        LOG.info("result = {}", result);
        return result;
    }
}
