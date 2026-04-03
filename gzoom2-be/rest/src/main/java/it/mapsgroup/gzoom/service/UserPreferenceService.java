package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginSecurityGroupDao;
import it.mapsgroup.gzoom.mybatis.dao.UserPreferenceDaoMB;
import it.mapsgroup.gzoom.mybatis.dto.UserPreference;
import it.mapsgroup.gzoom.security.Principals;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UserPreferenceService {

    private static final Logger LOG = getLogger(TimesheetService.class);

    private final UserPreferenceDaoMB userPreferenceDao;
    private final UserLoginSecurityGroupDao userLoginSecurityGroupDao;

    @Autowired
    public UserPreferenceService(UserPreferenceDaoMB userPreferenceDao, UserLoginSecurityGroupDao userLoginSecurityGroupDao) {
        this.userPreferenceDao = userPreferenceDao;
        this.userLoginSecurityGroupDao = userLoginSecurityGroupDao;
    }

    public Boolean updateUserPreference(UserPreference req) {
        Validators.assertNotNull(req, Messages.USER_PREFERENCE_REQUIRED);
        Validators.assertNotNull(principal().getUserLoginId(), Messages.USER_LOGIN_ID_REQUIRED);
        Validators.assertNotNull(req.getUserPrefTypeId(), Messages.USER_PREFERENCE_TYPE_ID_REQUIRED);

        UserPreference record = userPreferenceDao.getUserPreference(principal().getUserLoginId(), req.getUserPrefTypeId());
        if (record == null) {
            return userPreferenceDao.create(principal().getUserLoginId(), req);
        } else {
            return userPreferenceDao.update(principal().getUserLoginId(), req);
        }
    }

    public UserPreference getUserPreferenceNA(String userPrefTypeId) {
        Validators.assertNotNull(userPrefTypeId, Messages.USER_PREFERENCE_TYPE_ID_REQUIRED);

        String userLoginId = "_NA_";
        UserPreference defRrecord = userPreferenceDao.getUserPreference(userLoginId, userPrefTypeId);

        return defRrecord;
    }

    public UserPreference getUserPreference(String userPrefTypeId) {
        Validators.assertNotNull(userPrefTypeId, Messages.USER_PREFERENCE_TYPE_ID_REQUIRED);
        String userLoginId = "_NA_";
        UserPreference NArecord = userPreferenceDao.getUserPreference(userLoginId, userPrefTypeId);
        if (Principals.username() != null) {
            userLoginId = principal().getUserLoginId();
            UserPreference record = userPreferenceDao.getUserPreference(userLoginId, userPrefTypeId);
            if (record != null) {
                return record;
            }
        }

        return NArecord;
    }

    public String getOrganizationId() {
        UserPreference userPreference = this.getUserPreference("ORGANIZATION_PARTY");
        return userPreference.getUserPrefValue();
    }

    public UserPreference getUserPreferenceByUsername(String userPrefTypeId, String username) {
        Validators.assertNotNull(userPrefTypeId, Messages.USER_PREFERENCE_TYPE_ID_REQUIRED);
        String userLoginId = "_NA_";
        UserPreference NArecord = userPreferenceDao.getUserPreference(userLoginId, userPrefTypeId);
        if (username != null) {
            userLoginId = username;
            UserPreference record = userPreferenceDao.getUserPreference(userLoginId, userPrefTypeId);
            if (record != null) {
                return record;
            }
        }

        return NArecord;
    }

    public String getDefaultPortalPage() {
        return userLoginSecurityGroupDao.getDefaultPortalPage(principal().getUserLoginId());
    }
}
