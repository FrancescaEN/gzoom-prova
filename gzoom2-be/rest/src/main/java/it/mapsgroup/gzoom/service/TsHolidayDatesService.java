package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.TsHolidaysDatesDao;
import it.mapsgroup.gzoom.mybatis.dto.TsHolidaysDates;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TsHolidayDatesService {

    private static final Logger LOG = getLogger(TimeEntryService.class);

    public final TsHolidaysDatesDao tsHolidayDatesDao;

    public TsHolidayDatesService(TsHolidaysDatesDao tsHolidayDatesDao) {
        this.tsHolidayDatesDao = tsHolidayDatesDao;
    }

    public Result<TsHolidaysDates> getTsHolidayDates(String id) throws SQLException {
        List<TsHolidaysDates> list = tsHolidayDatesDao.getTsHolidayDatesByTimesheetId(id);
        return new Result<>(list, list.size());
    }
}
