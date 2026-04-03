package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.*;
import it.mapsgroup.gzoom.mybatis.mapper.JobLogLogMapper;
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
public class JobLogLogDao extends AbstractDao {
    private static final Logger LOG = getLogger(JobLogLogDao.class);
    private final JobLogLogMapper jobLogLogMapper;

    @Autowired
    public JobLogLogDao(JobLogLogMapper jobLogLogMapper) {
        this.jobLogLogMapper = jobLogLogMapper;
    }

    /**
     * Gets a list of jobLogLogEx.
     *
     * @return
     */
    @Transactional
    public List<JobLogLogEx> getJobLogLogEx(String jobLogId) {
        LOG.info("find all JobLogLog");

        List<JobLogLogEx> jobLogLogExList = this.jobLogLogMapper.getJobLogLogEx(jobLogId);
        LOG.info("size = {}", jobLogLogExList.size());
        return jobLogLogExList;
    }


}
