package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.EmplPositionTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.EmplPositionType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * EmplPositionType service.
 *
 */
@Service
public class EmplPositionTypeService{

    private static final Logger LOG = getLogger(EmplPositionTypeService.class);

    private final EmplPositionTypeDao emplPositionTypeDao;

    @Autowired
    public EmplPositionTypeService( EmplPositionTypeDao emplPositionTypeDao) {
        this.emplPositionTypeDao = emplPositionTypeDao;

    }

    public Result<EmplPositionType> getEmplPositionTypes() {
        List<EmplPositionType> list = emplPositionTypeDao.selectAllOrderByPrimaryKey();
        return new Result<>(list, list.size());
    }

    public boolean updateEmplPositionType(EmplPositionType emplPositionType) {
        Validators.assertNotNull(emplPositionType.getEmplPositionTypeId(), Messages.EMPL_POSITION_TYPE_ID_REQUIRED);
        EmplPositionType record = emplPositionTypeDao.selectByPrimaryKey(emplPositionType.getEmplPositionTypeId());
        Validators.assertNotNull(record, Messages.INVALID_EMPL_POSITION_TYPE);
        return emplPositionTypeDao.update(emplPositionType, principal().getUserLoginId());
    }


    public boolean deleteEmplPositionType(String[] emplPositionTypes) {

        Map<String, EmplPositionType> listRecord = new HashMap<>();
        Map<String, EmplPositionType> listRecordTemp = new HashMap<>();
        Map<String, EmplPositionType> parentList = new HashMap<>();

        for (String i : emplPositionTypes) {
            Validators.assertNotBlank(i, Messages.CUSTOM_TIME_PERIOD_ID_REQUIRED);
            EmplPositionType record = emplPositionTypeDao.selectByPrimaryKey(i);
            Validators.assertNotNull(record, Messages.INVALID_CUSTOM_TIME_PERIOD);

            listRecord.put(record.getEmplPositionTypeId(),record);
        }

        int control = listRecord.size();
        while (control > 0){

            parentList.clear();
            for (Map.Entry<String, EmplPositionType> item : listRecord.entrySet()) {
                if( item.getValue().getParentTypeId()!= null && !parentList.containsKey(item.getValue().getParentTypeId())){
                    parentList.put(item.getValue().getParentTypeId(), emplPositionTypeDao.selectByPrimaryKey(item.getValue().getParentTypeId()));
                }
            }

            listRecordTemp.clear();
            for (Map.Entry<String, EmplPositionType> item : listRecord.entrySet()) {
                listRecordTemp.put(item.getValue().getEmplPositionTypeId(), emplPositionTypeDao.selectByPrimaryKey(item.getValue().getEmplPositionTypeId()));
            }

            if(listRecord.keySet().equals(parentList.keySet())){
                Validators.assertFalse(true, Messages.CANNOT_DELETE_RECORDS);
                control = 0;
            }
            for (Map.Entry<String, EmplPositionType> item : listRecord.entrySet()) {
                String id = item.getValue().getEmplPositionTypeId();
                if(!isInList(id, parentList)){
                    Validators.assertNotBlank(id, Messages.EMPL_POSITION_TYPE_ID_REQUIRED);
                    EmplPositionType record = emplPositionTypeDao.selectByPrimaryKey(id);
                    Validators.assertNotNull(record, Messages.INVALID_EMPL_POSITION_TYPE);
                    emplPositionTypeDao.delete(id);
                    listRecordTemp.remove(id);
                    control--;
                }
            }
            listRecord.clear();
            for (Map.Entry<String, EmplPositionType> item : listRecordTemp.entrySet()) {
                listRecord.put(item.getValue().getEmplPositionTypeId(), emplPositionTypeDao.selectByPrimaryKey(item.getValue().getEmplPositionTypeId()));
            }
        }
        return true;
    }

    public String createEmplPositionType(EmplPositionType emplPositionType) {
        Validators.assertNotNull(emplPositionType, Messages.EMPL_POSITION_TYPE_REQUIRED);
        Validators.assertNotBlank(emplPositionType.getEmplPositionTypeId(), Messages.EMPL_POSITION_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(emplPositionType.getDescription(), Messages.EMPL_POSITION_TYPE_DESCRIPTION_REQUIRED);

        emplPositionTypeDao.create(emplPositionType, principal().getUserLoginId());
        return emplPositionType.getEmplPositionTypeId();
    }

    private boolean isInList(String id,  Map<String, EmplPositionType> records) {
        for (Map.Entry<String, EmplPositionType> item : records.entrySet()) {
            if (id.equals(item.getValue().getEmplPositionTypeId())) return true;
        }

        return false;
    }
}
