package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountOrganization;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountType;
import it.mapsgroup.gzoom.mybatis.dto.StatusValidChange;
import it.mapsgroup.gzoom.mybatis.dto.Uom;
import it.mapsgroup.gzoom.mybatis.mapper.GlAccountOrganizationMapper;
import it.mapsgroup.gzoom.mybatis.mapper.GlAccountTypeMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GlAccountOrganizationDao extends AbstractDao{
    private static final Logger LOG = getLogger(GlAccountOrganizationMapper.class);
    private final GlAccountOrganizationMapper glAccountOrganizationMapper;
    @Autowired
    public GlAccountOrganizationDao(GlAccountOrganizationMapper glAccountOrganizationMapper) {
        this.glAccountOrganizationMapper = glAccountOrganizationMapper;
    }

    @Transactional
    public boolean create(GlAccountOrganization glAccountOrganization, String userLoginId) {
        LOG.info("create glAccountOrganization");
        setCreatedTimestamp(glAccountOrganization);
        glAccountOrganization.setCreatedByUserLogin(userLoginId);
        int result = this.glAccountOrganizationMapper.insert(glAccountOrganization);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public List<GlAccountOrganization> selectByGlAccountId(String glAccountId) {
        LOG.info("selectByGlAccountId");
        List<GlAccountOrganization> glAccountOrganization = this.glAccountOrganizationMapper.selectByGlAccountId(glAccountId);
        LOG.info("size = {}", glAccountOrganization.size());
        return glAccountOrganization;
    }

    @Transactional
    public boolean update(GlAccountOrganization glAccountOrganization, String userLoginId) {
        LOG.info("update glAccountOrganization");
        setUpdateTimestamp(glAccountOrganization);
        glAccountOrganization.setLastModifiedByUserLogin(userLoginId);
        int result = this.glAccountOrganizationMapper.updateByPrimaryKey(glAccountOrganization);
        LOG.info("result = {}", result);
        return result > 0;
    }
    @Transactional
    public boolean delete(String glAccountId, String organizationId) {
        LOG.info("delete glAccountType");
        int result = this.glAccountOrganizationMapper.deleteByPrimaryKey(glAccountId, organizationId);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean deleteByGlAccountId(String glAccountId) {
        LOG.info("delete glAccountType by glAccountId");
        int result = this.glAccountOrganizationMapper.deleteByGlAccountId(glAccountId);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
