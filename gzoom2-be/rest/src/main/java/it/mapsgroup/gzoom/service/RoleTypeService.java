package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.RoleTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.RoleType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RoleTypeService {
	private static final Logger LOG = getLogger(RoleTypeService.class);
	
	private final RoleTypeDao roleTypeDao;

    @Autowired
    public RoleTypeService(RoleTypeDao roleTypeDao) {
        this.roleTypeDao = roleTypeDao;
    }

    public Result<RoleType> getRoleTypes() {
        List<RoleType> list = roleTypeDao.getRoleTypes();
        return new Result<>(list, list.size());
    }

    public Result<RoleType> getRoleTypeByParentTypeId(String parentTypeId) {
        List<RoleType> list = this.roleTypeDao.getRoleTypeByParentTypeId(parentTypeId);
        return new Result<>(list, list.size());
    }

    public Result<RoleType> getRoleTypeByOUAndLikeGOAL() {
        List<RoleType> list = this.roleTypeDao.getRoleTypeByOUAndLikeGOAL();
        return new Result<>(list, list.size());
    }

    public boolean updateRoleType(RoleType roleType) {
        Validators.assertNotNull(roleType.getRoleTypeId(), Messages.ROLE_TYPE_ID_REQUIRED);
        RoleType record = roleTypeDao.getRoleType(roleType.getRoleTypeId());
        Validators.assertNotNull(record, Messages.INVALID_ROLE_TYPE);
        return roleTypeDao.update(roleType, principal().getUserLoginId());
    }

    public boolean deleteRoleType(String[] roleTypes) {
        Map<String, RoleType> listRecord = new HashMap<>();
        Map<String, RoleType> listRecordTemp = new HashMap<>();
        Map<String, RoleType> parentList = new HashMap<>();

        for (String i : roleTypes) {
            Validators.assertNotBlank(i, Messages.CUSTOM_TIME_PERIOD_ID_REQUIRED);
            RoleType record = roleTypeDao.getRoleType(i);
            Validators.assertNotNull(record, Messages.INVALID_CUSTOM_TIME_PERIOD);

            listRecord.put(record.getRoleTypeId(),record);
        }

        int control = listRecord.size();
        while (control > 0){

            parentList.clear();
            for (Map.Entry<String, RoleType> item : listRecord.entrySet()) {
                if( item.getValue().getParentTypeId()!= null && !parentList.containsKey(item.getValue().getParentTypeId())){
                    parentList.put(item.getValue().getParentTypeId(), roleTypeDao.getRoleType(item.getValue().getParentTypeId()));
                }
            }

            listRecordTemp.clear();
            for (Map.Entry<String, RoleType> item : listRecord.entrySet()) {
                listRecordTemp.put(item.getValue().getRoleTypeId(), roleTypeDao.getRoleType(item.getValue().getRoleTypeId()));
            }

            if(listRecord.keySet().equals(parentList.keySet())){
                Validators.assertFalse(true, Messages.CANNOT_DELETE_RECORDS);
                control = 0;
            }
            for (Map.Entry<String, RoleType> item : listRecord.entrySet()) {
                String id = item.getValue().getRoleTypeId();
                if(!isInList(id, parentList)){
                    Validators.assertNotBlank(id, Messages.EMPL_POSITION_TYPE_ID_REQUIRED);
                    RoleType record = roleTypeDao.getRoleType(id);
                    Validators.assertNotNull(record, Messages.INVALID_EMPL_POSITION_TYPE);
                    roleTypeDao.delete(id);
                    listRecordTemp.remove(id);
                    control--;
                }
            }
            listRecord.clear();
            for (Map.Entry<String, RoleType> item : listRecordTemp.entrySet()) {
                listRecord.put(item.getValue().getRoleTypeId(), roleTypeDao.getRoleType(item.getValue().getRoleTypeId()));
            }
        }
        return true;
    }

    private boolean isInList(String id,  Map<String, RoleType> records) {
        for (Map.Entry<String, RoleType> item : records.entrySet()) {
            if (id.equals(item.getValue().getRoleTypeId())) return true;
        }

        return false;
    }

    public String createRoleType(RoleType roleType) {
        Validators.assertNotNull(roleType, Messages.ROLE_TYPE_REQUIRED);
        Validators.assertNotBlank(roleType.getRoleTypeId(), Messages.ROLE_TYPE_ID_REQUIRED);
        Validators.assertNotBlank(roleType.getDescription(), Messages.ROLE_TYPE_DESCRIPTION_REQUIRED);
        Validators.assertNotBlank(roleType.getDescription(), Messages.ROLE_TYPE_SHORT_LABEL_REQUIRED);

        roleTypeDao.create(roleType, principal().getUserLoginId());
        return roleType.getRoleTypeId();
    }
}
