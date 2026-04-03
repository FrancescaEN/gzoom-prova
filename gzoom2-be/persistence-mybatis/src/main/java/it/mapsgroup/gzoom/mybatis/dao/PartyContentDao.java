package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.PartyContentEx;
import it.mapsgroup.gzoom.mybatis.mapper.PartyContentMapper;
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
public class PartyContentDao extends AbstractDao {
    private static final Logger LOG = getLogger(PartyContentDao.class);
    private final PartyContentMapper partyContentMapper;

    @Autowired
    public PartyContentDao(PartyContentMapper partyContentMapper) {
        this.partyContentMapper = partyContentMapper;
    }

    @Transactional
    public PartyContentEx getPartyContent(String partyId, String partyContentTypeId) {
        LOG.info("getPartyContent");

        List<PartyContentEx> partyContentExList = this.partyContentMapper.getPartyContent(partyId, partyContentTypeId);
        LOG.info("size = {}", partyContentExList.size());
        return partyContentExList.isEmpty()? null : partyContentExList.get(0);
    }
}
