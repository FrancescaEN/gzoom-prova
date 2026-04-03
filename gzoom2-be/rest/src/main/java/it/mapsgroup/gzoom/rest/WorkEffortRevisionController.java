package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortRevision;
import it.mapsgroup.gzoom.service.WorkEffortRevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortRevisionController {
	private final WorkEffortRevisionService workEffortRevisionService;

	@Autowired
	public WorkEffortRevisionController(WorkEffortRevisionService workEffortRevisionService) {
		this.workEffortRevisionService = workEffortRevisionService;
	}

	@RequestMapping(value = "work-effort-revision/{workEffortTypeId}", method = RequestMethod.GET)
	@ResponseBody
	public Result<WorkEffortRevision> getRoleTypes(@PathVariable(value = "workEffortTypeId") String workEffortTypeId) {
		return Exec.exec("work-effort-revision get", () -> workEffortRevisionService.getWorkEffortRevisions(workEffortTypeId));
	}
}
