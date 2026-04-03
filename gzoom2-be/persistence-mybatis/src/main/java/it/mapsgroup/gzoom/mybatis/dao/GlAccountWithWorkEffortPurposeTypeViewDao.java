package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountWithWorkEffortPurposeTypeView;
import it.mapsgroup.gzoom.mybatis.mapper.GlAccountWithWorkEffortPurposeTypeViewMapper;
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
public class GlAccountWithWorkEffortPurposeTypeViewDao {
    private static final Logger LOG = getLogger(GlAccountWithWorkEffortPurposeTypeViewDao.class);
    private final GlAccountWithWorkEffortPurposeTypeViewMapper glAccountWithWorkEffortPurposeTypeViewMapper;

    @Autowired
    public GlAccountWithWorkEffortPurposeTypeViewDao(GlAccountWithWorkEffortPurposeTypeViewMapper glAccountWithWorkEffortPurposeTypeViewMapper) {
        this.glAccountWithWorkEffortPurposeTypeViewMapper = glAccountWithWorkEffortPurposeTypeViewMapper;
    }

    @Transactional
    public List<GlAccountWithWorkEffortPurposeTypeView> getGlAccountWithWorkEffortPurposeTypeView(String organizationId) {
        LOG.info("getGlAccountWithWorkEffortPurposeTypeView");

        List<GlAccountWithWorkEffortPurposeTypeView> glAccountWithWorkEffortPurposeTypeView = this.glAccountWithWorkEffortPurposeTypeViewMapper.getGlAccountWithWorkEffortPurposeTypeView(organizationId);
        LOG.info("size = {}", glAccountWithWorkEffortPurposeTypeView.size());
        return glAccountWithWorkEffortPurposeTypeView;
    }

    @Transactional
    public List<GlAccountWithWorkEffortPurposeTypeView> getGlAccountWithWorkEffortPurposeTypeViewFilter(String organizationId, String field, String value, boolean secondaryLanguage) {
        LOG.info("getGlAccountWithWorkEffortPurposeTypeViewFilter");

        List<GlAccountWithWorkEffortPurposeTypeView> glAccountWithWorkEffortPurposeTypeView = this.glAccountWithWorkEffortPurposeTypeViewMapper.getGlAccountWithWorkEffortPurposeTypeViewFilter(organizationId, value, secondaryLanguage);
        LOG.info("size = {}", glAccountWithWorkEffortPurposeTypeView.size());
        return glAccountWithWorkEffortPurposeTypeView;
    }

}
