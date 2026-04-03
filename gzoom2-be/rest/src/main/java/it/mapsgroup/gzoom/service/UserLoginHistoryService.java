package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginHistoryDao;
import it.mapsgroup.gzoom.mybatis.dto.UserLoginHistory;
import it.mapsgroup.gzoom.ofbiz.service.ChangePasswordServiceOfBiz;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Profile service.
 *
 */
@Service
public class UserLoginHistoryService {
    private static final Logger LOG = getLogger(UserLoginHistoryService.class);

    private final ChangePasswordServiceOfBiz changeService;
    private final UserLoginHistoryDao userLoginHistoryDao;
    private final LocaleService localeService;

    @Autowired
    public UserLoginHistoryService(ChangePasswordServiceOfBiz changeService, UserLoginHistoryDao userLoginHistoryDao, LocaleService localeService) {
        this.changeService = changeService;
        this.userLoginHistoryDao = userLoginHistoryDao;
        this.localeService = localeService;
    }


    public int deleteUserLoginHistory(String id, Instant fromDate) {
        Messages msg = new Messages();
        UserLoginHistory record = userLoginHistoryDao.getUserLoginHistoryById(id, fromDate);
        Validators.assertNotBlank(id, msg.getMessageColumn(Messages.VISITOR, Messages.VISITOR_ID, Messages.IS_REQUIRED));

        return userLoginHistoryDao.delete(record.getUserLoginId(), record.getFromDate());
    }

    public int deleteOld(Instant minusYears) {
        return userLoginHistoryDao.deleteOld(minusYears);
    }

}
