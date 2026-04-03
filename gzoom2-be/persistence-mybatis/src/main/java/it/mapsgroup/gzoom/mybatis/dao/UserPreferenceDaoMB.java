package it.mapsgroup.gzoom.mybatis.dao;


import it.mapsgroup.gzoom.mybatis.dto.UserPreference;
import it.mapsgroup.gzoom.mybatis.mapper.UserPreferenceMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UserPreferenceDaoMB extends AbstractDao{
    private static final Logger LOG = getLogger(UserPreferenceDaoMB.class);
    private final UserPreferenceMapper userPreferenceMapper;

    @Autowired
    public UserPreferenceDaoMB(UserPreferenceMapper userPreferenceMapper) {
        this.userPreferenceMapper = userPreferenceMapper;
    }

    @Transactional
    public UserPreference getUserPreference(String userLoginId, String userPrefTypeId) {
        LOG.info("find userPreference by id");

        UserPreference userPreference = this.userPreferenceMapper.selectByPrimaryKey(userLoginId, userPrefTypeId);
        LOG.info("UserPreference = {}", (userPreference != null));
        return userPreference;
    }

    @Transactional
    public boolean create(String userLoginId, UserPreference userPreference) {
        LOG.info("create userPreference");
        setCreatedTimestamp(userPreference);
        userPreference.setUserLoginId(userLoginId);
        userPreference.setUserPrefGroupTypeId("GLOBAL_PREFERENCES");
        int result = this.userPreferenceMapper.insert(userPreference);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean update(String loginId, UserPreference userPreference) {
        LOG.info("update userPreference");
        setUpdateTimestamp(userPreference);
        userPreference.setUserLoginId(loginId);
        userPreference.setUserPrefGroupTypeId("GLOBAL_PREFERENCES");
        int result = this.userPreferenceMapper.updateByPrimaryKey(userPreference);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
