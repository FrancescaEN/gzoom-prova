package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.PartyRelationshipTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.PartyRelationshipType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class PartyRelationshipTypeService {

    private final PartyRelationshipTypeDao partyRelationshipTypeDao;
    private final PartyRelationshipRoleService partyRelationshipRoleService;

    @Autowired
    public PartyRelationshipTypeService(PartyRelationshipTypeDao partyRelationshipTypeDao, PartyRelationshipRoleService partyRelationshipRoleService) {
        this.partyRelationshipTypeDao = partyRelationshipTypeDao;
        this.partyRelationshipRoleService = partyRelationshipRoleService;
    }

    public Result<PartyRelationshipType> findAllOrderById() {
        List<PartyRelationshipType> list = this.partyRelationshipTypeDao.findAllOrderById();
        return new Result<>(list, list.size());
    }

    public boolean create(PartyRelationshipType partyRelationshipType) {
        this.validatePartyRelationshipType(partyRelationshipType, "CREATE");
        return this.partyRelationshipTypeDao.create(partyRelationshipType, principal().getUserLoginId());
    }

    public boolean update(PartyRelationshipType partyRelationshipType) {
        this.validatePartyRelationshipType(partyRelationshipType, "UPDATE");
        return this.partyRelationshipTypeDao.update(partyRelationshipType, principal().getUserLoginId());
    }

    @Transactional
    public boolean delete(String partyRelationshipTypeId) {
        Messages msg = new Messages();
        Validators.assertNotBlank(partyRelationshipTypeId, msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_TYPE, Messages.PARTY_RELATIONSHIP_TYPE_ID, Messages.IS_REQUIRED));
        PartyRelationshipType record = this.partyRelationshipTypeDao.findById(partyRelationshipTypeId);
        Validators.assertFalse(record == null, msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_TYPE, Messages.PARTY_RELATIONSHIP_TYPE_ID, Messages.INVALID));

        this.partyRelationshipRoleService.deleteByPartyRelationshipTypeId(partyRelationshipTypeId);
        return this.partyRelationshipTypeDao.delete(partyRelationshipTypeId);
    }

    @Transactional
    public boolean delete(String[] idList) {
        Map<String, PartyRelationshipType> listRecord = new HashMap<>();
        Map<String, PartyRelationshipType> listRecordTemp = new HashMap<>();
        Map<String, PartyRelationshipType> parentList = new HashMap<>();
        Messages msg = new Messages();

        for (String i : idList) {
            Validators.assertNotBlank(i, msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_TYPE, Messages.PARTY_RELATIONSHIP_TYPE_ID, Messages.IS_REQUIRED));
            PartyRelationshipType record = this.partyRelationshipTypeDao.findById(i);
            Validators.assertFalse(record == null, msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_TYPE, Messages.PARTY_RELATIONSHIP_TYPE_ID, Messages.INVALID));

            listRecord.put(record.getPartyRelationshipTypeId(),record);
        }

        int control = listRecord.size();
        while (control > 0){

            parentList.clear();
            for (Map.Entry<String, PartyRelationshipType> item : listRecord.entrySet()) {
                if( item.getValue().getParentTypeId()!= null && !parentList.containsKey(item.getValue().getParentTypeId())){
                    parentList.put(item.getValue().getParentTypeId(), partyRelationshipTypeDao.findById(item.getValue().getParentTypeId()));
                }
            }

            listRecordTemp.clear();
            for (Map.Entry<String, PartyRelationshipType> item : listRecord.entrySet()) {
                listRecordTemp.put(item.getValue().getPartyRelationshipTypeId(), partyRelationshipTypeDao.findById(item.getValue().getPartyRelationshipTypeId()));
            }

            if(listRecord.keySet().equals(parentList.keySet())){
                Validators.assertFalse(true, Messages.CANNOT_DELETE_RECORDS);
                control = 0;
            }
            for (Map.Entry<String, PartyRelationshipType> item : listRecord.entrySet()) {
                String id = item.getValue().getPartyRelationshipTypeId();
                if(!isInList(id, parentList)){
                    Validators.assertNotBlank(id, msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_TYPE, Messages.PARTY_RELATIONSHIP_TYPE_ID, Messages.IS_REQUIRED));
                    PartyRelationshipType record = partyRelationshipTypeDao.findById(id);
                    Validators.assertFalse(record == null, msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_TYPE, Messages.PARTY_RELATIONSHIP_TYPE_ID, Messages.INVALID));
                    this.partyRelationshipRoleService.deleteByPartyRelationshipTypeId(id);
                    partyRelationshipTypeDao.delete(id);
                    listRecordTemp.remove(id);
                    control--;
                }
            }
            listRecord.clear();
            for (Map.Entry<String, PartyRelationshipType> item : listRecordTemp.entrySet()) {
                listRecord.put(item.getValue().getPartyRelationshipTypeId(), partyRelationshipTypeDao.findById(item.getValue().getPartyRelationshipTypeId()));
            }
        }
        return true;
    }

    private boolean isInList(String id,  Map<String, PartyRelationshipType> records) {
        for (Map.Entry<String, PartyRelationshipType> item : records.entrySet()) {
            if (id.equals(item.getValue().getPartyRelationshipTypeId())) return true;
        }

        return false;
    }

    private void validatePartyRelationshipType(PartyRelationshipType partyRelationshipType, String method) {
        Messages msg = new Messages();
        Validators.assertNotNull(partyRelationshipType, msg.getMessageTable(Messages.PARTY_RELATIONSHIP_TYPE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(partyRelationshipType.getPartyRelationshipTypeId(), msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_TYPE, Messages.PARTY_RELATIONSHIP_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(partyRelationshipType.getPartyRelationshipName(), msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_TYPE, Messages.PARTY_RELATIONSHIP_TYPE_NAME, Messages.IS_REQUIRED));

        PartyRelationshipType record = this.partyRelationshipTypeDao.findById(partyRelationshipType.getPartyRelationshipTypeId());

        if ( method.equalsIgnoreCase("CREATE")) {
            Validators.assertTrue(record == null, msg.getMessageTable(Messages.PARTY_RELATIONSHIP_TYPE, Messages.EXISTING));
        }
        if ( method.equalsIgnoreCase("UPDATE")) {
            Validators.assertFalse(record == null, msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_TYPE, Messages.PARTY_RELATIONSHIP_TYPE_ID, Messages.INVALID));
        }

    }
}
