package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.CustomTimePeriod;
import it.mapsgroup.gzoom.mybatis.mapper.CustomTimePeriodMapper;
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
public class CustomTimePeriodDao extends AbstractDao{

    private static final Logger LOG = getLogger(CustomTimePeriodDao.class);
    private final CustomTimePeriodMapper customTimePeriodMapper;


    @Autowired
    public CustomTimePeriodDao(CustomTimePeriodMapper customTimePeriodMapper) {
        this.customTimePeriodMapper = customTimePeriodMapper;
    }

    @Transactional
    public List<CustomTimePeriod> selectByPeriodTypeIdOrderByThruDate(String periodTypeId) {
        LOG.info("find customTimePeriod by periodTypeId order by thruDate");

        List<CustomTimePeriod> customTimePeriods = this.customTimePeriodMapper.selectByPeriodTypeIdOrderByThruDate(periodTypeId);
        LOG.info("size = {}", customTimePeriods.size());
        return customTimePeriods;
    }

    /**
     * This function gets a customTimePeriod given its id.
     *
     * @param customTimePeriodId data resource type id of the customTimePeriod
     * @return the corresponding record customTimePeriod
     */
    @Transactional
    public CustomTimePeriod get(String customTimePeriodId) {
        LOG.info("find customTimePeriod by id");

        CustomTimePeriod customTimePeriod = this.customTimePeriodMapper.selectByPrimaryKey(customTimePeriodId);
        LOG.info("CustomTimePeriod = {}", (customTimePeriod != null));
        return customTimePeriod;
    }


    /**
     * Gets a list of customTimePeriod.
     *
     * @return
     */
    @Transactional
    public List<CustomTimePeriod> selectAllOrderByPrimaryKey() {
        LOG.info("find all customTimePeriod");

        List<CustomTimePeriod> customTimePeriods = this.customTimePeriodMapper.selectAllOrderByPrimaryKey();
        LOG.info("size = {}", customTimePeriods.size());
        return customTimePeriods;
    }


    /**
     * Gets a list of customTimePeriod filtered by TimesheetId
     *
     * @return
     */
    @Transactional
    public List<CustomTimePeriod> getTimesheetCustomTimePeriodDropdownFilter(String userLoginId){

    return this.customTimePeriodMapper.getTimesheetCustomTimePeriodDropdownFilter(userLoginId);
    }

    /**
     * This function creates a new record customTimePeriod.
     *
     * @param customTimePeriod customTimePeriod to add
     * @param userLoginId User login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean create(CustomTimePeriod customTimePeriod, String userLoginId) {
        LOG.info("create customTimePeriod");
        setCreatedTimestamp(customTimePeriod);
        customTimePeriod.setCreatedByUserLogin(userLoginId);
        int result = this.customTimePeriodMapper.insert(customTimePeriod);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     * Update of customTimePeriod.
     *
     * @param customTimePeriod customTimePeriod to update
     * @param userLoginId user login id
     * @return true if the operation was successful
     */
    @Transactional
    public boolean update(CustomTimePeriod customTimePeriod, String userLoginId) {
        LOG.info("update customTimePeriod");
        setUpdateTimestamp(customTimePeriod);
        customTimePeriod.setLastModifiedByUserLogin(userLoginId);
        int result = this.customTimePeriodMapper.updateByPrimaryKey(customTimePeriod);
        LOG.info("result = {}", result);
        return result > 0;
    }

    /**
     *  Deletes a customTimePeriod.
     *
     * @param id id of the customTimePeriod to be deleted
     * @return true if the operation was successful
     */
    @Transactional
    public boolean delete(String id) {
        LOG.info("delete customTimePeriod");
        int result = this.customTimePeriodMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public CustomTimePeriod getCustomTimePeriodForIndicatorMovement(String acctgTransId, String acctgTransEntrySeqId) {
        LOG.info("find customTimePeriod by acctgTransId and acctgTransEntrySeqId");
        CustomTimePeriod customTimePeriod = this.customTimePeriodMapper.getCustomTimePeriodForIndicatorMovement(acctgTransId, acctgTransEntrySeqId);
        LOG.info("customTimePeriod = {}", (customTimePeriod != null));
        return customTimePeriod;
    }
}
