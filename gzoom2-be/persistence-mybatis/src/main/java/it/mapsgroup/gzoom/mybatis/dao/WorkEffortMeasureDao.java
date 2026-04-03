package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.mybatis.dto.GlAccount;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortMeasExUom;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortMeasure;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortMeasureMapper;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortMeasureDao extends AbstractDao{
    private static final Logger LOG = getLogger(WorkEffortMeasureDao.class);


    private final WorkEffortMeasureMapper workEffortMeasureMapper;
    private final SequenceGenerator sequenceGenerator;

    @Autowired
    public WorkEffortMeasureDao(WorkEffortMeasureMapper workEffortMeasureMapper, SequenceGenerator sequenceGenerator) {
        this.workEffortMeasureMapper = workEffortMeasureMapper;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Transactional
    public List<WorkEffortMeasure> findAll() {
        LOG.info("find all workEffortMeasure");
        List<WorkEffortMeasure> workEffortMeasureList = this.workEffortMeasureMapper.selectAll();
        LOG.info("size = {}", workEffortMeasureList.size());
        return workEffortMeasureList;
    }

    @Transactional
    public WorkEffortMeasure findById(String workEffortMeasureId) {
        LOG.info("find workEffortMeasure by id");
        WorkEffortMeasure workEffortMeasure = this.workEffortMeasureMapper.selectByPrimaryKey(workEffortMeasureId);
        LOG.info("WorkEffortMeasure = {}", (workEffortMeasure != null));
        return workEffortMeasure;
    }

    @Transactional
    public List<WorkEffortMeasure> getWorkEffortMeasureByWorkEffortId(String workEffortId) {
        LOG.info("find workEffortMeasure by workEffortId");
        List<WorkEffortMeasure> workEffortMeasureList = this.workEffortMeasureMapper.getWorkEffortMeasurebyWorkEffortId(workEffortId);
        LOG.info("workEffortMeasureList = {}", (workEffortMeasureList.size()));
        return workEffortMeasureList;
    }

    @Transactional
    public List<WorkEffortMeasure> getWeMeasureEvalId(String workEffortIdFrom) {
        LOG.info("find workEffortMeasure by weMeasureEvalId");
        List<WorkEffortMeasure> workEffortMeasureList = this.workEffortMeasureMapper.getWeMeasureEvalId(workEffortIdFrom);
        LOG.info("size = {}", workEffortMeasureList.size());
        return workEffortMeasureList;
    }

    @Transactional
    public List<WorkEffortMeasExUom> getWorkEffortMeasureList() {
        LOG.info("find WorkEffortMeasExUom");
        List<WorkEffortMeasExUom> workEffortMeasureList = this.workEffortMeasureMapper.getWorkEffortMeasureList();
        LOG.info("size = {}", workEffortMeasureList.size());
        return workEffortMeasureList;
    }

    @Transactional
    public List<WorkEffortMeasure> getWorkEffortMeasureJoinWorkEffort(String query, boolean secondaryLang) {
        LOG.info("getWorkEffortMeasureJoinWorkEffort");
        List<WorkEffortMeasure> workEffortMeasureList = this.workEffortMeasureMapper.getWorkEffortMeasureJoinWorkEffort(query, secondaryLang);
        LOG.info("size = {}", workEffortMeasureList.size());
        return workEffortMeasureList;
    }

    @Transactional
    public WorkEffortMeasExUom getWorkEffortMeasureExUomById(String workEffortMeasureId) {
        LOG.info("find WorkEffortMeasExUom by id");
        WorkEffortMeasExUom workEffortMeasExUom = this.workEffortMeasureMapper.getWorkEffortMeasureId(workEffortMeasureId);
        LOG.info("WorkEffortMeasExUom = {}", (workEffortMeasExUom != null));
        return workEffortMeasExUom;
    }

    public int getTotale(InfoPage infoPage) {
        if(infoPage.getFilter().length == 0){
            infoPage.setFilters(null);
        }

        boolean control = false;
        if(infoPage.getFilter() != null){
            for(Filter item: infoPage.getFilter()){
                if(item.getValue() != null){
                    control = true;
                }
            }
        }

        return this.workEffortMeasureMapper.getTotale(control, infoPage.getLimit(), infoPage.getOffset(), infoPage.getFilter(), infoPage.getFilterGenericLabel(), infoPage.getSortOrder(), infoPage.getSortField(), infoPage.getOrganizationId(), infoPage.getMatchModeSearch(), infoPage.getSecondaryLang());
    }


    @Transactional
    public List<WorkEffortMeasExUom> getWorkEffortMeasureListPagination(InfoPage infoPage) {

        if(infoPage.getFilter().length == 0){
            infoPage.setFilters(null);
        }

        boolean control = false;
        if(infoPage.getFilter() != null){
            for(Filter item: infoPage.getFilter()){
                if(item.getValue() != null){
                    control = true;
                }
            }
        }

        return this.workEffortMeasureMapper.getWorkEffortMeasureListPagination(control, infoPage.getLimit(), infoPage.getOffset(), infoPage.getFilter(), infoPage.getFilterGenericLabel(), infoPage.getSortOrder(), infoPage.getSortField(), infoPage.getOrganizationId(), infoPage.getMatchModeSearch(), infoPage.getSecondaryLang());
    }

    @Transactional
    public String create(WorkEffortMeasure workEffortMeasure, String userLoginId) {
        workEffortMeasure.setCreatedByUserLogin(userLoginId);

        String newId = this.sequenceGenerator.getNextSeqId("WorkEffortMeasure");
        workEffortMeasure.setWorkEffortMeasureId(newId);

        this.workEffortMeasureMapper.insert(workEffortMeasure);
        return workEffortMeasure.getWorkEffortMeasureId();
    }

    @Transactional
    public boolean update(WorkEffortMeasure workEffortMeasure, String userLoginId) {
        LOG.info("update workEffortMeasure");
        setUpdateTimestamp(workEffortMeasure);
        workEffortMeasure.setLastModifiedByUserLogin(userLoginId);
        int result = this.workEffortMeasureMapper.updateByPrimaryKey(workEffortMeasure);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean updateWEMeasureFromGlAccount(GlAccount glAccount, String userLoginId) {
        LOG.info("update workEffortMeasure");
        WorkEffortMeasure workEffortMeasure = new WorkEffortMeasure();
        workEffortMeasure.setGlAccountId(glAccount.getGlAccountId());
        workEffortMeasure.setPeriodTypeId(glAccount.getPeriodTypeId());
        workEffortMeasure.setWeWithoutPerf(glAccount.getWeWithoutPerf());
        workEffortMeasure.setWeScoreConvEnumId(glAccount.getWeScoreConvEnumId());
        workEffortMeasure.setWeScoreRangeEnumId(glAccount.getWeScoreRangeEnumId());
        workEffortMeasure.setWeMeasureTypeEnumId(glAccount.getWeMeasureTypeEnumId());
        workEffortMeasure.setUomRangeId(glAccount.getUomRangeId());
        setUpdateTimestamp(workEffortMeasure);
        workEffortMeasure.setLastModifiedByUserLogin(userLoginId);
        int result = this.workEffortMeasureMapper.updateWEMeasureFromGlAccount(workEffortMeasure);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String workEffortMeasureId) {
        LOG.info("delete workEffortMeasure");
        int result = this.workEffortMeasureMapper.deleteByPrimaryKey(workEffortMeasureId);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public List<WorkEffortMeasure> dropdownWorkEffortMeasure(String organizationId) {
        LOG.info("dropdownWorkEffortMeasure");
        List<WorkEffortMeasure> workEffortMeasureList = this.workEffortMeasureMapper.dropdownWorkEffortMeasure(organizationId);
        LOG.info("size = {}", workEffortMeasureList.size());
        return workEffortMeasureList;
    }

    @Transactional
    public List<WorkEffortMeasure> dropdownWorkEffortMeasureWEA(String organizationId) {
        LOG.info("dropdownWorkEffortMeasure");
        List<WorkEffortMeasure> workEffortMeasureList = this.workEffortMeasureMapper.dropdownWorkEffortMeasureWEA(organizationId);
        LOG.info("size = {}", workEffortMeasureList.size());
        return workEffortMeasureList;
    }
}
