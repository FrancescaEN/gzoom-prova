package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.JobLogLogDao;
import it.mapsgroup.gzoom.mybatis.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class JobLogLogService {

    private final JobLogLogDao jobLogLogDao;

    @Autowired
    public JobLogLogService(JobLogLogDao jobLogLogDao) {
        this.jobLogLogDao = jobLogLogDao;
    }

    /**
     * This function get a jobLogLogEx.
     */
    public Result<JobLogLogEx> getJobLogLogEx(String jobLogId) {
        List<JobLogLogEx> list = this.jobLogLogDao.getJobLogLogEx(jobLogId);
        return new Result<>(list, list.size());
    }


}
