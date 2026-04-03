package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.UserLoginHistory;
import it.mapsgroup.gzoom.mybatis.mapper.UserLoginHistoryMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UserLoginHistoryDao extends AbstractDao {
    private static final Logger LOG = getLogger(UserLoginHistoryDao.class);
    private final UserLoginHistoryMapper userLoginHistoryMapper;

    @Autowired
    public UserLoginHistoryDao(UserLoginHistoryMapper userLoginHistoryMapper) {
        this.userLoginHistoryMapper = userLoginHistoryMapper;
    }

    @Transactional
    public UserLoginHistory verifyToken(String username, String token) {
        LOG.info("getUserLoginHistory");
        UserLoginHistory userLoginHistory = this.userLoginHistoryMapper.verifyToken(username, token);
        LOG.info("userLoginHistory = {}", userLoginHistory);
        return userLoginHistory;
    }

    public UserLoginHistory getUserLoginHistoryById(String id, Instant fromDate){
        return userLoginHistoryMapper.selectByPrimaryKey(id, fromDate);
    }

    @Transactional
    public boolean create(UserLoginHistory userLoginHistory) {
        LOG.info("create UserLoginHistory");
        setCreatedTimestamp(userLoginHistory);
        int result = this.userLoginHistoryMapper.insert(userLoginHistory);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean update(UserLoginHistory userLoginHistory) {
        LOG.info("update UserLoginHistory");
        setUpdateTimestamp(userLoginHistory);
        int result = this.userLoginHistoryMapper.updateByPrimaryKey(userLoginHistory);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public int delete(String userLoginHistoryId, Instant fromDate) {
        LOG.info("delete UserLoginHistory");
        UserLoginHistory result = this.userLoginHistoryMapper.selectByPrimaryKey(userLoginHistoryId,fromDate);
        return this.userLoginHistoryMapper.deleteByPrimaryKey(result.getUserLoginId(), result.getFromDate());
    }

    @Transactional
    public int deleteOld(Instant minusYear) {
        LOG.info("delete old UserLoginHistory");
        return this.userLoginHistoryMapper.deleteOld(minusYear);
    }


    @Transactional
    public boolean updatePreToken(String userLoginId) {
        LOG.info("update all last UserLoginHistory where userLoginId = " + userLoginId);
        int result = this.userLoginHistoryMapper.updatePreToken(userLoginId);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
