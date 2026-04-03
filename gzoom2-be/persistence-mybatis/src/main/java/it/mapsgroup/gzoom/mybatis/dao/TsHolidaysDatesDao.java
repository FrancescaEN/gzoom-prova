package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.TsHolidaysDates;
import it.mapsgroup.gzoom.mybatis.mapper.TsHolidaysDatesMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TsHolidaysDatesDao extends AbstractDao{
    private static final Logger LOG = getLogger(TsHolidaysDatesDao.class);
    private final TsHolidaysDatesMapper tsHolidaysDatesMapper;

    @Autowired
    public TsHolidaysDatesDao(TsHolidaysDatesMapper tsHolidaysDatesMapper) {
        this.tsHolidaysDatesMapper = tsHolidaysDatesMapper;
    }

    @Transactional
    public List<TsHolidaysDates> getTsHolidayDatesByTimesheetId(String timesheetId) {
        LOG.info("find tsHolidaysDates by timesheetId");

        List<TsHolidaysDates> tsHolidaysDatesList = this.tsHolidaysDatesMapper.getTsHolidayDatesByTimesheetId(timesheetId);
        LOG.info("size = {}", tsHolidaysDatesList.size());
        return tsHolidaysDatesList;
    }


}
