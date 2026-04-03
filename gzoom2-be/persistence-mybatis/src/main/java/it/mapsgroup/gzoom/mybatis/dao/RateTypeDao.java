package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.RateType;
import it.mapsgroup.gzoom.mybatis.mapper.RateTypeMapper;
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
public class RateTypeDao extends AbstractDao{
    private static final Logger LOG = getLogger(RateTypeDao.class);
    private final RateTypeMapper rateTypeMapper;

    @Autowired
    public RateTypeDao(RateTypeMapper rateTypeMapper) {
        this.rateTypeMapper = rateTypeMapper;
    }

    @Transactional
    public List<RateType> getRateTypes() {
        LOG.info("find all rateType");

        List<RateType> rateTypes = this.rateTypeMapper.selectAll();
        LOG.info("size = {}", rateTypes.size());
        return rateTypes;
    }


}
