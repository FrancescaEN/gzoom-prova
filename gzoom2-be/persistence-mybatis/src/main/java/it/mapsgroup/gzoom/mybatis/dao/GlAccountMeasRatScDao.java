package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountMeasRatSc;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountMeasRatSc;
import it.mapsgroup.gzoom.mybatis.mapper.GlAccountMeasRatScMapper;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GlAccountMeasRatScDao extends AbstractDao{
    private final GlAccountMeasRatScMapper glAccountMeasRatScMapper;
    private static final Logger LOG = getLogger(GlAccountMeasRatScDao.class);

    @Autowired
    public GlAccountMeasRatScDao(GlAccountMeasRatScMapper glAccountMeasRatScMapper) {
        this.glAccountMeasRatScMapper = glAccountMeasRatScMapper;
    }

    @Transactional
    public int countByGlAccountId(String glAccountId) {
        LOG.info("count glAccountMeasRatSc by glAccountId");
        int result = this.glAccountMeasRatScMapper.countByGlAccountId(glAccountId);
        LOG.info("result = {}", result);
        return result;
    }

    @Transactional
    public List<GlAccountMeasRatSc> getGlAccountMeasRatScByGlAccountId(String glAccountId) {
        LOG.info("getGlAccountMeasRatScByGlAccountId");
        List<GlAccountMeasRatSc> glAccountMeasRatScList = this.glAccountMeasRatScMapper.selectByGlAccountId(glAccountId);
        LOG.info("size = {}", glAccountMeasRatScList.size());
        return glAccountMeasRatScList;
    }

    @Transactional
    public boolean create(GlAccountMeasRatSc glAccountMeasRatSc, String userLoginId) {
        LOG.info("create glAccountMeasRatSc");
        setCreatedTimestamp(glAccountMeasRatSc);
        glAccountMeasRatSc.setCreatedByUserLogin(userLoginId);
        int result = this.glAccountMeasRatScMapper.insert(glAccountMeasRatSc);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean update(GlAccountMeasRatSc glAccountMeasRatSc, String userLoginId) {
        LOG.info("update glAccountMeasRatSc");
        setUpdateTimestamp(glAccountMeasRatSc);
        glAccountMeasRatSc.setLastModifiedByUserLogin(userLoginId);
        int result = this.glAccountMeasRatScMapper.updateByPrimaryKey(glAccountMeasRatSc);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String glAccountId,  String uomId, Double uomRatingValue) {
        LOG.info("delete glAccountMeasRatSc");
        int result = this.glAccountMeasRatScMapper.deleteByPrimaryKey(glAccountId, uomId, uomRatingValue);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean deleteByGlAccountId(String glAccountId) {
        LOG.info("delete glAccountMeasRatSc by glAccountId");
        int result = this.glAccountMeasRatScMapper.deleteByGlAccountId(glAccountId);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
