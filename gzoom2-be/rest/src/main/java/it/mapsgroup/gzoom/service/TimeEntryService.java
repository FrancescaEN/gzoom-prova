package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.TimeEntryDao;
import it.mapsgroup.gzoom.mybatis.dao.TimesheetDao;
import it.mapsgroup.gzoom.mybatis.dto.TimeEntry;
import it.mapsgroup.gzoom.mybatis.dto.TimeEntryEx;
import it.mapsgroup.gzoom.mybatis.dto.Timesheet;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffort;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Profile service.
 *
 */
@Service
public class TimeEntryService {
    private static final Logger LOG = getLogger(TimeEntryService.class);

    private final TimesheetDao timesheetDao;
    private final TimeEntryDao timeEntryDao;
    private final DtoMapper dtoMapper;

    @Autowired
    public TimeEntryService(TimesheetDao timesheetDao, TimeEntryDao timeEntryDao,
                            DtoMapper dtoMapper) {
        this.timesheetDao = timesheetDao;
        this.timeEntryDao = timeEntryDao;
        this.dtoMapper = dtoMapper;
    }

    public Result<TimeEntryEx> getTimeEntries(String id) {
        List<TimeEntryEx> list = timeEntryDao.getTimeEntryEx(id);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffort> getWorkEfforts(String id, boolean secondaryLang) {
        List<WorkEffort> list = timeEntryDao.getWorkEffort(id, secondaryLang);
        return new Result<>(list, list.size());
    }

    public Boolean updateTimeEntry(TimeEntry item, String userLoginId) {
        final double[] sumActualHours = {0};
        final double[] sumPlanHours = {0};
        Validators.assertNotNull(item.getTimeEntryId(), Messages.TIME_ENTRY_ID_REQUIRED);
        TimeEntry record = timeEntryDao.findById(item.getTimeEntryId());
        Validators.assertNotNull(record, Messages.INVALID_TIME_ENTRY);
        timeEntryDao.update(item, userLoginId);
        String id = item.getTimesheetId();
        Timesheet timesheet = timesheetDao.getTimesheetById(id);
        List<TimeEntryEx> timeEntries = timeEntryDao.getTimeEntryEx(id);
        timeEntries.forEach(element -> {
           sumActualHours[0] = sumActualHours[0] + element.getHours();
           sumPlanHours[0] = sumPlanHours[0] + element.getPlanHours();
        });
        timesheet.setActualHours(sumActualHours[0]);
        timesheet.setContractHours(sumPlanHours[0]);
        timesheetDao.update(timesheet);
        return true;
    }


    public String createTimeEntry(TimeEntry item, String userLoginId) {
        final Double[] sumActualHours = {(double) 0};
        final Double[] sumPlanHours = {(double) 0};

        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setWorkEffortId(item.getWorkEffortId());
        timeEntry.setPercentage(item.getPercentage());
        timeEntry.setFromDate(item.getFromDate());
        timeEntry.setThruDate(item.getThruDate());
        timeEntry.setTimeEntryId(item.getTimeEntryId());
        timeEntry.setTimesheetId(item.getTimesheetId());
        timeEntry.setPlanHours(item.getPlanHours());
        timeEntry.setHours(item.getHours());
        timeEntry.setComments(item.getComments());
        timeEntry.setEffortUomId(item.getEffortUomId());
        timeEntry.setRateTypeId(item.getRateTypeId());
        timeEntry.setPartyId(item.getPartyId());
        timeEntry.setOrderId(item.getOrderId());
        timeEntry.setJobId(item.getJobId());
        String idTimeEntry = timeEntryDao.create(timeEntry, userLoginId);
        Timesheet timesheet = timesheetDao.getTimesheetById(timeEntry.getTimesheetId());
        List<TimeEntryEx> timeEntries = timeEntryDao.getTimeEntryEx(timeEntry.getTimesheetId());
        timeEntries.forEach(element -> {
            sumActualHours[0] = sumActualHours[0] + element.getHours();
            sumPlanHours[0] = sumPlanHours[0] + element.getPlanHours();
        });

        timesheet.setActualHours(sumActualHours[0]);
        timesheet.setContractHours(sumPlanHours[0]);
        timesheetDao.update(timesheet);
        return idTimeEntry;
    }

    public Boolean deleteTimeEntry(String[] data) {
            for(String id : data ) {
                final Double[] sumActualHours = {(double) 0};
                final Double[] sumPlanHours = {(double) 0};

                if (id != null && !id.substring(0,3).equals("new")) {
                    Validators.assertNotBlank(id, Messages.TIME_ENTRY_ID_REQUIRED);
                    TimeEntry record = timeEntryDao.findById(id);
                    Validators.assertNotNull(record, Messages.INVALID_TIME_ENTRY);
                    timeEntryDao.delete(id);
                    String idTimesheet = record.getTimesheetId();
                    Timesheet timesheet  = timesheetDao.findById(idTimesheet);
                    List<TimeEntryEx> timeEntries = timeEntryDao.getTimeEntryEx(idTimesheet);
                    timeEntries.forEach(element -> {
                        sumActualHours[0] = sumActualHours[0] + element.getHours();
                        sumPlanHours[0] = sumPlanHours[0] + element.getPlanHours();
                    });
                    timesheet.setActualHours(sumActualHours[0]);
                    timesheet.setContractHours(sumPlanHours[0]);
                    timesheetDao.update(timesheet);
                }
            }
        return true;
    }
}
