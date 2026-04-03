package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.mybatis.dto.*;
import it.mapsgroup.gzoom.mybatis.mapper.JobLogJobExecParamsMapper;
import it.mapsgroup.gzoom.mybatis.mapper.JobLogLogMapper;
import it.mapsgroup.gzoom.mybatis.mapper.JobLogMapper;
import it.mapsgroup.gzoom.mybatis.mapper.JobLogServiceTypeMapper;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class JobLogDao extends AbstractDao {
    private static final Logger LOG = getLogger(JobLogDao.class);
    private final SequenceGenerator sequenceGenerator;
    private final JobLogMapper jobLogMapper;
    private final JobLogLogMapper jobLogLogMapper;
    private final JobLogServiceTypeMapper jobLogServiceTypeMapper;
    private final JobLogJobExecParamsMapper jobLogJobExecParamsMapper;

    @Autowired
    public JobLogDao(SequenceGenerator sequenceGenerator, JobLogMapper jobLogMapper, JobLogLogMapper jobLogLogMapper, JobLogServiceTypeMapper jobLogServiceTypeMapper, JobLogJobExecParamsMapper jobLogJobExecParamsMapper) {
        this.sequenceGenerator = sequenceGenerator;
        this.jobLogMapper = jobLogMapper;
        this.jobLogLogMapper = jobLogLogMapper;
        this.jobLogServiceTypeMapper = jobLogServiceTypeMapper;
        this.jobLogJobExecParamsMapper = jobLogJobExecParamsMapper;
    }

    /**
     * Gets a list of jobLogEx.
     *
     * @return
     */
    @Transactional
    public List<JobLogEx> getJobLogEx(InfoPage infoPage) {
        LOG.info("find all JobLog");

        if(infoPage.getFilter().length == 0){
            infoPage.setFilters(null);
        }

        if(infoPage.getFilter() != null){
            for(Filter item: infoPage.getFilter()){
                if(item.getValue() != null){
                    if(item.getField().equals("logDate") || item.getField().equals("logEndDate")){
                        Instant instant = Instant.parse(item.getValue());
                        item.setDateValue(instant);
                    }

                    if(item.getField().equals("recordElaborated") ){
                        item.setBigDecimalValue(new BigDecimal(item.getValue()));
                    }
                    if(item.getField().equals("blockingErrors") ){
                        item.setBigDecimalValue(new BigDecimal(item.getValue()));
                    }
                    if(item.getField().equals("warningMessages") ){
                        item.setBigDecimalValue(new BigDecimal(item.getValue()));
                    }
                }
            }
        }

        List<JobLogEx> jobLogExList = this.jobLogMapper.getJobLogEx(infoPage.getLimit(), infoPage.getOffset(), infoPage.getFilter(), infoPage.getSortOrder(), infoPage.getSortField(), infoPage.getMatchModeSearch(), infoPage.getSecondaryLang());
        LOG.info("size = {}", jobLogExList.size());
        return jobLogExList;
    }
    /**
     * Gets a list of jobLog by id.
     *
     * @return
     */
    @Transactional
    public JobLog getJobLogById(String jobLogId) {
        LOG.info("find JobLog by id");
        JobLog jobLog = this.jobLogMapper.selectByPrimaryKey(jobLogId);
        LOG.info("jobLog = {}", jobLog != null);
        return jobLog;
    }


    @Transactional
    public boolean addJobLog(JobLog record, String username) {
        setCreatedTimestamp(record);
        record.setUserLoginId(username);
        record.setCreatedByUserLogin(username);
        LOG.info("Add JobLog: {}", record);
        int result = this.jobLogMapper.insert(record);
        LOG.info("result: {}", result);
        return result > 0;
    }

    @Transactional
    public boolean updateJobLogEndDate(JobLog record) {
        setUpdateTimestamp(record);
        LOG.info("Update LongEndDate JobLog: {}", record);
        int i = this.jobLogMapper.updateByPrimaryKey(record);
        LOG.info("updated records: {}", i);
        return i > 0;
    }

    @Transactional
    public boolean addJobLogLog(JobLogLog record) {
        record.setJobLogLogId(sequenceGenerator.getNextSeqId("JobLogLog"));
        setCreatedTimestamp(record);
        LOG.info("Add JobLogLog: {}", record);
        int i = this.jobLogLogMapper.insert(record);
        LOG.info("created records: {}", i);
        return i > 0;
    }

    @Transactional
    public boolean addJobLogJobExecParams(JobLogJobExecParams record, String username) {
        record.setCreatedByUserLogin(username);
        setCreatedTimestamp(record);
        LOG.info("Add JobLogJobExecParams: {}", record);
        int i = this.jobLogJobExecParamsMapper.insert(record);
        LOG.info("created records: {}", i);
        return i > 0;
    }

    @Transactional
    public boolean addJobLogServiceType(JobLogServiceType record) {
        setCreatedTimestamp(record);
        LOG.info("Add JobLogServiceType: {}", record);
        int i = this.jobLogServiceTypeMapper.insert(record);
        LOG.info("created records: {}", i);
        return i > 0;
    }
}
