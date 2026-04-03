package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.mybatis.dao.PartyContentDao;
import it.mapsgroup.gzoom.mybatis.dto.PartyContentEx;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 */
@Service
public class NodeService {
    private static final Logger LOG = getLogger(NodeService.class);


    private final PartyContentDao partyContentDao;

    @Autowired
    public NodeService(PartyContentDao partyContentDao) {
        this.partyContentDao = partyContentDao;
    }

    public String getObjectInfo(String partyId, String partyContentTypeId) {

        PartyContentEx partyContentEx = partyContentDao.getPartyContent(partyId, partyContentTypeId);

        if (partyContentEx != null) {
            LOG.info("stream partyContent path: " + partyContentEx.getDataResource().getObjectInfo());
            return partyContentEx.getDataResource().getObjectInfo();
        }

        return null;
    }
}