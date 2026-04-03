package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.PartyRelationshipRoleDao;
import it.mapsgroup.gzoom.mybatis.dto.PartyRelationshipRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class PartyRelationshipRoleService {

    private final PartyRelationshipRoleDao partyRelationshipRoleDao;

    @Autowired
    public PartyRelationshipRoleService(PartyRelationshipRoleDao partyRelationshipRoleDao) {
        this.partyRelationshipRoleDao = partyRelationshipRoleDao;
    }

    public Result<PartyRelationshipRole> findByPartyRelationshipTypeId(String partyRelationshipTypeId) {
        List<PartyRelationshipRole> list = this.partyRelationshipRoleDao.findByPartyRelationshipTypeId(partyRelationshipTypeId);
        return new Result<>(list, list.size());
    }

    public boolean create(PartyRelationshipRole partyRelationshipRole) {
        this.validatePartyRelationshipRole(partyRelationshipRole, "CREATE");
        return this.partyRelationshipRoleDao.create(partyRelationshipRole, principal().getUserLoginId());
    }

    public boolean update(PartyRelationshipRole partyRelationshipRole) {
        this.validatePartyRelationshipRole(partyRelationshipRole, "UPDATE");
        return this.partyRelationshipRoleDao.update(partyRelationshipRole, principal().getUserLoginId());
    }

    public boolean delete(String partyRelationshipTypeId, String roleTypeValidFrom, String roleTypeValidTo) {
        Messages msg = new Messages();
        Validators.assertNotBlank(partyRelationshipTypeId, msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_ROLE, Messages.PARTY_RELATIONSHIP_TYPE_ID, Messages.IS_REQUIRED));
        PartyRelationshipRole record = this.partyRelationshipRoleDao.findById(partyRelationshipTypeId, roleTypeValidFrom, roleTypeValidTo);
        Validators.assertFalse(record == null, msg.getMessageTable(Messages.PARTY_RELATIONSHIP_ROLE, Messages.INVALID));
        return this.partyRelationshipRoleDao.delete(partyRelationshipTypeId, roleTypeValidFrom, roleTypeValidTo);
    }

    public boolean deleteByPartyRelationshipTypeId(String partyRelationshipTypeId) {
        Messages msg = new Messages();
        Validators.assertNotBlank(partyRelationshipTypeId, msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_ROLE, Messages.PARTY_RELATIONSHIP_TYPE_ID, Messages.IS_REQUIRED));
        return this.partyRelationshipRoleDao.deleteByPartyRelationshipTypeId(partyRelationshipTypeId);
    }

    private void validatePartyRelationshipRole(PartyRelationshipRole partyRelationshipRole, String method) {
        Messages msg = new Messages();
        Validators.assertNotNull(partyRelationshipRole, msg.getMessageTable(Messages.PARTY_RELATIONSHIP_ROLE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(partyRelationshipRole.getPartyRelationshipTypeId(), msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_ROLE, Messages.PARTY_RELATIONSHIP_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(partyRelationshipRole.getRoleTypeValidFrom(), msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_ROLE, Messages.ROLE_TYPE_VALID_FROM, Messages.IS_REQUIRED));
        Validators.assertNotBlank(partyRelationshipRole.getRoleTypeValidTo(), msg.getMessageColumn(Messages.PARTY_RELATIONSHIP_ROLE, Messages.ROLE_TYPE_VALID_TO, Messages.IS_REQUIRED));

        PartyRelationshipRole record = this.partyRelationshipRoleDao.findById(partyRelationshipRole.getPartyRelationshipTypeId(), partyRelationshipRole.getRoleTypeValidFrom(), partyRelationshipRole.getRoleTypeValidTo());

        if ( method.equalsIgnoreCase("CREATE")) {
            Validators.assertTrue(record == null, msg.getMessageTable(Messages.PARTY_RELATIONSHIP_ROLE, Messages.EXISTING));
        }
        if ( method.equalsIgnoreCase("UPDATE")) {
            Validators.assertFalse(record == null, msg.getMessageTable(Messages.PARTY_RELATIONSHIP_ROLE, Messages.INVALID));
        }

    }
}
