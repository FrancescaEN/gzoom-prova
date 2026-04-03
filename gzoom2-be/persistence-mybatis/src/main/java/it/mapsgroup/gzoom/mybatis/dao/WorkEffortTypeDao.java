package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortType;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypeContentExt;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortTypeMapper;
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
public class WorkEffortTypeDao extends AbstractDao{
    private static final Logger LOG = getLogger(WorkEffortTypeDao.class);
    private final WorkEffortTypeMapper workEffortTypeMapper;

    @Autowired
    public WorkEffortTypeDao(WorkEffortTypeMapper workEffortTypeMapper) {
        this.workEffortTypeMapper = workEffortTypeMapper;
    }

    @Transactional
    public List<WorkEffortType> selectAll() {
        LOG.info("find all workEffortType");

        List<WorkEffortType> workEffortTypes = this.workEffortTypeMapper.selectAll();
        LOG.info("size = {}", workEffortTypes.size());
        return workEffortTypes;
    }

    @Transactional
    public WorkEffortType selectByPrimaryKey(String workEffortTypeId) {
        LOG.info("find workEffortType by id");

        WorkEffortType workEffortType = this.workEffortTypeMapper.selectByPrimaryKey(workEffortTypeId);
        LOG.info("WorkEffortType = {}", (workEffortType != null));
        return workEffortType;
    }

    @Transactional
    public List<WorkEffortType> selectLikeWorkEffortTypeId(String likeId) {
        LOG.info("selectLikeWorkEffortTypeId");

        List<WorkEffortType> workEffortTypes = this.workEffortTypeMapper.selectLikeWorkEffortTypeId(likeId);
        LOG.info("size = {}", workEffortTypes.size());
        return workEffortTypes;
    }

    @Transactional
    public List<WorkEffortType> getWorkEffortTypes(String workEffortTypeId) {
        LOG.info("getWorkEffortTypes");

        List<WorkEffortType> workEffortTypes = this.workEffortTypeMapper.getWorkEffortTypes(workEffortTypeId.split(","));
        LOG.info("size = {}", workEffortTypes.size());
        return workEffortTypes;
    }

    @Transactional
    public List<WorkEffortType> getWorkEffortTypeByParentId(String parentTypeId) {
        LOG.info("getWorkEffortTypeByParentId");
        List<WorkEffortType> workEffortTypes = this.workEffortTypeMapper.getWorkEffortTypeByParentId(parentTypeId);
        LOG.info("size = {}", workEffortTypes.size());
        return workEffortTypes;
    }

    @Transactional
    public List<WorkEffortType> getWorkEffortTypesParametric(String workEffortTypeId) {
        LOG.info("getWorkEffortTypesParametric");

        List<WorkEffortType> workEffortTypes = this.workEffortTypeMapper.getWorkEffortTypesParametric(workEffortTypeId);
        LOG.info("size = {}", workEffortTypes.size());
        return workEffortTypes;
    }

    @Transactional
    public List<WorkEffortType> getWorkEffortTypesIsRoot(String isRoot) {
        LOG.info("find workEffortType by isRoot");

        List<WorkEffortType> workEffortTypes = this.workEffortTypeMapper.selectByIsRoot(isRoot);
        LOG.info("size = {}", workEffortTypes.size());
        return workEffortTypes;
    }

    @Transactional
    public List<WorkEffortTypeContentExt> getWorkEffortTypeReminderActive(String contentId) {
        LOG.info("getWorkEffortTypeReminderActive");

        List<WorkEffortTypeContentExt> workEffortTypeContentExtList = this.workEffortTypeMapper.getWorkEffortTypeReminderActive(contentId);
        LOG.info("size = {}", workEffortTypeContentExtList.size());
        return workEffortTypeContentExtList;
    }
}
