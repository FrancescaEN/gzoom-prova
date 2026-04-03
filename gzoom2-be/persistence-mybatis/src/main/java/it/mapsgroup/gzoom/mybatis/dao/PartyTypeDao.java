package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.PartyType;
import it.mapsgroup.gzoom.mybatis.mapper.PartyTypeMapper;
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
public class PartyTypeDao extends AbstractDao{
    private static final Logger LOG = getLogger(PartyTypeDao.class);
    private final PartyTypeMapper partyTypeMapper;

    @Autowired
    public PartyTypeDao(PartyTypeMapper partyTypeMapper) {
        this.partyTypeMapper = partyTypeMapper;
    }

    @Transactional
    public List<PartyType> getPartyTypes() {
        LOG.info("find all partyType");

        List<PartyType> partyTypes = this.partyTypeMapper.selectAll();
        LOG.info("size = {}", partyTypes.size());
        return partyTypes;
    }
}
