package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.Visit;
import it.mapsgroup.gzoom.mybatis.mapper.VisitMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class VisitDao extends AbstractDao {
    private static final Logger LOG = getLogger(VisitDao.class);

    private final VisitMapper visitMapper;

    @Autowired
    public VisitDao(VisitMapper visitMapper) {
        this.visitMapper = visitMapper;
    }

    public Visit getVisitById(String visitId){
        return visitMapper.selectByPrimaryKey(visitId);
    }

    @Transactional
    public Visit create(Visit visit, String UserLoginId) {
        LOG.info("create Visitor");
        visit.setUserLoginId(UserLoginId);
        visit.setCreatedStamp(Instant.now());
        visit.setCreatedTxStamp(Instant.now());
        this.visitMapper.insert(visit);
        return visit;
    }

    @Transactional
    public int update(Visit visit) {
        LOG.info("update Visitor");
        visit.setLastUpdatedStamp(Instant.now());
        visit.setLastUpdatedTxStamp(Instant.now());
        return visitMapper.updateByPrimaryKey(visit);
    }

    @Transactional
    public int delete(String visitId) {
        LOG.info("delete Visitor");
        Visit visit = this.visitMapper.selectByPrimaryKey(visitId);
        return this.visitMapper.deleteByPrimaryKey(visit.getVisitId());
    }

    @Transactional
    public int deleteOld(Instant minusYear) {
        LOG.info("delete old Visit");
        return this.visitMapper.deleteOld(minusYear);
    }
}
