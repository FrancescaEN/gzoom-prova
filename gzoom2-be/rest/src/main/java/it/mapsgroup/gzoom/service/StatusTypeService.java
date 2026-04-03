package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.StatusItemDao;
import it.mapsgroup.gzoom.mybatis.dao.StatusTypeDao;
import it.mapsgroup.gzoom.mybatis.dao.StatusValidChangeDao;
import it.mapsgroup.gzoom.mybatis.dto.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class StatusTypeService {
    private final StatusTypeDao statusTypeDao;
    private final StatusItemDao statusItemDao;
    private final StatusValidChangeDao statusValidChangeDao;

    @Autowired
    public StatusTypeService(StatusTypeDao statusTypeDao, StatusItemDao statusItemDao, StatusValidChangeDao statusValidChangeDao) {
        this.statusTypeDao = statusTypeDao;
        this.statusItemDao = statusItemDao;
        this.statusValidChangeDao = statusValidChangeDao;
    }

    public Result<StatusType> getStatusTypeList() {
        List<StatusType> list = this.statusTypeDao.getStatusTypes();
        return new Result<>(list, list.size());
    }

    public StatusType getStatusTypeById(String statusTypeId) {
        StatusType statusType = this.statusTypeDao.getStatusTypeById(statusTypeId);
        return statusType;
    }

    public boolean createStatusType(StatusType req) {
        this.validateStatusType(req, "CREATE");
        return this.statusTypeDao.create(req, principal().getUserLoginId());
    }

    public boolean updateStatusType(StatusType req) {
        this.validateStatusType(req, "UPDATE");
        return this.statusTypeDao.update(req, principal().getUserLoginId());
    }

    @Transactional
    public boolean deleteStatusType(String[] idList) {
        Map<String, StatusType> listRecord = new HashMap<>();
        Map<String, StatusType> listRecordTemp = new HashMap<>();
        Map<String, StatusType> parentList = new HashMap<>();
        Messages msg = new Messages();

        for (String i : idList) {
            Validators.assertNotBlank(i, msg.getMessageColumn(Messages.STATUS_TYPE, Messages.STATUS_TYPE_ID, Messages.IS_REQUIRED));
            StatusType record = this.statusTypeDao.getStatusType(i);
            Validators.assertFalse(record == null, msg.getMessageColumn(Messages.STATUS_TYPE, Messages.STATUS_TYPE_ID, Messages.INVALID));

            listRecord.put(record.getStatusTypeId(),record);
        }

        int control = listRecord.size();
        while (control > 0){

            parentList.clear();
            for (Map.Entry<String, StatusType> item : listRecord.entrySet()) {
                if( item.getValue().getParentTypeId()!= null && !parentList.containsKey(item.getValue().getParentTypeId())){
                    parentList.put(item.getValue().getParentTypeId(), this.statusTypeDao.getStatusType(item.getValue().getParentTypeId()));
                }
            }

            listRecordTemp.clear();
            for (Map.Entry<String, StatusType> item : listRecord.entrySet()) {
                listRecordTemp.put(item.getValue().getStatusTypeId(), this.statusTypeDao.getStatusType(item.getValue().getStatusTypeId()));
            }

            if(listRecord.keySet().equals(parentList.keySet())){
                Validators.assertFalse(true, Messages.CANNOT_DELETE_RECORDS);
                control = 0;
            }
            for (Map.Entry<String, StatusType> item : listRecord.entrySet()) {
                String id = item.getValue().getStatusTypeId();
                if(!isInList(id, parentList)){
                    Validators.assertNotBlank(id, msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_TYPE, Messages.PARTY_RELATIONSHIP_TYPE_ID, Messages.IS_REQUIRED));
                    StatusType record = this.statusTypeDao.getStatusType(id);
                    Validators.assertFalse(record == null, msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_TYPE, Messages.PARTY_RELATIONSHIP_TYPE_ID, Messages.INVALID));
                    this.statusValidChangeDao.deleteByStatusTypeId(id);
                    this.statusItemDao.deleteByStatusTypeId(id);
                    this.statusTypeDao.delete(id);
                    listRecordTemp.remove(id);
                    control--;
                }
            }
            listRecord.clear();
            for (Map.Entry<String, StatusType> item : listRecordTemp.entrySet()) {
                listRecord.put(item.getValue().getStatusTypeId(), this.statusTypeDao.getStatusType(item.getValue().getStatusTypeId()));
            }
        }
        return true;
    }

    private boolean isInList(String id,  Map<String, StatusType> records) {
        for (Map.Entry<String, StatusType> item : records.entrySet()) {
            if (id.equals(item.getValue().getStatusTypeId())) return true;
        }

        return false;
    }
    private void validateStatusType(StatusType req, String method) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.STATUS_TYPE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getStatusTypeId(), msg.getMessageColumn(Messages.STATUS_TYPE, Messages.STATUS_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getDescription(), msg.getMessageColumn(Messages.STATUS_TYPE, Messages.DESCRIPTION, Messages.IS_REQUIRED));
        StatusType record = this.statusTypeDao.getStatusType(req.getStatusTypeId());
        if ( method.equalsIgnoreCase("UPDATE")) {
            Validators.assertFalse(record == null, msg.getMessageColumn(Messages.STATUS_TYPE, Messages.STATUS_TYPE_ID, Messages.INVALID));
        }
        if ( method.equalsIgnoreCase("CREATE")) {
            Validators.assertFalse(record != null, msg.getMessageColumn(Messages.STATUS_TYPE, Messages.STATUS_TYPE_ID, Messages.EXISTING));
        }

    }
}
