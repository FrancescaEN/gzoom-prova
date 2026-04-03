package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.AcctgTransInterface;
import it.mapsgroup.gzoom.mybatis.dto.AcctgTransInterfaceExt;
import it.mapsgroup.gzoom.mybatis.mapper.AcctgTransInterfaceExtMapper;
import it.mapsgroup.gzoom.mybatis.mapper.AcctgTransInterfaceMapper;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class AcctgTransInterfaceDao {
    private static final Logger LOG = getLogger(AcctgTransInterfaceDao.class);
    private final SequenceGenerator sequenceGenerator;
    private final AcctgTransInterfaceMapper acctgTransInterfaceMapper;
    private final AcctgTransInterfaceExtMapper acctgTransInterfaceExtMapper;

    public AcctgTransInterfaceDao(SequenceGenerator sequenceGenerator, AcctgTransInterfaceMapper acctgTransInterfaceMapper, AcctgTransInterfaceExtMapper acctgTransInterfaceExtMapper) {
        this.sequenceGenerator = sequenceGenerator;
        this.acctgTransInterfaceMapper = acctgTransInterfaceMapper;
        this.acctgTransInterfaceExtMapper = acctgTransInterfaceExtMapper;
    }

    @Transactional
    public List<AcctgTransInterface> getAll() {
        LOG.info("find all acctgTransInterface");

        List<AcctgTransInterface> acctgTransInterfaceList = this.acctgTransInterfaceMapper.selectAll();
        LOG.info("size = {}", acctgTransInterfaceList.size());
        return acctgTransInterfaceList;
    }

    @Transactional
    public boolean create(AcctgTransInterface record) {
        String id = sequenceGenerator.getNextSeqId("AcctgTransInterface");
        record.setId(id);
        int result = this.acctgTransInterfaceMapper.insert(record);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean createExt(AcctgTransInterfaceExt record) {
        String id = sequenceGenerator.getNextSeqId("AcctgTransInterfaceExt");
        record.setId(id);
        int result = this.acctgTransInterfaceExtMapper.insert(record);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
