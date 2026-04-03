package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.StatusType;
import it.mapsgroup.gzoom.mybatis.mapper.StatusTypeMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class StatusTypeDao extends AbstractDao {
    private static final Logger LOG = getLogger(StatusTypeDao.class);
    private final StatusTypeMapper statusTypeMapper;

    @Autowired
    public StatusTypeDao(StatusTypeMapper statusTypeMapper) {
        this.statusTypeMapper = statusTypeMapper;
    }

    @Transactional
    public List<StatusType> getStatusTypes() {
        LOG.info("find all statusType");
        List<StatusType> statusTypes = this.statusTypeMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", statusTypes.size());
        return statusTypes;
    }

    @Transactional
    public StatusType getStatusTypeById(String statusTypeId) {
        LOG.info("find statusType by statusTypeId");
        StatusType statusType = this.statusTypeMapper.selectByPrimaryKey(statusTypeId);
        LOG.info("statusType = {}", statusType != null);
        return statusType;
    }

    @Transactional
    public StatusType getStatusType(String statusTypeId) {
        LOG.info("find statusType by id");

        StatusType statusType = this.statusTypeMapper.selectByPrimaryKey(statusTypeId);
        LOG.info("StatusType = {}", (statusType != null));
        return statusType;
    }

    @Transactional
    public boolean update(StatusType statusType, String userLoginId) {
        LOG.info("update statusType");
        setUpdateTimestamp(statusType);
        statusType.setLastModifiedByUserLogin(userLoginId);
        int result = this.statusTypeMapper.updateByPrimaryKey(statusType);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String id) {
        LOG.info("delete statusType");
        int result = this.statusTypeMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean create(StatusType statusType, String userLoginId) {
        LOG.info("create statusType");
        setCreatedTimestamp(statusType);
        statusType.setCreatedByUserLogin(userLoginId);
        int result = this.statusTypeMapper.insert(statusType);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
