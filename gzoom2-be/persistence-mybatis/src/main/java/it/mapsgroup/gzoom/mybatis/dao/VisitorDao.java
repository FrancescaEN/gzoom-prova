package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.Visitor;
import it.mapsgroup.gzoom.mybatis.mapper.VisitorMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class VisitorDao extends AbstractDao {
    private static final Logger LOG = getLogger(VisitorDao.class);

    private final VisitorMapper visitorMapper;

    @Autowired
    public VisitorDao(VisitorMapper visitorMapper) {
        this.visitorMapper = visitorMapper;
    }

    public Visitor getVisitorById(String visitorId){
        return visitorMapper.selectByPrimaryKey(visitorId);
    }

    @Transactional
    public Visitor create(Visitor visitor, String UserLoginId) {
        LOG.info("create Visitor");
        visitor.setUserLoginId(UserLoginId);
        visitor.setCreatedStamp(Instant.now());
        visitor.setCreatedTxStamp(Instant.now());
        this.visitorMapper.insert(visitor);
        return visitor;
    }

    @Transactional
    public int update(Visitor visitor) {
        LOG.info("update Visitor");
        visitor.setLastUpdatedStamp(Instant.now());
        visitor.setLastUpdatedTxStamp(Instant.now());
        return visitorMapper.updateByPrimaryKey(visitor);
    }

    @Transactional
    public int delete(String visitorId) {
        LOG.info("delete Visitor");
        Visitor visitor = this.visitorMapper.selectByPrimaryKey(visitorId);
        return this.visitorMapper.deleteByPrimaryKey(visitor.getVisitorId());
    }

    @Transactional
    public int deleteOld(Instant minusYear) {
        LOG.info("delete old Visitor");
        return this.visitorMapper.deleteOld(minusYear);
    }
}
