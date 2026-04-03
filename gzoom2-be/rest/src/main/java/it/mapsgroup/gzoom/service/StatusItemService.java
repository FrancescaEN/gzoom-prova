package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.StatusItemDao;
import it.mapsgroup.gzoom.mybatis.dto.StatusItem;
import it.mapsgroup.gzoom.mybatis.dto.StatusItemExType;
import it.mapsgroup.gzoom.mybatis.dto.StatusItemExt;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class StatusItemService {
	private static final Logger LOG = getLogger(StatusItemService.class);
	
	private final StatusItemDao statusItemDao;
	private final Configuration config;

    @Autowired
    public StatusItemService(StatusItemDao statusItemDao, Configuration config) {
        this.statusItemDao = statusItemDao;
        this.config = config;
    }

    public Result<StatusItemExt> getStatusItems(String userLoginId) {
        List<StatusItemExt> list = this.statusItemDao.getStatusItems(userLoginId);
        return new Result<>(list, list.size());
    }

    public Result<StatusItem> getTimesheetStatusDropdownFilter(String userLoginId) throws SQLException {
        List<StatusItem> list = statusItemDao.getTimesheetStatusDropdownFilter(userLoginId);
        return new Result<>(list, list.size());
    }

    public Result<StatusItem> getStatusItemByStatusTypeId(String statusTypeId) {
        List<StatusItem> list = this.statusItemDao.findByStatusTypeId(statusTypeId);
        return new Result<>(list, list.size());
    }

    public Result<StatusItemExType> getStatusItemStateTo() {
        List<StatusItemExType> list = this.statusItemDao.getStatusItemStateTo();
        return new Result<>(list, list.size());
    }

    public boolean createStatusItem(StatusItem req) {
        this.validateStatusItem(req, "CREATE");
        return this.statusItemDao.create(req, principal().getUserLoginId());
    }

    public boolean updateStatusItem(StatusItem req) {
        this.validateStatusItem(req, "UPDATE");
        return this.statusItemDao.update(req, principal().getUserLoginId());
    }

    public boolean deleteStatusItem (String statusId) {
        Messages msg = new Messages();
        Validators.assertNotBlank(statusId, msg.getMessageColumn(Messages.STATUS_ITEM, Messages.STATUS_ID, Messages.IS_REQUIRED));
        StatusItem record = this.statusItemDao.getStatusItem(statusId);
        Validators.assertFalse(record == null, msg.getMessageColumn(Messages.STATUS_ITEM, Messages.STATUS_ID, Messages.INVALID));

        return this.statusItemDao.delete(statusId);
    }


    private void validateStatusItem(StatusItem req, String method) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.STATUS_ITEM, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getStatusId(), msg.getMessageColumn(Messages.STATUS_ITEM, Messages.STATUS_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getStatusTypeId(), msg.getMessageColumn(Messages.STATUS_ITEM, Messages.STATUS_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getDescription(), msg.getMessageColumn(Messages.STATUS_ITEM, Messages.DESCRIPTION, Messages.IS_REQUIRED));
        if (config.getLanguageType().equals("BILING")) Validators.assertNotNull(req.getDescriptionLang(), msg.getMessageColumn(Messages.STATUS_ITEM, Messages.DESCRIPTION_LANG, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getStatusCode(), msg.getMessageColumn(Messages.STATUS_ITEM, Messages.STATUS_CODE, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getSequenceId(), msg.getMessageColumn(Messages.STATUS_ITEM, Messages.SEQUENCE_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getActStEnumId(), msg.getMessageColumn(Messages.STATUS_ITEM, Messages.ACT_ST_ENUM_ID, Messages.IS_REQUIRED));

        StatusItem record = this.statusItemDao.getStatusItem(req.getStatusId());
        if ( method.equalsIgnoreCase("UPDATE")) {
            Validators.assertFalse(record == null, msg.getMessageColumn(Messages.STATUS_ITEM, Messages.STATUS_TYPE_ID, Messages.INVALID));
        }
        if ( method.equalsIgnoreCase("CREATE")) {
            Validators.assertFalse(record != null, msg.getMessageColumn(Messages.STATUS_ITEM, Messages.STATUS_TYPE_ID, Messages.EXISTING));
        }

    }

    public Result<StatusItemExType> getStatusItemStateFrom(String statusTypeId) {
        List<StatusItemExType> list = this.statusItemDao.getStatusItemStateFrom(statusTypeId);
        return new Result<>(list, list.size());
    }

    public Result<StatusItem> getStatusItembyCode(String statusCode) {
        List<StatusItem> list = this.statusItemDao.getStatusItemByCode(statusCode);
        return new Result<>(list, list.size());
    }
}
