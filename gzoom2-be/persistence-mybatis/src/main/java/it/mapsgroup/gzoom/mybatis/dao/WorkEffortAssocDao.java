package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssoc;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssocEx;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortAssocMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortAssocDao extends AbstractDao{

    private static final Logger LOG = getLogger(WorkEffortPartyAssignmentDao.class);
    private final WorkEffortAssocMapper workEffortAssocMapper;

    @Autowired
    public WorkEffortAssocDao(WorkEffortAssocMapper workEffortAssocMapper) {
        this.workEffortAssocMapper = workEffortAssocMapper;
    }

    public List<WorkEffortAssoc> findAll() {
        return this.workEffortAssocMapper.selectAll(); }

    public int getTotale(InfoPage infoPage) {
        if(infoPage.getFilter().length == 0){
            infoPage.setFilters(null);
        }

        boolean control = false;
        if(infoPage.getFilter() != null){
            for(Filter item: infoPage.getFilter()){
                if(item.getValue() != null){
                    control = true;
                    if(item.getField().equals("fromDate") || item.getField().equals("thruDate")){
                        Instant instant = Instant.parse(item.getValue());
                        item.setDateValue(instant);
                    }

                    if(item.getField().equals("sequenceNum") ){
                        item.setBigDecimalValue(new BigDecimal(item.getValue()));
                    }
                    if(item.getField().equals("assocWeight")){
                        item.setDoubleValue(Double.parseDouble(item.getValue()));
                    }
                }
            }
        }
        return this.workEffortAssocMapper.getTotale(control, infoPage.getOrganizationId(), infoPage.getLimit(), infoPage.getOffset(), infoPage.getFilter(), infoPage.getFilterGenericLabel(), infoPage.getSortOrder(), infoPage.getSortField(), infoPage.getMatchModeSearch(), infoPage.getSecondaryLang());
    }


    public List<WorkEffortAssocEx> getWorkEffortAssocPagination(InfoPage infoPage) {

        if(infoPage.getFilter().length == 0){
            infoPage.setFilters(null);
        }

        boolean control = false;
        if(infoPage.getFilter() != null){
            for(Filter item: infoPage.getFilter()){
                if(item.getValue() != null){
                    control = true;
                    if(item.getField().equals("fromDate") || item.getField().equals("thruDate")){
                        Instant instant = Instant.parse(item.getValue());
                        item.setDateValue(instant);
                    }

                    if(item.getField().equals("sequenceNum") ){
                        item.setBigDecimalValue(new BigDecimal(item.getValue()));
                    }
                    if(item.getField().equals("assocWeight")){
                        item.setDoubleValue(Double.parseDouble(item.getValue()));
                    }
                }
            }
        }
        return this.workEffortAssocMapper.getWorkEffortAssocPagination(control, infoPage.getOrganizationId(), infoPage.getLimit(), infoPage.getOffset(), infoPage.getFilter(), infoPage.getFilterGenericLabel(), infoPage.getSortOrder(), infoPage.getSortField(), infoPage.getMatchModeSearch(), infoPage.getSecondaryLang()); }

    public WorkEffortAssoc findById(String workEffortIdFrom, String workEffortIdTo, String workEffortAssocTypeId, Instant fromDate) {
        return this.workEffortAssocMapper.selectByPrimaryKey(workEffortIdFrom, workEffortIdTo, workEffortAssocTypeId, fromDate); }
    @Transactional
    public boolean create(WorkEffortAssoc workEffortAssoc, String userLoginId) {
        this.workEffortAssocMapper.insert(workEffortAssoc);
        return true;
    }
    @Transactional
    public int update(WorkEffortAssoc workEffortAssoc, String userLoginId) {
        return this.workEffortAssocMapper.updateByPrimaryKey(workEffortAssoc);}

    @Transactional
    public int delete(String workEffortIdFrom, String workEffortIdTo, String workEffortAssocTypeId, Instant fromDate) {
        return this.workEffortAssocMapper.deleteByPrimaryKey(workEffortIdFrom, workEffortIdTo, workEffortAssocTypeId, fromDate);}

    @Transactional
    public int deleteByWorkEffortId(String workEffortId) {
        LOG.info("deleteByWorkEffortId workEffortAssoc");
        return this.workEffortAssocMapper.deleteByWorkEffortId(workEffortId);}
}
