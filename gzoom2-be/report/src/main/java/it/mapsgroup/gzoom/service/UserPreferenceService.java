package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.mybatis.dao.UserPreferenceDaoMB;
import it.mapsgroup.gzoom.mybatis.dto.UserPreference;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;
@Service
public class UserPreferenceService {
    private static final Logger LOG = getLogger(UserPreferenceService.class);
    private final UserPreferenceDaoMB userPreferenceDao;

    @Autowired
    public UserPreferenceService(UserPreferenceDaoMB userPreferenceDao) {
        this.userPreferenceDao = userPreferenceDao;
    }

    public UserPreference getUserPreference(String userLoginId) {
        UserPreference ret = userPreferenceDao.getUserPreference(userLoginId, "ORGANIZATION_PARTY");
        return ret;
    }
}