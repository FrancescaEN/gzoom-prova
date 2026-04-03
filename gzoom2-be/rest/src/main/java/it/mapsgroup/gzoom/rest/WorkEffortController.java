package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffort;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortEx;
import it.mapsgroup.gzoom.service.WorkEffortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static it.mapsgroup.gzoom.security.Principals.principal;

@RestController
@RequestMapping(value = "work-effort", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortController {
	private final WorkEffortService workEffortService;

	@Autowired
	public WorkEffortController(WorkEffortService workEffortService) {
		this.workEffortService = workEffortService;
	}

	@RequestMapping(value = "/{parentTypeId}/{workEffortTypeId}/{useFilter}", method = RequestMethod.GET)
	@ResponseBody
	public Result<WorkEffort> getWorkEfforts(@PathVariable(value = "parentTypeId") String parentTypeId, @PathVariable(value = "workEffortTypeId") String workEffortTypeId, @PathVariable(value = "useFilter") String useFilter) {
		String[] workEffortTypeIds = workEffortTypeId.split(",");
		return Exec.exec("workEffortTypeId get", () -> workEffortService.getWorkEfforts(principal().getUserLoginId(), parentTypeId, workEffortTypeIds, useFilter));
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public Result<WorkEffort> getWorkEfforts() {
		return Exec.exec("workEfforts get", () -> workEffortService.getWorkEfforts());
	}

	@GetMapping("/{workEffortId}")
	@ResponseBody
	public WorkEffort getWorkEffort(@PathVariable String workEffortId) {
		return 	Exec.exec("get workEffort by id",() ->this.workEffortService.getWorkEffort(workEffortId));
	}

	@GetMapping("/work-effort-ex/{workEffortId}")
	@ResponseBody
	public WorkEffortEx getWorkEffortEx(@PathVariable String workEffortId) {
		return 	Exec.exec("get workEffort by id",() ->this.workEffortService.getWorkEffortEx(workEffortId));
	}

	@RequestMapping(value = "/work-effort-isRoot-isTemplate", method = RequestMethod.GET)
	@ResponseBody
	public Result<WorkEffort> getWorkEffortsIsRootIsTemplate() {
		return Exec.exec("workEfforts get", () -> workEffortService.getWorkEffortsIsRootIsTemplate());
	}
	
	
	@RequestMapping(value = "/work-effort-parent/{workEffortParentId}", method = RequestMethod.GET)
	@ResponseBody
	public Result<WorkEffort> getWorkEffortParents(@PathVariable(value = "workEffortParentId") String workEffortParentId) {
		return Exec.exec("workEffortParents get", () -> workEffortService.getWorkEffortParents(workEffortParentId));
	}

	@RequestMapping(value = "/work-effort-ex", method = RequestMethod.GET)
	@ResponseBody
	public Result<WorkEffortEx> getWorkEffortExList() {
		return Exec.exec("workEffortEx get", () -> workEffortService.getWorkEffortExList(principal().getUserLoginId()));
	}

	@RequestMapping(value = "/work-effort-ex/pagination", method = RequestMethod.POST)
	@ResponseBody
	public Result<WorkEffortEx> getWorkEffortExListPagination(@RequestBody InfoPage infoPage) {
		return Exec.exec("workEffortEx pagination get", () -> workEffortService.getWorkEffortExListPagination(principal().getUserLoginId(), infoPage));
	}

	@RequestMapping(value = "/work-effort-by-org-id", method = RequestMethod.GET)
	@ResponseBody
	public Result<WorkEffort> getWorkEffortByOrgId() {
		return Exec.exec("WorkEffortByOrgId get", () -> workEffortService.getWorkEffortByOrgId(principal().getUserLoginId()));
	}


	@GetMapping(value = "/work-effort-dropdown/{organizationId}")
	@ResponseBody
	public Result<WorkEffortEx> getWorkEffortDropdown(@PathVariable(value = "organizationId") String organizationId){
		return Exec.exec("get work-effort-view", () -> this.workEffortService.getWorkEffortDropdown(organizationId) );
	}

	@PostMapping
	@ResponseBody
	public WorkEffort createWorkEffort(@RequestBody WorkEffort req){
		return Exec.exec("create work-effort", () -> this.workEffortService.createWorkEffort(req));
	}

	@PutMapping
	@ResponseBody
	public int updateWorkEffort(@RequestBody WorkEffort req){
		return Exec.exec("update work-effort", () -> this.workEffortService.updateWorkEffort(req));
	}

	@DeleteMapping(value = "/{workEffortId}")
	@ResponseBody
	public int deleteWorkEffort(@PathVariable(value = "workEffortId") String id){
		return Exec.exec("delete work-effort", () -> this.workEffortService.deleteWorkEffort(id));
	}

	@DeleteMapping(value = "deleteWorkEffortTree/{workEffortId}")
	@ResponseBody
	public boolean deleteWorkEffortTree(@PathVariable(value = "workEffortId") String id){
		return Exec.exec("delete work-effort", () -> this.workEffortService.deleteWorkEffortTree(id));
	}

}
