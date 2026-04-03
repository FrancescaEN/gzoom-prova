package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.JobLogJobExecParams;
import it.mapsgroup.gzoom.service.JobLogJobExecParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "job-log-job-exec-params", produces = { MediaType.APPLICATION_JSON_VALUE })
public class JobLogJobExecParamsController {
    private final JobLogJobExecParamsService jobLogJobExecParamsService;

    @Autowired
    public JobLogJobExecParamsController(JobLogJobExecParamsService jobLogJobExecParamsService) {
        this.jobLogJobExecParamsService = jobLogJobExecParamsService;
    }

    @GetMapping(value = "/{jobLogId}")
    @ResponseBody
    public Result<JobLogJobExecParams> getJobLogJobExecParamsbyJobLogId(@PathVariable(value = "jobLogId") String jobLogId){
        return Exec.exec("get JobLogJobExecParams", () -> this.jobLogJobExecParamsService.getJobLogJobExecParamsbyJobLogId(jobLogId));
    }


}
