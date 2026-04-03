package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.UserLoginPasswordHistory;
import it.mapsgroup.gzoom.mybatis.mapper.UserLoginPasswordHistoryMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UserLoginPasswordHistoryDao extends AbstractDao {
    private static final Logger LOG = getLogger(UserLoginPasswordHistoryDao.class);
    private final UserLoginPasswordHistoryMapper userLoginPasswordHistoryMapper;

    @Autowired
    public UserLoginPasswordHistoryDao(UserLoginPasswordHistoryMapper userLoginPasswordHistoryMapper) {
        this.userLoginPasswordHistoryMapper = userLoginPasswordHistoryMapper;
    }

    @Transactional
    public boolean create(UserLoginPasswordHistory userLoginPasswordHistory) {
        LOG.info("create userLoginPasswordHistory");
        setCreatedTimestamp(userLoginPasswordHistory);
        int result = this.userLoginPasswordHistoryMapper.insert(userLoginPasswordHistory);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean update(UserLoginPasswordHistory userLoginPasswordHistory) {
        LOG.info("update userLoginPasswordHistory");
        setUpdateTimestamp(userLoginPasswordHistory);
        int result = this.userLoginPasswordHistoryMapper.updateByPrimaryKey(userLoginPasswordHistory);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
