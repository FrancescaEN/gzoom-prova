package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortContentTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.mapsgroup.gzoom.security.Principals.principal;

/**
 * @author Leonardo Minaudo
 */
@Service
public class WorkEffortContentTypeService {

    private final Configuration config;
    private final WorkEffortContentTypeDao workEffortContentTypeDao;

    @Autowired
    public WorkEffortContentTypeService(Configuration config, WorkEffortContentTypeDao workEffortContentTypeDao) {
        this.config = config;
        this.workEffortContentTypeDao = workEffortContentTypeDao;
    }

    public Result<WorkEffortContentType> getWorkEffortContentTypeList() {
        List<WorkEffortContentType> list = this.workEffortContentTypeDao.getWorkEffortContentTypeList();
        return new Result<>(list, list.size());
    }

    public boolean createWorkEffortContentType(WorkEffortContentType req) {
        Validators.assertNotNull(req, Messages.WORK_EFFORT_CONTENT_TYPE_REQUIRED);
        Validators.assertNotBlank(req.getWorkEffortContentTypeId(), Messages.WORK_EFFORT_CONTENT_TYPE_ID_REQUIRED);
        WorkEffortContentType record = workEffortContentTypeDao.get(req.getWorkEffortContentTypeId());
        Validators.assertTrue(record == null, Messages.WORK_EFFORT_CONTENT_TYPE_ID_EXIST);
        Validators.assertNotBlank(req.getDescription(), Messages.WORK_EFFORT_CONTENT_TYPE_DESCRIPTION_REQUIRED);
        Validators.assertNotBlank(req.getContentTypeId(), Messages.WORK_EFFORT_CONTENT_TYPE_CONTENT_TYPE_ID_REQUIRED);

        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getDescriptionLang(), Messages.WORK_EFFORT_CONTENT_TYPE_DESCRIPTION_LANG_REQUIRED);

        }

        return workEffortContentTypeDao.create(req, principal().getUserLoginId());
    }

    public boolean updateWorkEffortContentType(WorkEffortContentType req) {
        Validators.assertNotNull(req, Messages.WORK_EFFORT_CONTENT_TYPE_REQUIRED);
        Validators.assertNotBlank(req.getWorkEffortContentTypeId(), Messages.WORK_EFFORT_CONTENT_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(req.getDescription(), Messages.WORK_EFFORT_CONTENT_TYPE_DESCRIPTION_REQUIRED);
        Validators.assertNotBlank(req.getContentTypeId(), Messages.WORK_EFFORT_CONTENT_TYPE_CONTENT_TYPE_ID_REQUIRED);

        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getDescriptionLang(), Messages.WORK_EFFORT_CONTENT_TYPE_DESCRIPTION_LANG_REQUIRED);

        }

        WorkEffortContentType record = workEffortContentTypeDao.get(req.getWorkEffortContentTypeId());
        Validators.assertNotNull(record, Messages.INVALID_WORK_EFFORT_CONTENT_TYPE);
        return workEffortContentTypeDao.update(req, principal().getUserLoginId());
    }

    public boolean deleteWorkEffortContentType(String[] idList) {
        Map<String, WorkEffortContentType> listRecord = new HashMap<>();
        Map<String, WorkEffortContentType> listRecordTemp = new HashMap<>();
        Map<String, WorkEffortContentType> parentList = new HashMap<>();

        for (String i : idList) {
            Validators.assertNotBlank(i, Messages.WORK_EFFORT_CONTENT_TYPE_ID_REQUIRED);
            WorkEffortContentType record = workEffortContentTypeDao.get(i);
            Validators.assertNotNull(record, Messages.INVALID_WORK_EFFORT_CONTENT_TYPE);

            listRecord.put(record.getWorkEffortContentTypeId(),record);
        }

        int control = listRecord.size();
        while (control > 0){

            parentList.clear();
            for (Map.Entry<String, WorkEffortContentType> item : listRecord.entrySet()) {
                if( item.getValue().getParentTypeId()!= null && !parentList.containsKey(item.getValue().getParentTypeId())){
                    parentList.put(item.getValue().getParentTypeId(), workEffortContentTypeDao.get(item.getValue().getParentTypeId()));
                }
            }

            listRecordTemp.clear();
            for (Map.Entry<String, WorkEffortContentType> item : listRecord.entrySet()) {
                listRecordTemp.put(item.getValue().getWorkEffortContentTypeId(), workEffortContentTypeDao.get(item.getValue().getWorkEffortContentTypeId()));
            }

            if(listRecord.keySet().equals(parentList.keySet())){
                Validators.assertFalse(true, Messages.CANNOT_DELETE_RECORDS);
                control = 0;
            }
            for (Map.Entry<String, WorkEffortContentType> item : listRecord.entrySet()) {
                String id = item.getValue().getWorkEffortContentTypeId();
                if(!isInList(id, parentList)){
                    Validators.assertNotBlank(id, Messages.WORK_EFFORT_CONTENT_TYPE_ID_REQUIRED);
                    WorkEffortContentType record = workEffortContentTypeDao.get(id);
                    Validators.assertNotNull(record, Messages.INVALID_WORK_EFFORT_CONTENT_TYPE);
                    workEffortContentTypeDao.delete(id);
                    listRecordTemp.remove(id);
                    control--;
                }
            }
            listRecord.clear();
            for (Map.Entry<String, WorkEffortContentType> item : listRecordTemp.entrySet()) {
                listRecord.put(item.getValue().getWorkEffortContentTypeId(), workEffortContentTypeDao.get(item.getValue().getWorkEffortContentTypeId()));
            }
        }
        return true;
    }

    private boolean isInList(String id,  Map<String, WorkEffortContentType> records) {
        for (Map.Entry<String, WorkEffortContentType> item : records.entrySet()) {
            if (id.equals(item.getValue().getWorkEffortContentTypeId())) return true;
        }

        return false;
    }

    public Result<WorkEffortContentType> getContentTypeList(String workEffortId) {
        List<WorkEffortContentType> list = this.workEffortContentTypeDao.getContentTypeList(workEffortId);
        return new Result<>(list, list.size());
    }
}
