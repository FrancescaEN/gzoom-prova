package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.mybatis.dto.UserPreference;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPartyAssignment;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortPartyAssignmentEx;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortPartyAssignmentMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortPartyAssignmentDao extends AbstractDao{
    private static final Logger LOG = getLogger(WorkEffortPartyAssignmentDao.class);
    private final WorkEffortPartyAssignmentMapper workEffortPartyAssignmentMapper;
    @Autowired
    public WorkEffortPartyAssignmentDao(WorkEffortPartyAssignmentMapper workEffortPartyAssignmentMapper) {
        this.workEffortPartyAssignmentMapper = workEffortPartyAssignmentMapper;
    }

    /**
     * This function gets a WorkEffortPartyAssignment given its id.
     *
     * @return the corresponding WorkEffortPartyAssignment record
     */
    @Transactional
    public WorkEffortPartyAssignment get(String workEffortId, String roleTypeId, String partyId, Instant fromDate) {
        LOG.info("find workEffortPartyAssignment by id");

        WorkEffortPartyAssignment workEffortPartyAssignment = this.workEffortPartyAssignmentMapper.selectByPrimaryKey(workEffortId, roleTypeId, partyId, fromDate);
        LOG.info("WorkEffortPartyAssignment = {}", (workEffortPartyAssignment != null));
        return workEffortPartyAssignment;
    }


    /**
     * Gets a list of WorkEffortPartyAssignment.
     *
     * @return
     */
    @Transactional
    public List<WorkEffortPartyAssignmentEx> getWorkEffortPartyAssignmentList(UserPreference organization) {
        LOG.info("getWorkEffortPartyAssignmentList");

        List<WorkEffortPartyAssignmentEx> effortPartyAssignmentExList = this.workEffortPartyAssignmentMapper.getWorkEffortPartyAssignmentList(organization.getUserPrefValue());
        LOG.info("size = {}", effortPartyAssignmentExList.size());
        return effortPartyAssignmentExList;
    }

    /**
     * Gets a list of WorkEffortPartyAssignmentFilter.
     *
     * @return
     */
    @Transactional
    public List<WorkEffortPartyAssignmentEx> getWorkEffortPartyAssignmentListFilter(InfoPage infoPage, UserPreference organization) {
        LOG.info("getWorkEffortPartyAssignmentList");
        if(infoPage.getFilter().length == 0){
            infoPage.setFilters(null);
        }
        List<WorkEffortPartyAssignmentEx> effortPartyAssignmentExList = this.workEffortPartyAssignmentMapper.getWorkEffortPartyAssignmentListFilter( infoPage.getSecondaryLang(), infoPage.getFilter(), infoPage.getFilterGenericLabel(), organization.getUserPrefValue(), infoPage.getMatchModeSearch());
        LOG.info("size = {}", effortPartyAssignmentExList.size());
        return effortPartyAssignmentExList;
    }

    /**
     * This function creates a new record WorkEffortPartyAssignment.
     *
     * @param workEffortPartyAssignment WorkEffortPartyAssignment to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */

    @Transactional
    public boolean create(WorkEffortPartyAssignment workEffortPartyAssignment, String userLoginId) {
        LOG.info("create workEffortPartyAssignment");
        setCreatedTimestamp(workEffortPartyAssignment);
        workEffortPartyAssignment.setCreatedByUserLogin(userLoginId);
        int result = this.workEffortPartyAssignmentMapper.insert(workEffortPartyAssignment);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of WorkEffortPartyAssignment.
     *
     * @param workEffortPartyAssignment WorkEffortPartyAssignment to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(WorkEffortPartyAssignment workEffortPartyAssignment, String userLoginId) {
        LOG.info("update workEffortPartyAssignment");
        setUpdateTimestamp(workEffortPartyAssignment);
        workEffortPartyAssignment.setLastModifiedByUserLogin(userLoginId);
        int result = this.workEffortPartyAssignmentMapper.updateByPrimaryKey(workEffortPartyAssignment);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a WorkEffortPartyAssignment.
     *
     * @param workEffortPartyAssignment of class WorkEffortPartyAssignment to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(WorkEffortPartyAssignment workEffortPartyAssignment) {
        LOG.info("delete workEffortPartyAssignment");
        int result = this.workEffortPartyAssignmentMapper.deleteByPrimaryKey(workEffortPartyAssignment.getWorkEffortId(), workEffortPartyAssignment.getPartyId(), workEffortPartyAssignment.getRoleTypeId(), workEffortPartyAssignment.getFromDate());
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean deleteByWorkEffortId(String workEffortId) {
        LOG.info("deleteByWorkEffortId workEffortPartyAssignment");
        int result = this.workEffortPartyAssignmentMapper.deleteByWorkEffortId(workEffortId);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
