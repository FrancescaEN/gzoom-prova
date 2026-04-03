package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.JobLogJobExecParamsDao;
import it.mapsgroup.gzoom.mybatis.dto.JobLogJobExecParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class JobLogJobExecParamsService {

    private final JobLogJobExecParamsDao jobLogJobExecParamsDao;

    @Autowired
    public JobLogJobExecParamsService(JobLogJobExecParamsDao jobLogJobExecParamsDao) {
        this.jobLogJobExecParamsDao = jobLogJobExecParamsDao;
    }

    /**
     * This function get a jobLogJobExecParams.
     */
    public Result<JobLogJobExecParams> getJobLogJobExecParamsbyJobLogId(String jobLogId) {
        List<JobLogJobExecParams> list = this.jobLogJobExecParamsDao.getJobLogJobExecParamsbyJobLogId(jobLogId);
        return new Result<>(list, list.size());
    }

}
