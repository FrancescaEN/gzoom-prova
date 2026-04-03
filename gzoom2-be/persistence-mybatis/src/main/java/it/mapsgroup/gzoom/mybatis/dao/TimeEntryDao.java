package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.TimeEntry;
import it.mapsgroup.gzoom.mybatis.mapper.TimeEntryMapper;
import it.mapsgroup.gzoom.mybatis.dto.TimeEntryEx;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffort;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TimeEntryDao extends AbstractDao{

    private static final Logger LOG = getLogger(TimeEntryDao.class);
    private final TimeEntryMapper timeEntryMapper;
    private final SequenceGenerator sequenceGenerator;

    @Autowired
    public TimeEntryDao(TimeEntryMapper timeEntryMapper, SequenceGenerator sequenceGenerator) {
        this.timeEntryMapper = timeEntryMapper;
        this.sequenceGenerator = sequenceGenerator;
    }

    public List<TimeEntry> findAll() {

        return this.timeEntryMapper.selectAll(); }

    public List<TimeEntryEx> getTimeEntryEx(String timesheetId) {
        return this.timeEntryMapper.getTimeEntryEx(timesheetId);
    }

    public List<WorkEffort> getWorkEffort(String timesheetId, boolean secondaryLang) {
        return this.timeEntryMapper.getWorkEffort(timesheetId, secondaryLang);
    }

    public TimeEntry findById(String timeEntryId) {

        return this.timeEntryMapper.selectByPrimaryKey(timeEntryId);
    }

    public String create(TimeEntry timeEntry, String userLoginId) {

        String id = sequenceGenerator.getNextSeqId("TimeEntry");
        timeEntry.setTimeEntryId(id);
        setCreatedTimestamp(timeEntry);
        timeEntry.setCreatedByUserLogin(userLoginId);
        timeEntry.setLastModifiedByUserLogin(userLoginId);
        this.timeEntryMapper.insert(timeEntry);
        return id;
    }

    public int update(TimeEntry timeEntry, String userLoginId) {

        setUpdateTimestamp(timeEntry);
        timeEntry.setLastModifiedByUserLogin(userLoginId);
        return this.timeEntryMapper.updateByPrimaryKey(timeEntry);
    }

    public int delete(String timeEntryId) {

        return this.timeEntryMapper.deleteByPrimaryKey(timeEntryId);
    }
}
