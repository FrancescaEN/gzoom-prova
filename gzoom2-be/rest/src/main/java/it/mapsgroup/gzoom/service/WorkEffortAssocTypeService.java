package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortAssocTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssocType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Leonardo Minaudo.
 */
@Service
public class WorkEffortAssocTypeService {
    private static final Logger LOG = getLogger(WorkEffortAssocTypeService.class);

    private final WorkEffortAssocTypeDao workEffortAssocTypeDao;

    @Autowired
    public WorkEffortAssocTypeService(WorkEffortAssocTypeDao workEffortAssocTypeDao) {
        this.workEffortAssocTypeDao = workEffortAssocTypeDao;
    }

    public Result<WorkEffortAssocType> getWorkEffortAssocType() {
        List<WorkEffortAssocType> list = this.workEffortAssocTypeDao.selectAllOrderByPrimaryKey();
        return new Result<>(list, list.size());
    }

    public boolean createWorkEffortAssocType(WorkEffortAssocType req) {
        Validators.assertNotNull(req, Messages.WORK_EFFORT_ASSOC_TYPE_REQUIRED);
        Validators.assertNotBlank(req.getDescription(), Messages.WORK_EFFORT_ASSOC_TYPE_DESCRIPTION_REQUIRED);
        WorkEffortAssocType record = workEffortAssocTypeDao.get(req.getWorkEffortAssocTypeId());
        Validators.assertTrue(record == null, Messages.WORK_EFFORT_ASSOC_TYPE_ID_EXIST);

        return workEffortAssocTypeDao.create(req, principal().getUserLoginId());
    }

    public boolean updateWorkEffortAssocType(WorkEffortAssocType list) {
        Validators.assertNotBlank(list.getWorkEffortAssocTypeId(), Messages.WORK_EFFORT_ASSOC_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(list.getDescription(), Messages.WORK_EFFORT_ASSOC_TYPE_DESCRIPTION_REQUIRED);
        WorkEffortAssocType record = workEffortAssocTypeDao.get(list.getWorkEffortAssocTypeId());
        Validators.assertNotNull(record, Messages.INVALID_WORK_EFFORT_ASSOC_TYPE);
        return workEffortAssocTypeDao.update(list, principal().getUserLoginId());
    }

    public boolean deleteWorkEffortAssocType(String[] idList) {
        Map<String, WorkEffortAssocType> listRecord = new HashMap<>();
        Map<String, WorkEffortAssocType> listRecordTemp = new HashMap<>();
        Map<String, WorkEffortAssocType> parentList = new HashMap<>();

        for (String i : idList) {
            Validators.assertNotBlank(i, Messages.CUSTOM_TIME_PERIOD_ID_REQUIRED);
            WorkEffortAssocType record = workEffortAssocTypeDao.get(i);
            Validators.assertNotNull(record, Messages.INVALID_CUSTOM_TIME_PERIOD);

            listRecord.put(record.getWorkEffortAssocTypeId(),record);
        }

        int control = listRecord.size();
        while (control > 0){

            parentList.clear();
            for (Map.Entry<String, WorkEffortAssocType> item : listRecord.entrySet()) {
                if( item.getValue().getParentTypeId()!= null && !parentList.containsKey(item.getValue().getParentTypeId())){
                    parentList.put(item.getValue().getParentTypeId(), workEffortAssocTypeDao.get(item.getValue().getParentTypeId()));
                }
            }

            listRecordTemp.clear();
            for (Map.Entry<String, WorkEffortAssocType> item : listRecord.entrySet()) {
                listRecordTemp.put(item.getValue().getWorkEffortAssocTypeId(), workEffortAssocTypeDao.get(item.getValue().getWorkEffortAssocTypeId()));
            }

            if(listRecord.keySet().equals(parentList.keySet())){
                Validators.assertFalse(true, Messages.CANNOT_DELETE_RECORDS);
                control = 0;
            }
            for (Map.Entry<String, WorkEffortAssocType> item : listRecord.entrySet()) {
                String id = item.getValue().getWorkEffortAssocTypeId();
                if(!isInList(id, parentList)){
                    Validators.assertNotBlank(id, Messages.EMPL_POSITION_TYPE_ID_REQUIRED);
                    WorkEffortAssocType record = workEffortAssocTypeDao.get(id);
                    Validators.assertNotNull(record, Messages.INVALID_EMPL_POSITION_TYPE);
                    workEffortAssocTypeDao.delete(id);
                    listRecordTemp.remove(id);
                    control--;
                }
            }
            listRecord.clear();
            for (Map.Entry<String, WorkEffortAssocType> item : listRecordTemp.entrySet()) {
                listRecord.put(item.getValue().getWorkEffortAssocTypeId(), workEffortAssocTypeDao.get(item.getValue().getWorkEffortAssocTypeId()));
            }
        }
        return true;
    }

    private boolean isInList(String id,  Map<String, WorkEffortAssocType> records) {
        for (Map.Entry<String, WorkEffortAssocType> item : records.entrySet()) {
            if (id.equals(item.getValue().getWorkEffortAssocTypeId())) return true;
        }

        return false;
    }

}
