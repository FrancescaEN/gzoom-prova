package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortStatus;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortStatusEx;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortStatusMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortStatusDao extends AbstractDao{
    private static final Logger LOG = getLogger(WorkEffortStatusDao.class);

    private final WorkEffortStatusMapper workEffortStatusMapper;

    @Autowired
    public WorkEffortStatusDao( WorkEffortStatusMapper workEffortStatusMapper) {
        this.workEffortStatusMapper = workEffortStatusMapper;
    }

    public List<WorkEffortStatus> findAll() {
        return this.workEffortStatusMapper.selectAll(); }

    public List<WorkEffortStatusEx> getWorkEffortStatusEx(String workEffortId){
        return this.workEffortStatusMapper.selectWorkEffortStatusEx(workEffortId);
    }

    public WorkEffortStatus findById(String workEffortId, String statusId, Instant statusDatetime){
        return this.workEffortStatusMapper.selectByPrimaryKey(workEffortId, statusId, statusDatetime);
    }
    @Transactional
    public WorkEffortStatus create(WorkEffortStatus workEffortStatus, String userLoginId) {
        workEffortStatus.setCreatedByUserLogin(userLoginId);
        this.workEffortStatusMapper.insert(workEffortStatus);
        return workEffortStatus;
    }
    @Transactional
    public int update(WorkEffortStatus workEffortStatus, String userLoginId) {
        workEffortStatus.setLastModifiedByUserLogin(userLoginId);
        return this.workEffortStatusMapper.updateByPrimaryKey(workEffortStatus);
    }

    @Transactional
    public int delete(String workEffortId, String statusId, Instant statusDatetime) {
        WorkEffortStatus workEffortStatus = this.workEffortStatusMapper.selectByPrimaryKey(workEffortId, statusId, statusDatetime);
        return this.workEffortStatusMapper.deleteByPrimaryKey(workEffortStatus.getWorkEffortId(), workEffortStatus.getStatusId(), workEffortStatus.getStatusDatetime());
    }
}
