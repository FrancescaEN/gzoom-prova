package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.ContentAndAttributes;
import it.mapsgroup.gzoom.mybatis.mapper.ContentAndAttributesMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ContentAndAttributesDao extends AbstractDao {
    private static final Logger LOG = getLogger(ContentAndAttributesDao.class);
    private final ContentAndAttributesMapper contentAndAttributesMapper;

    @Autowired
    public ContentAndAttributesDao(ContentAndAttributesMapper contentAndAttributesMapper) {
        this.contentAndAttributesMapper = contentAndAttributesMapper;
    }

    @Transactional
    public List<ContentAndAttributes> getValidMenu(List<String> keys, String userLoginId) {
        LOG.info("getValidMenu");
        List<String> tmpKey = new ArrayList<>();
        for (String key: keys) {
            // Specific mapping for COMMONEXT / COMMONDATAEXT
            if ("COMMONEXT".equals(key)) {
                key = "COMMONDATAEXT";
            } else if ("BSCPERF".equals(key)) {
                key = "STRATPERF";
            }
            tmpKey.add(key);
        }
        List<ContentAndAttributes> contentAndAttributesList = this.contentAndAttributesMapper.getValidMenu(tmpKey, userLoginId);
        LOG.info("size = {}", contentAndAttributesList.size());
        return contentAndAttributesList;
    }

    @Transactional
    public List<ContentAndAttributes> getFolderMenu() {
        LOG.info("getFolderMenu");
        List<ContentAndAttributes> contentAndAttributesList = this.contentAndAttributesMapper.getFolderMenu();
        LOG.info("size = {}", contentAndAttributesList.size());
        return contentAndAttributesList;
    }
}
