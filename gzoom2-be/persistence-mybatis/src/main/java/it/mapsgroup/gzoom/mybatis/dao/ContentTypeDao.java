package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.ContentType;
import it.mapsgroup.gzoom.mybatis.mapper.ContentTypeMapper;
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
public class ContentTypeDao extends AbstractDao {
    private static final Logger LOG = getLogger(ContentTypeDao.class);
    private final ContentTypeMapper contentTypeMapper;

    @Autowired
    public ContentTypeDao(ContentTypeMapper contentTypeMapper) {
        this.contentTypeMapper = contentTypeMapper;
    }

    /**
     * This function gets a contentType list given its parentTypeId.
     *
     * @param parentTypeId data resource type id of the contentType
     * @return the corresponding contentType record
     */
    @Transactional
    public List<ContentType> getContentTypeWithParentTypeId(String parentTypeId) {
        LOG.info("find contentType by id");

        List<ContentType> contentTypes = this.contentTypeMapper.selectByParentTypeId(parentTypeId);
        LOG.info("size = {}", contentTypes.size());
        return contentTypes;
    }
}
