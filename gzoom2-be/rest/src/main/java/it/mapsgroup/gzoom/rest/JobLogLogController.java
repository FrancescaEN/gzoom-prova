package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.JobLogLogEx;
import it.mapsgroup.gzoom.service.JobLogLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "job-log-log", produces = { MediaType.APPLICATION_JSON_VALUE })
public class JobLogLogController {
    private final JobLogLogService jobLogLogService;

    @Autowired
    public JobLogLogController(JobLogLogService jobLogLogService) {
        this.jobLogLogService = jobLogLogService;
    }

    @GetMapping(value = "/{jobLogId}")
    @ResponseBody
    public Result<JobLogLogEx> getJobLogLogEx(@PathVariable(value = "jobLogId") String jobLogId){
        return Exec.exec("get JobLogJobExecParams", () -> this.jobLogLogService.getJobLogLogEx(jobLogId));
    }
}
