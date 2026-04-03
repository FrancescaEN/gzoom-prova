package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.PartyRelationshipRole;
import it.mapsgroup.gzoom.mybatis.mapper.PartyRelationshipRoleMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class PartyRelationshipRoleDao extends AbstractDao {
    private static final Logger LOG = getLogger(PartyRelationshipRoleDao.class);
    private final PartyRelationshipRoleMapper partyRelationshipRoleMapper;

    @Autowired
    public PartyRelationshipRoleDao(PartyRelationshipRoleMapper partyRelationshipRoleMapper) {
        this.partyRelationshipRoleMapper = partyRelationshipRoleMapper;
    }

    @Transactional
    public PartyRelationshipRole findById(String partyRelationshipTypeId, String roleTypeValidFrom, String roleTypeValidTo) {
        LOG.info("find partyRelationshipRole by id");


        PartyRelationshipRole partyRelationshipRole = this.partyRelationshipRoleMapper.selectByPrimaryKey(partyRelationshipTypeId, roleTypeValidFrom, roleTypeValidTo);
        LOG.info("PartyRelationshipRole = {}", (partyRelationshipRole != null));
        return partyRelationshipRole;
    }

    @Transactional
    public List<PartyRelationshipRole> findByPartyRelationshipTypeId(String partyRelationshipTypeId) {
        LOG.info("find partyRelationshipRole by partyRelationshipTypeId");


        List<PartyRelationshipRole> partyRelationshipRoles = this.partyRelationshipRoleMapper.selectByPartyRelationshipTypeId(partyRelationshipTypeId);
        LOG.info("size = {}", partyRelationshipRoles.size());
        return partyRelationshipRoles;
    }

    @Transactional
    public boolean create(PartyRelationshipRole partyRelationshipRole, String userLoginId) {
        LOG.info("create partyRelationshipRole");
        setCreatedTimestamp(partyRelationshipRole);
        setCreatedByUserLogin(partyRelationshipRole, userLoginId);
        int result = this.partyRelationshipRoleMapper.insert(partyRelationshipRole);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean update(PartyRelationshipRole partyRelationshipRole, String userLoginId) {
        LOG.info("update partyRelationshipRole");
        setUpdateTimestamp(partyRelationshipRole);
        setLastModifiedByUserLogin(partyRelationshipRole, userLoginId);
        int result = this.partyRelationshipRoleMapper.updateByPrimaryKey(partyRelationshipRole);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String partyRelationshipTypeId, String roleTypeValidFrom, String roleTypeValidTo) {
        LOG.info("delete partyRelationshipRole");
        int result = this.partyRelationshipRoleMapper.deleteByPrimaryKey(partyRelationshipTypeId, roleTypeValidFrom, roleTypeValidTo);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean deleteByPartyRelationshipTypeId(String partyRelationshipTypeId) {
        LOG.info("delete partyRelationshipRole by partyRelationshipTypeId");
        int result = this.partyRelationshipRoleMapper.deleteByPartyRelationshipTypeId(partyRelationshipTypeId);
        LOG.info("result = {}", result);
        return result > 0;
    }

    private void setCreatedByUserLogin(PartyRelationshipRole record, String userLoginId) {
        record.setCreatedByUserLogin(userLoginId);
    }

    private void setLastModifiedByUserLogin(PartyRelationshipRole record, String userLoginId) {
        record.setLastModifiedByUserLogin(userLoginId);
    }
}
