package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.EmplPositionType;
import it.mapsgroup.gzoom.mybatis.mapper.EmplPositionTypeMapper;
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
public class EmplPositionTypeDao extends AbstractDao{

    private static final Logger LOG = getLogger(EmplPositionTypeDao.class);

    private final EmplPositionTypeMapper emplPositionTypeMapper;

    @Autowired
    public EmplPositionTypeDao(EmplPositionTypeMapper emplPositionTypeMapper) {
        this.emplPositionTypeMapper = emplPositionTypeMapper;
    }    
    
    @Transactional
    public List<EmplPositionType> selectAllOrderByPrimaryKey() {
        LOG.info("find all emplPositionType");

        List<EmplPositionType> emplPositionTypes = this.emplPositionTypeMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", emplPositionTypes.size());
        return emplPositionTypes;
    }

    @Transactional
    public EmplPositionType selectByPrimaryKey(String emplPositionTypeId) {
        LOG.info("find emplPositionType by id");

        EmplPositionType emplPositionType = this.emplPositionTypeMapper.selectByPrimaryKey(emplPositionTypeId);
        LOG.info("EmplPositionType = {}", (emplPositionType != null));
        return emplPositionType;
    }

    @Transactional
    public boolean update(EmplPositionType emplPositionType, String userLoginId) {
        LOG.info("update emplPositionType");
        setUpdateTimestamp(emplPositionType);
        emplPositionType.setLastModifiedByUserLogin(userLoginId);
        int result = this.emplPositionTypeMapper.updateByPrimaryKey(emplPositionType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String id) {
        LOG.info("delete emplPositionType");
        int result = this.emplPositionTypeMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean create(EmplPositionType emplPositionType, String userLoginId) {
        LOG.info("create emplPositionType");
        setCreatedTimestamp(emplPositionType);
        emplPositionType.setCreatedByUserLogin(userLoginId);
        int result = this.emplPositionTypeMapper.insert(emplPositionType);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
