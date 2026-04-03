package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.PeriodTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.PeriodType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * PeriodType service.
 *
 */
@Service
public class PeriodTypeService {

    private static final Logger LOG = getLogger(PeriodTypeService.class);

    private final PeriodTypeDao periodTypeDao;


    @Autowired
    public PeriodTypeService(PeriodTypeDao periodTypeDao) {
        this.periodTypeDao = periodTypeDao;
    }

    public Result<PeriodType> getPeriodTypes() {
        List<PeriodType> list = periodTypeDao.selectAllOrderByPrimaryKey();
        return new Result<>(list, list.size());
    }

    public boolean updatePeriodType(PeriodType periodType) {
        Validators.assertNotNull(periodType.getPeriodTypeId(), Messages.PERIOD_TYPE_ID_REQUIRED);
        PeriodType record = periodTypeDao.getPeriodType(periodType.getPeriodTypeId());
        Validators.assertNotNull(record, Messages.INVALID_PERIOD_TYPE);
        return periodTypeDao.update(periodType, principal().getUserLoginId());
    }

    public boolean deletePeriodType(String[] periodTypes) {
        for(String id : periodTypes ) {
            if (id != null ) {
                if(id.length() < 3 || !id.substring(0, 3).equals("new")){
                    Validators.assertNotBlank(id, Messages.PERIOD_TYPE_ID_REQUIRED);
                    PeriodType record = periodTypeDao.getPeriodType(id);
                    Validators.assertNotNull(record, Messages.INVALID_TIME_ENTRY);
                    periodTypeDao.delete(id);
                }
            }
        }
        return true;
    }

    public String createPeriodType(PeriodType periodType) {
        Validators.assertNotNull(periodType, Messages.PERIOD_TYPE_REQUIRED);
        Validators.assertNotBlank(periodType.getPeriodTypeId(), Messages.PERIOD_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(periodType.getDescription(), Messages.PERIOD_TYPE_DESCRIPTION_REQUIRED);

        periodType.setPeriodLength(null);
        periodType.setUomId(null);
        periodTypeDao.create(periodType, principal().getUserLoginId());
        return periodType.getPeriodTypeId();
    }

}

