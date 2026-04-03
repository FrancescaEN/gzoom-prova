package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.*;
import it.mapsgroup.gzoom.mybatis.mapper.JobLogJobExecParamsMapper;
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
public class JobLogJobExecParamsDao extends AbstractDao {
    private static final Logger LOG = getLogger(JobLogJobExecParamsDao.class);

    private final JobLogJobExecParamsMapper jobExecParamsMapper;

    @Autowired
    public JobLogJobExecParamsDao(JobLogJobExecParamsMapper jobExecParamsMapper) {
        this.jobExecParamsMapper = jobExecParamsMapper;
    }


    /**
     * Gets a list of JobLogJobExecParams.
     *
     * @return
     */
    @Transactional
    public List<JobLogJobExecParams> getJobLogJobExecParamsbyJobLogId(String jobLogId) {
        LOG.info("find all JobLogJobExecParams");

        List<JobLogJobExecParams> jobLogJobExecParamsList = this.jobExecParamsMapper.selectByJobLogId(jobLogId);
        LOG.info("size = {}", jobLogJobExecParamsList.size());
        return jobLogJobExecParamsList;
    }

}
