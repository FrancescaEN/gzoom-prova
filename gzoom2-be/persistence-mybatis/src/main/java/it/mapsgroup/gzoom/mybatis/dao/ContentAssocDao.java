package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.ContentAssoc;
import it.mapsgroup.gzoom.mybatis.dto.ContentAssocMenu;
import it.mapsgroup.gzoom.mybatis.mapper.ContentAssocMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ContentAssocDao extends AbstractDao {
    private static final Logger LOG = getLogger(ContentAssocDao.class);
    private final ContentAssocMapper contentAssocMapper;

    @Autowired
    public ContentAssocDao(ContentAssocMapper contentAssocMapper) {
        this.contentAssocMapper = contentAssocMapper;
    }

    @Transactional
    public List<ContentAssoc> getHelpId(String contentIdTo) {
        LOG.info("getHelpId");
        
        List<ContentAssoc> contentAssocList = this.contentAssocMapper.getHelpId(contentIdTo);
        LOG.info("size = {}", contentAssocList.size());
        return contentAssocList;
    }

    @Transactional
    public List<ContentAssocMenu> getMenuPath(String contentIdTo) {
        LOG.info("getMenuPath");

        List<ContentAssocMenu> contentAssocMenuList = this.contentAssocMapper.getMenuPath(contentIdTo);
        LOG.info("size = {}", contentAssocMenuList.size());
        return contentAssocMenuList;
    }

    @Transactional
    public boolean create(ContentAssoc contentAssoc, String userLoginId) {
        LOG.info("create contentAssoc");
        setCreatedTimestamp(contentAssoc);
        contentAssoc.setCreatedByUserLogin(userLoginId);
        int result = this.contentAssocMapper.insert(contentAssoc);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean update(ContentAssoc contentAssoc, String userLoginId) {
        LOG.info("update contentAssoc");
        setUpdateTimestamp(contentAssoc);
        contentAssoc.setLastModifiedByUserLogin(userLoginId);
        int result = this.contentAssocMapper.updateByPrimaryKey(contentAssoc);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String contentId, String contentIdTo, String contentAssocTypeId, Instant fromDate) {
        LOG.info("delete contentAssoc");
        int result = this.contentAssocMapper.deleteByPrimaryKey(contentId, contentIdTo, contentAssocTypeId, fromDate);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
