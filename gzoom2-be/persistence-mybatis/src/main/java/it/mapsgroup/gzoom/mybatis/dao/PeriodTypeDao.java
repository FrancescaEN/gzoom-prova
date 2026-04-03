package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.PeriodType;
import it.mapsgroup.gzoom.mybatis.mapper.PeriodTypeMapper;
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
public class PeriodTypeDao extends AbstractDao {


    private static final Logger LOG = getLogger(PeriodTypeDao.class);
    private final PeriodTypeMapper periodTypeMapper;

    @Autowired
    public PeriodTypeDao(PeriodTypeMapper periodTypeMapper) {
        this.periodTypeMapper = periodTypeMapper;
    }
    
    @Transactional
    public List<PeriodType> selectAllOrderByPrimaryKey() {
        LOG.info("find all periodType");

        List<PeriodType> periodTypes = this.periodTypeMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", periodTypes.size());
        return periodTypes;
    }

    @Transactional
    public PeriodType getPeriodType(String periodTypeId) {
        LOG.info("find periodType by id");

        PeriodType periodType = this.periodTypeMapper.selectByPrimaryKey(periodTypeId);
        LOG.info("PeriodType = {}", (periodType != null));
        return periodType;
    }

    @Transactional
    public boolean update(PeriodType periodType, String userLoginId) {
        LOG.info("update periodType");
        setUpdateTimestamp(periodType);
        setLastModifiedByUserLogin(periodType, userLoginId);
        int result = this.periodTypeMapper.updateByPrimaryKey(periodType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String id) {
        LOG.info("delete periodType");
        int result = this.periodTypeMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean create(PeriodType periodType, String userLoginId) {
        LOG.info("create periodType");
        setCreatedTimestamp(periodType);
        setCreatedByUserLogin(periodType, userLoginId);
        int result = this.periodTypeMapper.insert(periodType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    private void setCreatedByUserLogin(PeriodType record, String userLoginId) {
        record.setCreatedByUserLogin(userLoginId);
    }

    private void setLastModifiedByUserLogin(PeriodType record, String userLoginId) {
        record.setLastModifiedByUserLogin(userLoginId);
    }
}
