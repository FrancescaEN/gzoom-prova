package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortPurposeTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPurposeType;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPurposeTypeEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static it.mapsgroup.gzoom.security.Principals.principal;

/**
 * @author Leonardo Minaudo
 */
@Service
public class WorkEffortPurposeTypeService {

    private final Configuration config;
    private final WorkEffortPurposeTypeDao workEffortPurposeTypeDao;

    @Autowired
    public WorkEffortPurposeTypeService(Configuration config, WorkEffortPurposeTypeDao workEffortPurposeTypeDao) {
        this.config = config;
        this.workEffortPurposeTypeDao = workEffortPurposeTypeDao;
    }

    public Result<WorkEffortPurposeType> getWorkEffortPurposeTypeList() {
        List<WorkEffortPurposeType> list = this.workEffortPurposeTypeDao.getWorkEffortPurposeTypeList();
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortPurposeType> getWorkEffortPurposeTypeListByPurposeTypeEnumId(String purposeTypeEnumId) {
        List<WorkEffortPurposeType> list = this.workEffortPurposeTypeDao.selectByPurposeTypeEnumId(purposeTypeEnumId);
        return new Result<>(list, list.size());
    }

    public Result<WorkEffortPurposeTypeEx> getPurposeTabType(String glAccountId, boolean in) {
        List<WorkEffortPurposeTypeEx> list = this.workEffortPurposeTypeDao.getPurposeTabType(glAccountId, in);
        return new Result<>(list, list.size());
    }

    public boolean createWorkEffortPurposeType(WorkEffortPurposeType req) {
        Validators.assertNotNull(req, Messages.WORK_EFFORT_PURPOSE_TYPE_REQUIRED);
        Validators.assertNotBlank(req.getWorkEffortPurposeTypeId(), Messages.WORK_EFFORT_PURPOSE_TYPE_ID_REQUIRED);
        WorkEffortPurposeType record = workEffortPurposeTypeDao.get(req.getWorkEffortPurposeTypeId());
        Validators.assertTrue(record == null, Messages.WORK_EFFORT_PURPOSE_TYPE_ID_EXIST);
        Validators.assertNotBlank(req.getWorkEffortPurposeTypeCode(), Messages.WORK_EFFORT_PURPOSE_TYPE_CODE_REQUIRED);
        Validators.assertNotBlank(req.getDescription(), Messages.WORK_EFFORT_PURPOSE_TYPE_DESCRIPTION_REQUIRED);
        Validators.assertNotBlank(req.getPurposeTypeEnumId(), Messages.WORK_EFFORT_PURPOSE_TYPE_PURPOSE_TYPE_ENUM_ID_REQUIRED);

        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getDescriptionLang(), Messages.WORK_EFFORT_PURPOSE_TYPE_DESCRIPTION_LANG_REQUIRED);
        }

        return workEffortPurposeTypeDao.create(req, principal().getUserLoginId());
    }

    public boolean updateWorkEffortPurposeType(WorkEffortPurposeType req) {
        Validators.assertNotNull(req, Messages.WORK_EFFORT_PURPOSE_TYPE_REQUIRED);
        Validators.assertNotBlank(req.getWorkEffortPurposeTypeId(), Messages.WORK_EFFORT_PURPOSE_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(req.getWorkEffortPurposeTypeCode(), Messages.WORK_EFFORT_PURPOSE_TYPE_CODE_REQUIRED);
        Validators.assertNotBlank(req.getDescription(), Messages.WORK_EFFORT_PURPOSE_TYPE_DESCRIPTION_REQUIRED);
        Validators.assertNotBlank(req.getPurposeTypeEnumId(), Messages.WORK_EFFORT_PURPOSE_TYPE_PURPOSE_TYPE_ENUM_ID_REQUIRED);

        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getDescriptionLang(), Messages.WORK_EFFORT_PURPOSE_TYPE_DESCRIPTION_LANG_REQUIRED);
        }

        WorkEffortPurposeType record = workEffortPurposeTypeDao.get(req.getWorkEffortPurposeTypeId());
        Validators.assertNotNull(record, Messages.INVALID_WORK_EFFORT_PURPOSE_TYPE);
        return workEffortPurposeTypeDao.update(req, principal().getUserLoginId());
    }

    public boolean deleteWorkEffortPurposeType(String[] idList) {
        Map<String, WorkEffortPurposeType> listRecord = new HashMap<>();
        Map<String, WorkEffortPurposeType> listRecordTemp = new HashMap<>();
        Map<String, WorkEffortPurposeType> parentList = new HashMap<>();

        for (String i : idList) {
            Validators.assertNotBlank(i, Messages.CUSTOM_TIME_PERIOD_ID_REQUIRED);
            WorkEffortPurposeType record = workEffortPurposeTypeDao.get(i);
            Validators.assertNotNull(record, Messages.INVALID_CUSTOM_TIME_PERIOD);

            listRecord.put(record.getWorkEffortPurposeTypeId(),record);
        }

        int control = listRecord.size();
        while (control > 0){

            parentList.clear();
            for (Map.Entry<String, WorkEffortPurposeType> item : listRecord.entrySet()) {
                if( item.getValue().getParentTypeId()!= null && !parentList.containsKey(item.getValue().getParentTypeId())){
                    parentList.put(item.getValue().getParentTypeId(), workEffortPurposeTypeDao.get(item.getValue().getParentTypeId()));
                }
            }

            listRecordTemp.clear();
            for (Map.Entry<String, WorkEffortPurposeType> item : listRecord.entrySet()) {
                listRecordTemp.put(item.getValue().getWorkEffortPurposeTypeId(), workEffortPurposeTypeDao.get(item.getValue().getWorkEffortPurposeTypeId()));
            }

            if(listRecord.keySet().equals(parentList.keySet())){
                Validators.assertFalse(true, Messages.CANNOT_DELETE_RECORDS);
                control = 0;
            }
            for (Map.Entry<String, WorkEffortPurposeType> item : listRecord.entrySet()) {
                String id = item.getValue().getWorkEffortPurposeTypeId();
                if(!isInList(id, parentList)){
                    Validators.assertNotBlank(id, Messages.EMPL_POSITION_TYPE_ID_REQUIRED);
                    WorkEffortPurposeType record = workEffortPurposeTypeDao.get(id);
                    Validators.assertNotNull(record, Messages.INVALID_EMPL_POSITION_TYPE);
                    workEffortPurposeTypeDao.delete(id);
                    listRecordTemp.remove(id);
                    control--;
                }
            }
            listRecord.clear();
            for (Map.Entry<String, WorkEffortPurposeType> item : listRecordTemp.entrySet()) {
                listRecord.put(item.getValue().getWorkEffortPurposeTypeId(), workEffortPurposeTypeDao.get(item.getValue().getWorkEffortPurposeTypeId()));
            }
        }
        return true;
    }

    private boolean isInList(String id,  Map<String, WorkEffortPurposeType> records) {
        for (Map.Entry<String, WorkEffortPurposeType> item : records.entrySet()) {
            if (id.equals(item.getValue().getWorkEffortPurposeTypeId())) return true;
        }

        return false;
    }
}
