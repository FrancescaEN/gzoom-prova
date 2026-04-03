package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.StatusValidChange;
import it.mapsgroup.gzoom.mybatis.mapper.StatusValidChangeMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class StatusValidChangeDao extends AbstractDao {
    private static final Logger LOG = getLogger(StatusValidChangeDao.class);
    private final StatusValidChangeMapper statusValidChangeMapper;

    @Autowired
    public StatusValidChangeDao(StatusValidChangeMapper statusValidChangeMapper) {
        this.statusValidChangeMapper = statusValidChangeMapper;
    }

    @Transactional
    public List<StatusValidChange> findByStatusTypeId(String statusTypeId) {
        LOG.info("find statusValidChange by statusTypeId");

        List<StatusValidChange> statusValidChanges = this.statusValidChangeMapper.findByStatusTypeId(statusTypeId);
        LOG.info("size = {}", statusValidChanges.size());
        return statusValidChanges;
    }

    @Transactional
    public StatusValidChange getStatusValidChange(String statusId, String statusIdTo) {
        LOG.info("find statusValidChange by id");

        StatusValidChange statusValidChange = this.statusValidChangeMapper.selectByPrimaryKey(statusId, statusIdTo);
        LOG.info("StatusItem = {}", (statusIdTo != null));
        return statusValidChange;
    }


    @Transactional
    public boolean create(StatusValidChange statusValidChange, String userLoginId) {
        LOG.info("create statusValidChange");
        setCreatedTimestamp(statusValidChange);
        statusValidChange.setCreatedByUserLogin(userLoginId);
        int result = this.statusValidChangeMapper.insert(statusValidChange);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean update(StatusValidChange statusValidChange, String userLoginId) {
        LOG.info("update statusValidChange");
        setUpdateTimestamp(statusValidChange);
        statusValidChange.setLastModifiedByUserLogin(userLoginId);
        int result = this.statusValidChangeMapper.updateByPrimaryKey(statusValidChange);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String statusId, String status_id_to) {
        LOG.info("delete statusValidChange by statusId");
        int result = this.statusValidChangeMapper.deleteByPrimaryKey(statusId, status_id_to);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean deleteByStatusTypeId(String statusTypeId) {
        LOG.info("delete statusValidChange by statusTypeId");
        int result = this.statusValidChangeMapper.deleteByStatusTypeId(statusTypeId);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
