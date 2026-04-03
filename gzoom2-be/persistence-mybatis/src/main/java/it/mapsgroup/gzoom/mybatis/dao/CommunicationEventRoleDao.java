package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.CommunicationEventRole;
import it.mapsgroup.gzoom.mybatis.mapper.CommunicationEventRoleMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CommunicationEventRoleDao extends AbstractDao{
    private static final Logger LOG = getLogger(CommunicationEventRoleDao.class);
    private final CommunicationEventRoleMapper communicationEventRoleMapper;


    @Autowired
    public CommunicationEventRoleDao(CommunicationEventRoleMapper communicationEventRoleMapper) {
        this.communicationEventRoleMapper = communicationEventRoleMapper;
    }


    @Transactional
    public boolean create(CommunicationEventRole record) {
        LOG.info("create communicationEventRole");
        setCreatedTimestamp(record);
        int result = this.communicationEventRoleMapper.insert(record);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
