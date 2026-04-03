package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.JobLog;
import it.mapsgroup.gzoom.mybatis.dto.JobLogEx;
import it.mapsgroup.gzoom.service.JobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "job-log", produces = { MediaType.APPLICATION_JSON_VALUE })
public class JobLogController {
    private final JobLogService jobLogService;

    @Autowired
    public JobLogController(JobLogService jobLogService) {
        this.jobLogService = jobLogService;
    }

    @RequestMapping(value = "/pagination", method = RequestMethod.POST)
    @ResponseBody
    public Result<JobLogEx>getJobLogEx(@RequestBody InfoPage infoPage){
        return Exec.exec("get job-log-pagination", () -> this.jobLogService.getJobLogEx(infoPage) );
    }

    @GetMapping(value = "/{jobLogId}")
    @ResponseBody
    public JobLog getJobLogById(@PathVariable String jobLogId){
        return Exec.exec("get job-log by id", ()-> this.jobLogService.getJobLogById(jobLogId));
    }
}
