package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.AcctgTrans;
import it.mapsgroup.gzoom.mybatis.dto.AcctgTransEntry;
import it.mapsgroup.gzoom.mybatis.mapper.AcctgTransMapper;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class AcctgTransDao extends AbstractDao {
    private static final Logger LOG = getLogger(AcctgTransDao.class);
    private final SequenceGenerator sequenceGenerator;
    private final AcctgTransMapper acctgTransMapper;

    public AcctgTransDao(SequenceGenerator sequenceGenerator, AcctgTransMapper acctgTransMapper) {
        this.sequenceGenerator = sequenceGenerator;
        this.acctgTransMapper = acctgTransMapper;
    }


    @Transactional
    public List<AcctgTrans> getAcctgTransByWorkEffortMeasureId(String workEffortMeasureId) {
        LOG.info("find all AcctgTrans by workEffortMeasureId");
        List<AcctgTrans> acctgTransEntryList = this.acctgTransMapper.getAcctgTransByWorkEffortMeasureId(workEffortMeasureId);
        LOG.info("size = {}", acctgTransEntryList.size());
        return acctgTransEntryList;
    }

    @Transactional
    public AcctgTrans selectByPrimaryKey(String acctgTransId) {
        LOG.info("selectByPrimaryKey AcctgTrans");
        AcctgTrans acctgTrans = this.acctgTransMapper.selectByPrimaryKey(acctgTransId);
        LOG.info("size = {}", (acctgTrans != null));
        return acctgTrans;
    }

    @Transactional
    public boolean delete(AcctgTrans acctgTrans) {
        LOG.info("delete acctgTrans");
        int result = this.acctgTransMapper.deleteByPrimaryKey(acctgTrans.getAcctgTransId());
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean deleteByPrimaryKey(String acctgTransId) {
        LOG.info("delete acctgTrans by primaryKey");
        int result = this.acctgTransMapper.deleteByPrimaryKey(acctgTransId);
        LOG.info("result = {}", result);
        return result > 0;
    }



    @Transactional
    public boolean updatePartialTable(AcctgTrans acctgTrans, String userLoginId) {
        LOG.info("updatePartialTable acctgTrans");
        setUpdateTimestamp(acctgTrans);
        acctgTrans.setLastModifiedByUserLogin(userLoginId);
        int result = this.acctgTransMapper.updatePartialTable(acctgTrans);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public String create(AcctgTrans acctgTrans, String userLoginId) {
        LOG.info("create acctgTrans");
        String id = this.sequenceGenerator.getNextSeqId("AcctgTrans");
        acctgTrans.setAcctgTransId(id);
        setCreatedTimestamp(acctgTrans);
        acctgTrans.setCreatedByUserLogin(userLoginId);
        int result = this.acctgTransMapper.insert(acctgTrans);
        LOG.info("result = {}", result);
        return result > 0? id : null;
    }

    @Transactional
    public boolean updateDetail(AcctgTrans acctgTrans, String userLoginId) {
        LOG.info("updateDetail acctgTrans");
        setUpdateTimestamp(acctgTrans);
        acctgTrans.setLastModifiedByUserLogin(userLoginId);
        int result = this.acctgTransMapper.updateDetail(acctgTrans);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
