package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.PartyNoteEx;
import it.mapsgroup.gzoom.mybatis.mapper.PartyNoteMapper;
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
public class PartyNoteDao extends AbstractDao {
    private static final Logger LOG = getLogger(PartyNoteDao.class);
    private final PartyNoteMapper partyNoteMapper;

    @Autowired
    public PartyNoteDao(PartyNoteMapper partyNoteMapper) {
        this.partyNoteMapper = partyNoteMapper;
    }

    @Transactional
    public PartyNoteEx getPartyNote(String partyId, String noteName) {
        LOG.info("getPartyNote");


        List<PartyNoteEx> partyNoteExList = this.partyNoteMapper.getPartyNote(partyId, noteName);
        LOG.info("size = {}", partyNoteExList.size());
        return partyNoteExList.isEmpty()? null : partyNoteExList.get(0);
    }
}
