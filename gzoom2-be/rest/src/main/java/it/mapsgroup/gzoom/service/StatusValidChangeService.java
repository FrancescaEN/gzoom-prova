package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.StatusItemDao;
import it.mapsgroup.gzoom.mybatis.dao.StatusValidChangeDao;
import it.mapsgroup.gzoom.mybatis.dto.StatusValidChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class StatusValidChangeService {
    private final StatusValidChangeDao statusValidChangeDao;
    private final StatusItemDao statusItemDao;

    @Autowired
    public StatusValidChangeService(StatusValidChangeDao statusValidChangeDao, StatusItemDao statusItemDao) {
        this.statusValidChangeDao = statusValidChangeDao;
        this.statusItemDao = statusItemDao;
    }

    public Result<StatusValidChange> getStatusValidChangeByStatusTypeId(String statusTypeId) {
        List<StatusValidChange> list = this.statusValidChangeDao.findByStatusTypeId(statusTypeId);
        return new Result<>(list, list.size());
    }

    public boolean createStatusValidChange(StatusValidChange req, String statusTypeId) {
        this.validateStatusValidChange(req, "CREATE", statusTypeId);
        return this.statusValidChangeDao.create(req, principal().getUserLoginId());
    }

    public boolean updateStatusValidChange(StatusValidChange req, String statusTypeId) {
        this.validateStatusValidChange(req, "UPDATE", statusTypeId);
        return this.statusValidChangeDao.update(req, principal().getUserLoginId());
    }

    public boolean deleteStatusValidChange (String statusId, String statusIdTo) {
        Messages msg = new Messages();
        Validators.assertNotBlank(statusId, msg.getMessageColumn(Messages.STATUS_VALID_CHANGE, Messages.STATUS_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(statusId, msg.getMessageColumn(Messages.STATUS_VALID_CHANGE, Messages.STATUS_ID_TO, Messages.IS_REQUIRED));
        StatusValidChange record = this.statusValidChangeDao.getStatusValidChange(statusId, statusIdTo);
        Validators.assertFalse(record == null, msg.getMessageTable(Messages.STATUS_VALID_CHANGE, Messages.INVALID));

        return this.statusValidChangeDao.delete(statusId, statusIdTo);
    }


    private void validateStatusValidChange(StatusValidChange req, String method, String statusTypeId) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.STATUS_VALID_CHANGE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getStatusId(), msg.getMessageColumn(Messages.STATUS_VALID_CHANGE, Messages.STATUS_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getStatusIdTo(), msg.getMessageColumn(Messages.STATUS_VALID_CHANGE, Messages.STATUS_ID_TO, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getTransitionName(), msg.getMessageColumn(Messages.STATUS_VALID_CHANGE, Messages.TRANSITION_NAME, Messages.IS_REQUIRED));

        StatusValidChange record = this.statusValidChangeDao.getStatusValidChange(req.getStatusId(), req.getStatusIdTo());
        if ( method.equalsIgnoreCase("UPDATE")) {
            Validators.assertFalse(record == null, msg.getMessageTable(Messages.STATUS_VALID_CHANGE, Messages.INVALID));
        }
        if ( method.equalsIgnoreCase("CREATE")) {
            Validators.assertFalse(record != null, msg.getMessageTable(Messages.STATUS_VALID_CHANGE, Messages.EXISTING));
        }

        if (
                !this.statusItemDao.getStatusItem(req.getStatusId()).getStatusTypeId().equals(statusTypeId) &&
                        !this.statusItemDao.getStatusItem(req.getStatusIdTo()).getStatusTypeId().equals(statusTypeId)
        ) {
            Validators.assertFalse(true, msg.getMessagesWithSpace(Messages.STATUS_VALID_CHANGE, Messages.STATUS_ID, "or", Messages.STATUS_ID_TO, "must belong to", statusTypeId));
        }

    }
}
