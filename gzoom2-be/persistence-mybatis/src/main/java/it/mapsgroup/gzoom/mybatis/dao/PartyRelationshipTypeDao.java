package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.PartyRelationshipType;
import it.mapsgroup.gzoom.mybatis.mapper.PartyRelationshipTypeMapper;
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
public class PartyRelationshipTypeDao extends AbstractDao {
    private static final Logger LOG = getLogger(PartyRelationshipTypeDao.class);
    private final PartyRelationshipTypeMapper partyRelationshipTypeMapper;

    @Autowired
    public PartyRelationshipTypeDao(PartyRelationshipTypeMapper partyRelationshipTypeMapper) {
        this.partyRelationshipTypeMapper = partyRelationshipTypeMapper;
    }

    @Transactional
    public PartyRelationshipType findById(String partyRelationshipTypeId) {
        LOG.info("find partyRelationshipType by id");


        PartyRelationshipType  partyRelationshipType = this.partyRelationshipTypeMapper.selectByPrimaryKey(partyRelationshipTypeId);
        LOG.info("PartyRelationshipType = {}", (partyRelationshipType != null));
        return partyRelationshipType;
    }

    @Transactional
    public List<PartyRelationshipType> findAllOrderById() {
        LOG.info("find all partyRelationshipType");


        List<PartyRelationshipType> partyRelationshipTypes = this.partyRelationshipTypeMapper.selectAllOrderById();
        LOG.info("size = {}", partyRelationshipTypes.size());
        return partyRelationshipTypes;
    }

    @Transactional
    public boolean create(PartyRelationshipType partyRelationshipType, String userLoginId) {
        LOG.info("create partyRelationshipType");
        setCreatedTimestamp(partyRelationshipType);
        setCreatedByUserLogin(partyRelationshipType, userLoginId);
        int result = this.partyRelationshipTypeMapper.insert(partyRelationshipType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean update(PartyRelationshipType partyRelationshipType, String userLoginId) {
        LOG.info("update partyRelationshipType");
        setUpdateTimestamp(partyRelationshipType);
        setLastModifiedByUserLogin(partyRelationshipType, userLoginId);
        int result = this.partyRelationshipTypeMapper.updateByPrimaryKey(partyRelationshipType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String partyRelationshipTypeId) {
        LOG.info("delete partyRelationshipType");
        int result = this.partyRelationshipTypeMapper.deleteByPrimaryKey(partyRelationshipTypeId);
        LOG.info("result = {}", result);
        return result > 0;
    }

    private void setCreatedByUserLogin(PartyRelationshipType record, String userLoginId) {
        record.setCreatedByUserLogin(userLoginId);
    }

    private void setLastModifiedByUserLogin(PartyRelationshipType record, String userLoginId) {
        record.setLastModifiedByUserLogin(userLoginId);
    }

}
