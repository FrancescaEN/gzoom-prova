package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.mybatis.dto.Timesheet;
import it.mapsgroup.gzoom.mybatis.dto.TimesheetEx;
import it.mapsgroup.gzoom.mybatis.mapper.TimesheetMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TimesheetDao extends AbstractDao{

    private static final Logger LOG = getLogger(TimesheetDao.class);
    private final TimesheetMapper timesheetMapper;

    @Autowired
    public TimesheetDao(TimesheetMapper timesheetMapper) {
        this.timesheetMapper = timesheetMapper;
    }

    public List<Timesheet> findAll() {

        return this.timesheetMapper.selectAll(); }

    public Integer validStatusId(String id, Timesheet record){
        record.setStatusId("TIMESHEET_COMPLETED");
        return this.timesheetMapper.updateByPrimaryKey(record);
    }

    public Integer reopenStatusId(String id, Timesheet record){
        record.setStatusId("TIMESHEET_IN_PROCESS");
        return this.timesheetMapper.updateByPrimaryKey(record);
    }

    public List<TimesheetEx> getTimesheetExs(String userLoginId) {
        return this.timesheetMapper.getTimesheetExs(userLoginId);
    }

    public TimesheetEx getTimesheetExsById(String timesheetId, String userLoginId) {
        return this.timesheetMapper.getTimesheetExsById(timesheetId, userLoginId);
    }

    public Timesheet getTimesheetById(String timesheetId) {
        return this.timesheetMapper.selectByPrimaryKey(timesheetId);
    }

    public List<TimesheetEx> findTimesheetsPagination(String userLoginId , InfoPage infoPage) {

        if(infoPage.getFilter() != null){
            for(Filter item: infoPage.getFilter()){
                if(item.getValue() != null){
                    if(item.getField().equals("fromDate") || item.getField().equals("thruDate")){
                        Instant instant = Instant.parse(item.getValue());
                        item.setDateValue(instant);
                    }
                    if(item.getField().equals("actualHours")){
                        item.setDoubleValue(Double.parseDouble(item.getValue()));
                    }
                }
            }
        }
        return this.timesheetMapper.findTimesheetsPagination(userLoginId, infoPage.getLimit(), infoPage.getOffset(), infoPage.getFilter(), infoPage.getSortOrder(), infoPage.getSortField(), infoPage.getSecondaryLang());
    }


    public Timesheet findById(String timesheetId) {

        return this.timesheetMapper.selectByPrimaryKey(timesheetId);
    }

    public boolean create(Timesheet timesheet) {

        this.timesheetMapper.insert(timesheet);
        return true;
    }

    public int update(Timesheet timesheet) {

        return this.timesheetMapper.updateByPrimaryKey(timesheet);
    }

    public int delete(String timesheetId) {

        return this.timesheetMapper.deleteByPrimaryKey(timesheetId);
    }
}
