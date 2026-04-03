package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortStatus;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortStatusEx;
import it.mapsgroup.gzoom.service.WorkEffortStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "work-effort-status", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortStatusController {
	private final WorkEffortStatusService workEffortStatusService;

	@Autowired
	public WorkEffortStatusController(WorkEffortStatusService workEffortStatusService) {
		this.workEffortStatusService = workEffortStatusService;
	}


	@GetMapping("work-effort-status-ex/{workEffortId}")
	@ResponseBody
	public Result<WorkEffortStatusEx> getWorkEffort(@PathVariable String workEffortId) {
		return 	Exec.exec("get workEffort by id",() ->this.workEffortStatusService.getWorkEffortStatusEx(workEffortId));
	}

	@PostMapping
	@ResponseBody
	public WorkEffortStatus createWorkEffortStatus(@RequestBody WorkEffortStatus req){
		return Exec.exec("create work-effort-status", () -> this.workEffortStatusService.createWorkEffortStatus(req));
	}

	@PutMapping
	@ResponseBody
	public int updateWorkEffortStatus(@RequestBody WorkEffortStatus req){
		return Exec.exec("update work-effort-status", () -> this.workEffortStatusService.updateWorkEffortStatus(req));
	}
	
	@PostMapping(value = "delete/")
	@ResponseBody
	public Integer deleteWorkEffortStatus(@RequestBody WorkEffortStatus req ){
		return Exec.exec("delete work-effort-status", () -> this.workEffortStatusService.deleteWorkEffortStatus(req.getWorkEffortId(), req.getStatusId(), req.getStatusDatetime()));
	}


}
