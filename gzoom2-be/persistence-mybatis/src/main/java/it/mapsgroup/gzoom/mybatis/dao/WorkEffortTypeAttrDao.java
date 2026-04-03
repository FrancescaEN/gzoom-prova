package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypeAttr;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortTypeAttrMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortTypeAttrDao extends AbstractDao{
    private static final Logger LOG = getLogger(WorkEffortTypeAttrDao.class);
    private final WorkEffortTypeAttrMapper workEffortTypeAttrMapper;

    @Autowired
    public WorkEffortTypeAttrDao(WorkEffortTypeAttrMapper workEffortTypeAttrMapper) {
        this.workEffortTypeAttrMapper = workEffortTypeAttrMapper;
    }

    @Transactional
    public List<WorkEffortTypeAttr> getWorkEffortTypeAttrList(String workEffortTypeId) {
        LOG.info("find workEffortTypeAttrMapper by id");

        List<WorkEffortTypeAttr> workEffortTypeAttrList = this.workEffortTypeAttrMapper.getWorkEffortTypeAttrList(workEffortTypeId);
        LOG.info("size = {}", workEffortTypeAttrList.size());
        return workEffortTypeAttrList;
    }

    @Transactional
    public List<WorkEffortTypeAttr> getWorkEffortTypeAttrListAll() {
        LOG.info("find workEffortTypeAttrMapper");

        List<WorkEffortTypeAttr> workEffortTypeAttrList = this.workEffortTypeAttrMapper.selectAll();
        LOG.info("size = {}", workEffortTypeAttrList.size());
        return workEffortTypeAttrList;
    }

    @Transactional
    public WorkEffortTypeAttr get(String workEffortTypeId, String attrName) {
        LOG.info("WorkEffortTypeAttr");

        WorkEffortTypeAttr workEffortTypeAttr = this.workEffortTypeAttrMapper.get(workEffortTypeId,attrName);
        LOG.info("WorkEffortTypeAttr = {}", (workEffortTypeAttr != null));
        return workEffortTypeAttr;
    }


}
