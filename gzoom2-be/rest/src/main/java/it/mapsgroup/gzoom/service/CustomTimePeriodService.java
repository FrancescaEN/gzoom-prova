package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.CustomTimePeriodDao;
import it.mapsgroup.gzoom.mybatis.dto.CustomTimePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.*;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class CustomTimePeriodService {

    private final Configuration config;
    private final CustomTimePeriodDao customTimePeriodDao;

    @Autowired
    public CustomTimePeriodService(Configuration config, CustomTimePeriodDao customTimePeriodDao) {
        this.config = config;
        this.customTimePeriodDao = customTimePeriodDao;
    }

    public Result<CustomTimePeriod> getCustomTimePeriods(String periodTypeId) {
        List<CustomTimePeriod> list = customTimePeriodDao.selectByPeriodTypeIdOrderByThruDate(periodTypeId);
        return new Result<>(list, list.size());
    }

    public Result<CustomTimePeriod> getCustomTimePeriodList() {
        List<CustomTimePeriod> list = this.customTimePeriodDao.selectAllOrderByPrimaryKey();
        return new Result<>(list, list.size());
    }

    public Result<CustomTimePeriod> getTimesheetCustomTimePeriodDropdownFilter(String userLoginId) throws SQLException {
        List<CustomTimePeriod> list = customTimePeriodDao.getTimesheetCustomTimePeriodDropdownFilter(userLoginId);
        return new Result<>(list, list.size());
    }

    public boolean createCustomTimePeriod(CustomTimePeriod req) {
        Validators.assertNotNull(req, Messages.CUSTOM_TIME_PERIOD_REQUIRED);
        Validators.assertNotBlank(req.getCustomTimePeriodId(), Messages.CUSTOM_TIME_PERIOD_ID_REQUIRED);
        CustomTimePeriod record = customTimePeriodDao.get(req.getCustomTimePeriodId());
        Validators.assertTrue(record == null, Messages.CUSTOM_TIME_PERIOD_ID_EXIST);
        Validators.assertNotBlank(req.getCustomTimePeriodCode(), Messages.CUSTOM_TIME_PERIOD_CODE_REQUIRED);
        Validators.assertNotBlank(req.getPeriodTypeId(), Messages.CUSTOM_TIME_PERIOD_PERIOD_TYPE_ID_REQUIRED);
        Validators.assertNotNull(req.getPeriodNum(), Messages.CUSTOM_TIME_PERIOD_PERIOD_NUM_REQUIRED);
        Validators.assertNotBlank(req.getPeriodName(), Messages.CUSTOM_TIME_PERIOD_PERIOD_NAME_REQUIRED);
        Validators.assertNotBlank(req.getIsClosed(), Messages.CUSTOM_TIME_PERIOD_IS_CLOSED_REQUIRED);
        Validators.assertNotNull(req.getFromDate(), Messages.CUSTOM_TIME_PERIOD_FROM_DATE_REQUIRED);
        Validators.assertNotNull(req.getThruDate(), Messages.CUSTOM_TIME_PERIOD_THRU_DATE_REQUIRED);

        Validators.assertIsBeforeOrEqual(Date.from(req.getFromDate().atZone(ZoneId.systemDefault()).toInstant()), Date.from(req.getThruDate().atZone(ZoneId.systemDefault()).toInstant()), Messages.CUSTOM_TIME_PERIOD_OUT_RANGE_DATE);

        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getPeriodNameLang(), Messages.CUSTOM_TIME_PERIOD_PERIOD_NAME_LANG_REQUIRED);
            Validators.assertNotBlank(req.getCustomTimePeriodCodeLang(), Messages.CUSTOM_TIME_PERIOD_CODE_LANG_REQUIRED);
        }

        return customTimePeriodDao.create(req, principal().getUserLoginId());
    }

    public boolean updateCustomTimePeriod(CustomTimePeriod req) {
        Validators.assertNotNull(req, Messages.CUSTOM_TIME_PERIOD_REQUIRED);
        Validators.assertNotBlank(req.getCustomTimePeriodId(), Messages.CUSTOM_TIME_PERIOD_ID_REQUIRED);
        Validators.assertNotBlank(req.getCustomTimePeriodCode(), Messages.CUSTOM_TIME_PERIOD_CODE_REQUIRED);
        Validators.assertNotBlank(req.getPeriodTypeId(), Messages.CUSTOM_TIME_PERIOD_PERIOD_TYPE_ID_REQUIRED);
        Validators.assertNotNull(req.getPeriodNum(), Messages.CUSTOM_TIME_PERIOD_PERIOD_NUM_REQUIRED);
        Validators.assertNotBlank(req.getPeriodName(), Messages.CUSTOM_TIME_PERIOD_PERIOD_NAME_REQUIRED);
        Validators.assertNotBlank(req.getIsClosed(), Messages.CUSTOM_TIME_PERIOD_IS_CLOSED_REQUIRED);
        Validators.assertNotNull(req.getFromDate(), Messages.CUSTOM_TIME_PERIOD_FROM_DATE_REQUIRED);
        Validators.assertNotNull(req.getThruDate(), Messages.CUSTOM_TIME_PERIOD_THRU_DATE_REQUIRED);

        Validators.assertIsBefore(Date.from(req.getFromDate().atZone(ZoneId.systemDefault()).toInstant()), Date.from(req.getThruDate().atZone(ZoneId.systemDefault()).toInstant()), Messages.CUSTOM_TIME_PERIOD_OUT_RANGE_DATE);

        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getPeriodNameLang(), Messages.CUSTOM_TIME_PERIOD_PERIOD_NAME_LANG_REQUIRED);
            Validators.assertNotBlank(req.getCustomTimePeriodCodeLang(), Messages.CUSTOM_TIME_PERIOD_CODE_LANG_REQUIRED);
        }

        CustomTimePeriod record = customTimePeriodDao.get(req.getCustomTimePeriodId());
        Validators.assertNotNull(record, Messages.INVALID_CUSTOM_TIME_PERIOD);
        return customTimePeriodDao.update(req, principal().getUserLoginId());
    }

    public boolean deleteCustomTimePeriod(String[] idList) {
        Map<String, CustomTimePeriod> listRecord = new HashMap<>();
        Map<String, CustomTimePeriod> listRecordTemp = new HashMap<>();
        Map<String, CustomTimePeriod> parentList = new HashMap<>();

        for (String i : idList) {
            Validators.assertNotBlank(i, Messages.CUSTOM_TIME_PERIOD_ID_REQUIRED);
            CustomTimePeriod record = customTimePeriodDao.get(i);
            Validators.assertNotNull(record, Messages.INVALID_CUSTOM_TIME_PERIOD);

            listRecord.put(record.getCustomTimePeriodId(),record);
        }

        int control = listRecord.size();
        while (control > 0){

            parentList.clear();
            for (Map.Entry<String, CustomTimePeriod> item : listRecord.entrySet()) {
                if( item.getValue().getParentPeriodId()!= null && !parentList.containsKey(item.getValue().getParentPeriodId())){
                    parentList.put(item.getValue().getParentPeriodId(), customTimePeriodDao.get(item.getValue().getParentPeriodId()));
                }
            }

            listRecordTemp.clear();
            for (Map.Entry<String, CustomTimePeriod> item : listRecord.entrySet()) {
                listRecordTemp.put(item.getValue().getCustomTimePeriodId(), customTimePeriodDao.get(item.getValue().getCustomTimePeriodId()));
            }

            if(listRecord.keySet().equals(parentList.keySet())){
                Validators.assertFalse(true, Messages.CANNOT_DELETE_RECORDS);
                control = 0;
            }
            for (Map.Entry<String, CustomTimePeriod> item : listRecord.entrySet()) {
                String id = item.getValue().getCustomTimePeriodId();
                if(!isInList(id, parentList)){
                    Validators.assertNotBlank(id, Messages.CUSTOM_TIME_PERIOD_ID_REQUIRED);
                    CustomTimePeriod record = customTimePeriodDao.get(id);
                    Validators.assertNotNull(record, Messages.INVALID_CUSTOM_TIME_PERIOD);
                    customTimePeriodDao.delete(id);
                    listRecordTemp.remove(id);
                    control--;
                }
            }
            listRecord.clear();
            for (Map.Entry<String, CustomTimePeriod> item : listRecordTemp.entrySet()) {
                listRecord.put(item.getValue().getCustomTimePeriodId(), customTimePeriodDao.get(item.getValue().getCustomTimePeriodId()));
            }
        }
        return true;
    }

    private boolean isInList(String id,  Map<String, CustomTimePeriod> records) {
        for (Map.Entry<String, CustomTimePeriod> item : records.entrySet()) {
            if (id.equals(item.getValue().getCustomTimePeriodId())) return true;
        }

        return false;
    }

    public CustomTimePeriod getCustomTimePeriodForIndicatorMovement(String acctgTransId, String acctgTransEntrySeqId) {
        return this.customTimePeriodDao.getCustomTimePeriodForIndicatorMovement(acctgTransId, acctgTransEntrySeqId);
    }
}
