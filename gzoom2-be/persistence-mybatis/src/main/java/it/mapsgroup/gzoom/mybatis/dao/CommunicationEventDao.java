package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.CommunicationEvent;
import it.mapsgroup.gzoom.mybatis.mapper.CommunicationEventMapper;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CommunicationEventDao extends AbstractDao {
    private static final Logger LOG = getLogger(CommunicationEventDao.class);
    private final SequenceGenerator sequenceGenerator;
    private final CommunicationEventMapper communicationEventMapper;

    @Autowired
    public CommunicationEventDao(SequenceGenerator sequenceGenerator, CommunicationEventMapper communicationEventMapper) {
        this.sequenceGenerator = sequenceGenerator;
        this.communicationEventMapper = communicationEventMapper;
    }

    /**
     * create element CommunicationEvent
     * @param record
     * @return
     */
    @Transactional
    public String create(CommunicationEvent record) {
        LOG.info("create communicationEvent");
        String id = sequenceGenerator.getNextSeqId("CommunicationEvent");
        record.setCommunicationEventId(id);
        setCreatedTimestamp(record);
        int result = this.communicationEventMapper.insert(record);
        LOG.info("result = {}", result);
        return id;
    }

    /**
     * Update element CommunicationEvent
     * @param record
     * @return
     */
    @Transactional
    public boolean update(CommunicationEvent record) {
        LOG.info("update communicationEvent");
        setUpdateTimestamp(record);
        int result = this.communicationEventMapper.updateByPrimaryKey(record);
        LOG.info("result = {}", result);
        return result > 0;
    }


}
