package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.PartyContactMech;
import it.mapsgroup.gzoom.mybatis.mapper.PartyContactMechMapper;
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
public class PartyContactMechDao extends AbstractDao {
    private static final Logger LOG = getLogger(PartyContactMechDao.class);
    private final PartyContactMechMapper partyContactMechMapper;

    @Autowired
    public PartyContactMechDao(PartyContactMechMapper partyContactMechMapper) {
        this.partyContactMechMapper = partyContactMechMapper;
    }

    @Transactional
    public List<PartyContactMech> getContactMechWorkEffortTypeRole(String workEffortTypeId) {
        LOG.info("getContactMechWorkEffortTypeRole");

        List<PartyContactMech> partyContactMechList = this.partyContactMechMapper.getContactMechWorkEffortTypeRole(workEffortTypeId);
        LOG.info("size = {}", partyContactMechList.size());
        return partyContactMechList;
    }
}
