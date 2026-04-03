package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.Content;
import it.mapsgroup.gzoom.mybatis.dto.ContentExDataResource;
import it.mapsgroup.gzoom.mybatis.mapper.ContentMapper;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ContentDao extends AbstractDao {
    private static final Logger LOG = getLogger(ContentDao.class);
    private final ContentMapper contentMapper;
    private final SequenceGenerator sequenceGenerator;

    @Autowired
    public ContentDao(ContentMapper contentMapper, SequenceGenerator sequenceGenerator) {
        this.contentMapper = contentMapper;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Transactional
    public boolean create(Content content) {
        LOG.info("create content");
        setCreatedTimestamp(content);
        String id = sequenceGenerator.getNextSeqId("Content");
        content.setContentId(id);
        int result = this.contentMapper.insert(content);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean createWithId(Content content) {
        LOG.info("create content with id");
        setCreatedTimestamp(content);
        int result = this.contentMapper.insert(content);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public String createRetId(Content content, String userLoginId) {
        LOG.info("create content");
        setCreatedTimestamp(content);
        content.setCreatedByUserLogin(userLoginId);
        String id = sequenceGenerator.getNextSeqId("Content");
        content.setContentId(id);
        int result = this.contentMapper.insert(content);
        LOG.info("result = {}", result);
        return (result > 0)? content.getContentId() : null;
    }

    @Transactional
    public Content getContentByActivityId(String activityId) {
        LOG.info("getContentByActivityId");

        List<Content> contents = this.contentMapper.selectByServiceName(activityId);
        LOG.info("size = {}", contents.size());
        return contents.isEmpty() ? null : contents.get(0);
    }

    @Transactional
    public List<ContentExDataResource> getContentListByContentTypeId(String contentTypeId) {
        LOG.info("getContentListByContentTypeId");

        List<ContentExDataResource> contents = this.contentMapper.getContentListByContentTypeId(contentTypeId);
        LOG.info("size = {}", contents.size());
        return contents;
    }

    @Transactional
    public Content getContentById(String contentId) {
        LOG.info("getContentById");
        Content content = this.contentMapper.selectByPrimaryKey(contentId);
        LOG.info("size = {}", content);
        return content;
    }

    @Transactional
    public List<Content> findByContentTypeId(String contentTypeId) {
        LOG.info("findByContentTypeId");

        List<Content> contents = this.contentMapper.selectByContentTypeId(contentTypeId);
        LOG.info("size = {}", contents.size());
        return contents;
    }

    @Transactional
    public boolean update(Content content, String userLoginId) {
        LOG.info("update content");
        setUpdateTimestamp(content);
        content.setLastModifiedByUserLogin(userLoginId);
        int result = this.contentMapper.updateByPrimaryKey(content);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean updateDescription(String contentId, String description, String userLoginId) {
        LOG.info("update content description");
        Content content = new Content();
        content.setContentId(contentId);
        content.setDescription(description);
        setUpdateTimestamp(content);
        content.setLastModifiedByUserLogin(userLoginId);
        int result = this.contentMapper.updateDescription(content);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String contentId) {
        LOG.info("delete content");
        int result = this.contentMapper.deleteByPrimaryKey(contentId);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
