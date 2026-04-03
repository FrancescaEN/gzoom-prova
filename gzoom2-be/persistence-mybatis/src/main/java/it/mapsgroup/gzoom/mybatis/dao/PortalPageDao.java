package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.PortalPage;
import it.mapsgroup.gzoom.mybatis.mapper.PortalPageMapper;
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
public class PortalPageDao extends AbstractDao {
    private static final Logger LOG = getLogger(PortalPageDao.class);
    private final PortalPageMapper portalPageMapper;

    @Autowired
    public PortalPageDao(PortalPageMapper portalPageMapper) {
        this.portalPageMapper = portalPageMapper;
    }

    @Transactional
    public List<PortalPage> findByParentPortalPageId ( String parentPortalPageId ) {
        LOG.info("find portal page by parentPortalPageId");

        List<PortalPage> portalPages = this.portalPageMapper.selectByParentPortalPageId(parentPortalPageId);
        LOG.info("size = {}", portalPages.size());
        return portalPages;
    }
}
