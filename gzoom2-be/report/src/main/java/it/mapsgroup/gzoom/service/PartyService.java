package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.mybatis.dao.PartyDao;
import it.mapsgroup.gzoom.mybatis.dto.Party;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class PartyService {
    private static final Logger LOG = getLogger(PartyService.class);
    private final PartyDao partyDao;

    @Autowired
    public PartyService(PartyDao partyDao) {
        this.partyDao = partyDao;
    }

    public Party getParty(String partyId) {
        return this.partyDao.findByPartyId(partyId);
    }
}