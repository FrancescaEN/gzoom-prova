package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffort;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortEx;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortStatus;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortMapper;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortStatusMapper;
import it.mapsgroup.gzoom.mybatis.service.FilterService;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortDao extends AbstractDao {
    private static final Logger LOG = getLogger(WorkEffortDao.class);

    private final WorkEffortMapper workEffortMapper;
    private final WorkEffortStatusMapper workEffortStatusMapper;
    private final FilterService filterService;

    @Autowired
    public WorkEffortDao(WorkEffortMapper workEffortMapper, WorkEffortStatusMapper workEffortStatusMapper, FilterService filterService) {
        this.workEffortMapper = workEffortMapper;
        this.workEffortStatusMapper = workEffortStatusMapper;
        this.filterService = filterService;
    }

    public List<WorkEffort> findAll() {
        return this.workEffortMapper.selectAllOrderByWorkEffortName();
    }

    public WorkEffort findById(String workEffortId) {
        return this.workEffortMapper.selectByPrimaryKey(workEffortId);
    }

    public List<WorkEffort> selectByWorkEffortParentIdOrderByWorkEffortName(String workEffortParentId) {
        return this.workEffortMapper.selectByWorkEffortParentIdOrderByWorkEffortName(workEffortParentId);
    }

    ;

    public List<WorkEffort> selectWorkEffortsIsRootIsTemplate() {
        return this.workEffortMapper.selectWorkEffortsIsRootIsTemplate();
    }

    ;

    public List<WorkEffort> getWorkEffortParents(String workEffortParentId) {
        return this.workEffortMapper.getWorkEffortParents(workEffortParentId);
    }

    public List<WorkEffort> getParentsbyWorkEffort(String workEffortId) {
        return this.workEffortMapper.getParentsbyWorkEffort(workEffortId);
    }


    public List<WorkEffort> getWorkEfforts(String userLoginId, String parentTypeId, String useFilter, String[] workEffortTypeIds, String userPreferenceOrganizationUnitId) {
        return this.workEffortMapper.getWorkEfforts(userLoginId, parentTypeId, filterService.setMapFilter(userLoginId, ContextPermissionPrefixEnum.valueOf(parentTypeId)), useFilter.equals("Y"), workEffortTypeIds, userPreferenceOrganizationUnitId);
    }

    public WorkEffort getWorkEffort(String workEffortId) {
        return this.workEffortMapper.getWorkEffort(workEffortId);
    }

    public WorkEffortEx getWorkEffortEx(String workEffortId) {
        return this.workEffortMapper.getWorkEffortEx(workEffortId);
    }

    public List<WorkEffort> getWorkEffortsIsRootIsTemplate() {
        return this.workEffortMapper.getWorkEffortsIsRootIsTemplate();
    }

    public List<WorkEffortEx> getWorkEffortExList(String userPreferenceOrganizationUnitId) {
        List<WorkEffortEx> workEffortExList = this.workEffortMapper.getWorkEffortExList(userPreferenceOrganizationUnitId);
        LOG.info("size = {}", workEffortExList.size());
        return workEffortExList;
    }

    public List<WorkEffortEx> getWorkEffortExListPagination(String userPreferenceOrganizationUnitId, InfoPage infoPage) {

        if(infoPage.getFilter() != null){
            for(Filter item: infoPage.getFilter()){
                if(item.getValue() != null){
                    if(item.getField().equals("estimatedStartDate") || item.getField().equals("estimatedCompletionDate")){
                        Instant instant = Instant.parse(item.getValue());
                        item.setDateValue(instant);
                    }
                }
            }
        }

        List<WorkEffortEx> workEffortExList = this.workEffortMapper.getWorkEffortExListPagination(infoPage.getLimit(), infoPage.getOffset(), infoPage.getFilter(), infoPage.getFilterGenericLabel(), infoPage.getSortOrder(), infoPage.getSortField(), userPreferenceOrganizationUnitId, infoPage.getMatchModeSearch(), infoPage.getSecondaryLang());
        LOG.info("size = {}", workEffortExList.size());
        return workEffortExList;
    }

    public List<WorkEffort> getWorkEffortByOrgId(String userPreferenceOrganizationUnitId) {
        List<WorkEffort> workEffortList = this.workEffortMapper.getWorkEffortByOrgId(userPreferenceOrganizationUnitId);
        LOG.info("size = {}", workEffortList.size());
        return workEffortList;
    }

    public List<WorkEffortEx> getWorkEffortDropdown(String userPreferenceOrganizationUnitId) {
        List<WorkEffortEx> workEffortList = this.workEffortMapper.getWorkEffortDropdown(userPreferenceOrganizationUnitId);
        LOG.info("size = {}", workEffortList.size());
        return workEffortList;
    }
    @Transactional
    public WorkEffort create(WorkEffort workEffort, String userLoginId, String userPreferenceOrganizationUnitId) {
        workEffort.setCreatedByUserLogin(userLoginId);
        workEffort.setOrganizationId(userPreferenceOrganizationUnitId);
        this.workEffortMapper.insert(workEffort);
        WorkEffortStatus workEffortStatus = new WorkEffortStatus();
        workEffortStatus.setWorkEffortId(workEffort.getWorkEffortId());
        workEffortStatus.setStatusId(workEffort.getCurrentStatusId());
        workEffortStatus.setStatusDatetime(Instant.now());
        workEffortStatus.setCreatedByUserLogin(userLoginId);
        this.workEffortStatusMapper.insert(workEffortStatus);
        return workEffort;
    }
    @Transactional
    public int update(WorkEffort workEffort, String userLoginId, String userPreferenceOrganizationUnitId) {
        LOG.info("WorkEffort update");
        return this.workEffortMapper.updateByPrimaryKey(workEffort);
    }

    @Transactional
    public int delete(String workEffortId) {
        WorkEffort workEffort = this.workEffortMapper.getWorkEffort(workEffortId);
        this.workEffortStatusMapper.deleteAllByWorkEffortId(workEffort.getWorkEffortId());
        return this.workEffortMapper.deleteByPrimaryKey(workEffortId);
    }

    @Transactional
    public int anonymizeWorkEffort(Instant expirationDate) {
        LOG.info("anonymizeWorkEffort update");
        int result = this.workEffortMapper.anonymizeWorkEffort(expirationDate);
        LOG.info("result = {}", result);
        return result;
    }
}
