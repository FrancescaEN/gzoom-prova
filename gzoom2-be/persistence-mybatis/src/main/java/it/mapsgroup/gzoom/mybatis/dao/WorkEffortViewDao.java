package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortView;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortViewEx;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortViewMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortViewDao {
    private static final Logger LOG = getLogger(WorkEffortViewDao.class);
    private final WorkEffortViewMapper workEffortViewMapper;

    @Autowired
    public WorkEffortViewDao(WorkEffortViewMapper workEffortViewMapper) {
        this.workEffortViewMapper = workEffortViewMapper;
    }

    @Transactional
    public List<WorkEffortViewEx> getWorkEffortView(String organizationId) {
        LOG.info("find all WorkEffortView where workEffortRevisionId is null and by organizationId");

        List<WorkEffortViewEx> effortPartyAssignmentExList = this.workEffortViewMapper.selectAllWhereWorkEffortRevisionIdIsNullAndByOrganizationId(organizationId);
        LOG.info("size = {}", effortPartyAssignmentExList.size());
        return effortPartyAssignmentExList;
    }

    @Transactional
    public List<WorkEffortView> getWorkEffortViewFilter(String organizationId, String field, String value) {
        LOG.info("getWorkEffortViewFilter");

        List<WorkEffortView> effortPartyAssignmentExList = this.workEffortViewMapper.getWorkEffortViewFilter(organizationId, field, value);
        LOG.info("size = {}", effortPartyAssignmentExList.size());
        return effortPartyAssignmentExList;
    }
}
