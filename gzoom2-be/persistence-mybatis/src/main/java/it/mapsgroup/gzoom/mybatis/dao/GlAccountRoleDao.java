package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountRole;
import it.mapsgroup.gzoom.mybatis.dto.GlResourceType;
import it.mapsgroup.gzoom.mybatis.dto.Uom;
import it.mapsgroup.gzoom.mybatis.mapper.GlAccountRoleMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GlAccountRoleDao extends AbstractDao{
    private static final Logger LOG = getLogger(GlAccountRoleDao.class);
    private final GlAccountRoleMapper glAccountRoleMapper;

    @Autowired
    public GlAccountRoleDao(GlAccountRoleMapper glAccountRoleMapper) {
        this.glAccountRoleMapper = glAccountRoleMapper;
    }

    @Transactional
    public List<GlAccountRole> selectByGlAccountId(String glAccountId) {
        LOG.info("selectByPrimaryKey");
        List<GlAccountRole> glAccountRole = this.glAccountRoleMapper.selectByGlAccountId(glAccountId);
        LOG.info("size = {}", glAccountRole.size());
        return glAccountRole;
    }

    @Transactional
    public List<GlAccountRole> getUoDetectedByGlAccountId(String glAccountId) {
        LOG.info("getUoDetectedByGlAccountId");
        List<GlAccountRole> glAccountRole = this.glAccountRoleMapper.getUoDetectedByGlAccountId(glAccountId);
        LOG.info("size = {}", glAccountRole.size());
        return glAccountRole;
    }

    @Transactional
    public boolean create(GlAccountRole glAccountRole, String userLoginId) {
        LOG.info("create glAccountRole");
        setCreatedTimestamp(glAccountRole);
        glAccountRole.setCreatedByUserLogin(userLoginId);
        int result = this.glAccountRoleMapper.insert(glAccountRole);
        LOG.info("result = {}", result);
        return result > 0;
    }


    @Transactional
    public boolean update(GlAccountRole glAccountRole, String userLoginId) {
        LOG.info("update glAccountRole");
        setUpdateTimestamp(glAccountRole);
        glAccountRole.setLastModifiedByUserLogin(userLoginId);
        int result = this.glAccountRoleMapper.updateByPrimaryKey(glAccountRole);
        LOG.info("result = {}", result);
        return result > 0;
    }


    @Transactional
    public boolean delete(GlAccountRole glAccountRole) {
        LOG.info("delete glAccountRole");
        int result = this.glAccountRoleMapper.deleteByPrimaryKey(glAccountRole.getGlAccountId(), glAccountRole.getPartyId(), glAccountRole.getRoleTypeId(), glAccountRole.getFromDate());
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean deleteByGlAccountId(String glAccountId) {
        LOG.info("delete glAccountRole by glAccountId");
        int result = this.glAccountRoleMapper.deleteByGlAccountId(glAccountId);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
