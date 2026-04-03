package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.NoteDataDao;
import it.mapsgroup.gzoom.mybatis.dao.TimesheetDao;
import it.mapsgroup.gzoom.mybatis.dto.Timesheet;
import it.mapsgroup.gzoom.mybatis.dto.TimesheetEx;
import it.mapsgroup.gzoom.mybatis.dto.NoteData;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Profile service.
 *
 */
@Service
public class TimesheetService {
    private static final Logger LOG = getLogger(TimesheetService.class);

    private final TimesheetDao timesheetDao;
    private final NoteDataDao noteDataDao;
    private final Configuration config;

    @Autowired
    public TimesheetService(TimesheetDao timesheetDao, NoteDataDao noteDataDao, Configuration config) {
        this.timesheetDao = timesheetDao;
        this.noteDataDao = noteDataDao;
        this.config = config;
    }

    public Result<TimesheetEx> getTimesheet(String userLoginId) throws SQLException {
        List<TimesheetEx> list = timesheetDao.getTimesheetExs(userLoginId);
        return new Result<>(list, list.size());
    }

    public TimesheetEx getTimesheetExsBy(String timesheetId, String userLoginId) throws SQLException {
        TimesheetEx timesheetEx = timesheetDao.getTimesheetExsById(timesheetId, userLoginId);
        return timesheetEx;
    }



    public Result<TimesheetEx> getTimesheetPagination(String userLoginId, InfoPage infoPage) throws SQLException {
        List<TimesheetEx> list = timesheetDao.findTimesheetsPagination(userLoginId, infoPage );
        Result<TimesheetEx> result = new Result<>(list, list.size());
        return result;
    }

    public Result<NoteData> getParamsTimesheet(String id) {
        List<NoteData> list = noteDataDao.getNoteDataParamsTimesheet(id);
        return new Result<>(list, list.size());
    }


    public String createTimesheet(Timesheet req) {
        Validators.assertNotNull(req, Messages.TIMESHEET_REQUIRED);
        Validators.assertNotBlank(req.getPartyId(), Messages.PARTY_ID_REQUIRED);
        Timesheet timesheet = new Timesheet();
        timesheet.setPartyId(req.getPartyId());
        timesheet.setFromDate(req.getFromDate().with(LocalDateTime.MIN));
        timesheet.setThruDate(req.getThruDate().with(LocalDateTime.MIN));
        timesheet.setActualHours(req.getActualHours());
        timesheet.setContractHours(req.getContractHours());
        timesheetDao.create(timesheet);
        return timesheet.getTimesheetId();
    }


    public String validStatusItem(String id) {
        Validators.assertNotBlank(id, Messages.TIMESHEET_ID_REQUIRED);
       Timesheet record = timesheetDao.findById(id);
        Validators.assertNotNull(record, Messages.INVALID_TIMESHEET);

        timesheetDao.validStatusId(principal().getUserLoginId(), record);
        return record.getTimesheetId();
    }

    public String reopenStatusItem(String id) {
        Validators.assertNotBlank(id, Messages.TIMESHEET_ID_REQUIRED);
        Timesheet record = timesheetDao.findById(id);
        Validators.assertNotNull(record, Messages.INVALID_TIMESHEET);

        timesheetDao.reopenStatusId(principal().getUserLoginId(), record);
        return record.getTimesheetId();
    }

    public Boolean deleteTimesheet(String[] data) {
        for(String id : data ){
            Validators.assertNotBlank(id, Messages.TIMESHEET_ID_REQUIRED);
            Timesheet record = timesheetDao.findById(id);
            Validators.assertNotNull(record, Messages.INVALID_TIMESHEET);
            timesheetDao.delete(id);
        }
        return true;
    }

}
