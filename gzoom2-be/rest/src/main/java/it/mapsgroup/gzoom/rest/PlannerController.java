package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.QrtzJobDetails;
import it.mapsgroup.gzoom.mybatis.dto.QrtzTriggers;
import it.mapsgroup.gzoom.quartz.scheduler.dto.InfoBase;
import it.mapsgroup.gzoom.quartz.scheduler.info.JobData;
import it.mapsgroup.gzoom.quartz.scheduler.dto.ServiceJob;
import it.mapsgroup.gzoom.quartz.scheduler.service.PlannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leonardo Minaudo
 */
@RestController
@RequestMapping(value = "planner", produces = { MediaType.APPLICATION_JSON_VALUE })
public class PlannerController {
    private final PlannerService plannerService;

    @Autowired
    public PlannerController(PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    @GetMapping("/services")
    public ServiceJob getServiceList() {
        return this.plannerService.getServiceList();
    }

    @GetMapping("/job-details")
    public Result<QrtzJobDetails> getJobDetails() {
        return Exec.exec("get job-details", this.plannerService::getJobDetails);
    }

    @GetMapping("/triggers")
    public Result<QrtzTriggers> getTriggers() {
        return Exec.exec("get triggers", this.plannerService::getTriggers);
    }

    @GetMapping("/job-data/{jobName}")
    public JobData getJobData(@PathVariable("jobName") String jobName) {
        return Exec.exec("get job-data", () -> this.plannerService.getJobData(jobName));
    }

    @PutMapping("/set-description/{jobName}")
    public boolean updateDesc(@PathVariable String jobName, @RequestBody String description) {
        return Exec.exec("update job description", () -> this.plannerService.updateDesc(jobName, description));
    }

    @PostMapping("/create/job")
    public boolean createJob(@RequestBody InfoBase req){
        return Exec.exec("create job", () -> this.plannerService.createJob(req) );
    }

    @PostMapping("/update/trigger")
    public boolean updateTrigger(@RequestBody InfoBase req){
        return Exec.exec("update trigger", () -> this.plannerService.updateTrigger(req) );
    }

    @DeleteMapping("/{jobName}")
    public boolean deleteTimer(@PathVariable String jobName) {
        return this.plannerService.deleteJob(jobName);
    }
}
