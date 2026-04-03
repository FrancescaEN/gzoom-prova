package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountInputCalc;
import it.mapsgroup.gzoom.mybatis.dto.Party;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPurposeType;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPurposeTypeEx;
import it.mapsgroup.gzoom.mybatis.mapper.GlAccountInputCalcMapper;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GlAccountInputCalcDao extends AbstractDao{
    private final GlAccountInputCalcMapper glAccountInputCalcMapper;
    private static final Logger LOG = getLogger(GlAccountInputCalcDao.class);
    private final SequenceGenerator sequenceGenerator;

    @Autowired
    public GlAccountInputCalcDao(GlAccountInputCalcMapper glAccountInputCalcMapper, SequenceGenerator sequenceGenerator) {
        this.glAccountInputCalcMapper = glAccountInputCalcMapper;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Transactional
    public int countByGlAccountId(String glAccountId) {
        LOG.info("countByGlAccountId");
        int result = this.glAccountInputCalcMapper.countByGlAccountId(glAccountId);
        LOG.info("size = {}", result);
        return result;
    }

    @Transactional
    public List<GlAccountInputCalc> getGlAccountInputCalcByGlAccountId(String glAccountId) {
        LOG.info("getGlAccountInputCalcByGlAccountId");
        List<GlAccountInputCalc> glAccountInputCalcs = this.glAccountInputCalcMapper.selectByGlAccountId(glAccountId);
        LOG.info("size = {}", glAccountInputCalcs.size());
        return glAccountInputCalcs;
    }

    @Transactional
    public List<GlAccountInputCalc> getGlAccountInputCalcByGlAccountIdRef(String glAccountIdRef) {
        LOG.info("getGlAccountInputCalcByGlAccountIdRef");
        List<GlAccountInputCalc> glAccountInputCalcs = this.glAccountInputCalcMapper.selectByGlAccountIdRef(glAccountIdRef);
        LOG.info("size = {}", glAccountInputCalcs.size());
        return glAccountInputCalcs;
    }

    @Transactional
    public GlAccountInputCalc create(GlAccountInputCalc glAccountInputCalc, String userLoginId) {
        LOG.info("create glAccountInputCalc");
        String newId = this.sequenceGenerator.getNextSeqId("GlAccountInputCalc");
        glAccountInputCalc.setGlAccountInputCalcId(newId);
        setCreatedTimestamp(glAccountInputCalc);
        glAccountInputCalc.setCreatedByUserLogin(userLoginId);
        int result = this.glAccountInputCalcMapper.insert(glAccountInputCalc);
        LOG.info("result = {}", result);
        return result > 0? glAccountInputCalc : null;
    }

    @Transactional
    public boolean update(GlAccountInputCalc glAccountInputCalc, String userLoginId) {
        LOG.info("update glAccountInputCalc");
        setUpdateTimestamp(glAccountInputCalc);
        glAccountInputCalc.setLastModifiedByUserLogin(userLoginId);
        int result = this.glAccountInputCalcMapper.updateByPrimaryKey(glAccountInputCalc);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean updateGlAccountIdRef(String glAccountInputCalcId, String glAccountIdRef, String userLoginId) {
        LOG.info("update glAccountInputCalc GlAccountIdRef");
        GlAccountInputCalc glAccountInputCalc = new GlAccountInputCalc();
        setUpdateTimestamp(glAccountInputCalc);
        glAccountInputCalc.setLastModifiedByUserLogin(userLoginId);
        glAccountInputCalc.setGlAccountInputCalcId(glAccountInputCalcId);
        glAccountInputCalc.setGlAccountIdRef(glAccountIdRef);
        int result = this.glAccountInputCalcMapper.updateGlAccountIdRefByPrimaryKey(glAccountInputCalc);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String glAccountInputCalcId) {
        LOG.info("delete glAccountInputCalc");
        int result = this.glAccountInputCalcMapper.deleteByPrimaryKey(glAccountInputCalcId);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean deleteByGlAccountId(String glAccountId) {
        LOG.info("delete glAccountInputCalc by glAccountId");
        int result = this.glAccountInputCalcMapper.deleteByGlAccountId(glAccountId);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
