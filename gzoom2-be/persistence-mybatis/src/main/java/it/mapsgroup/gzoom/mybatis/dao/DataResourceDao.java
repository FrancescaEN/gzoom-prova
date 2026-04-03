package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.DataResource;
import it.mapsgroup.gzoom.mybatis.mapper.DataResourceMapper;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class DataResourceDao extends AbstractDao{
    private static final Logger LOG = getLogger(DataResourceDao.class);
    private final SequenceGenerator sequenceGenerator;
    private final DataResourceMapper dataResourceMapper;

    @Autowired
    public DataResourceDao(SequenceGenerator sequenceGenerator, DataResourceMapper dataResourceMapper) {
        this.sequenceGenerator = sequenceGenerator;
        this.dataResourceMapper = dataResourceMapper;
    }

    @Transactional
    public List<DataResource> getDataResourceByContentId(String contentId){
        LOG.info("getWorkEffortContentByWorkEffortId");
        List<DataResource> workEffortContentList = this.dataResourceMapper.getDataResourceByContentId(contentId);
        LOG.info("result = {}", workEffortContentList);
        return workEffortContentList;
    }



    @Transactional
    public boolean create(DataResource dataResource) {
        LOG.info("create dataResource");
        setCreatedTimestamp(dataResource);
        String id = sequenceGenerator.getNextSeqId("DataResource");
        dataResource.setDataResourceId(id);
        int result = this.dataResourceMapper.insert(dataResource);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean createWithId(DataResource dataResource) {
        LOG.info("create dataResource with id");
        setCreatedTimestamp(dataResource);
        int result = this.dataResourceMapper.insert(dataResource);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public String createRetId(DataResource dataResource, String userLoginId) {
        LOG.info("create dataResource");
        setCreatedTimestamp(dataResource);
        dataResource.setCreatedByUserLogin(userLoginId);
        String id = sequenceGenerator.getNextSeqId("DataResource");
        dataResource.setDataResourceId(id);
        int result = this.dataResourceMapper.insert(dataResource);
        LOG.info("result = {}", result);
        return (result > 0)? dataResource.getDataResourceId() : null;
    }

    @Transactional
    public boolean update(DataResource dataResource, String userLoginId) {
        LOG.info("update dataResource");
        setUpdateTimestamp(dataResource);
        dataResource.setLastModifiedByUserLogin(userLoginId);
        int result = this.dataResourceMapper.updateByPrimaryKey(dataResource);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String id) {
        LOG.info("delete dataResource");
        int result = this.dataResourceMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

}
