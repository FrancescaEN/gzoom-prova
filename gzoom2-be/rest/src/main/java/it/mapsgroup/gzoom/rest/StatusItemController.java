package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.mybatis.dto.StatusItem;
import it.mapsgroup.gzoom.mybatis.dto.StatusItemExType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.StatusItemExt;
import it.mapsgroup.gzoom.service.StatusItemService;

import static it.mapsgroup.gzoom.security.Principals.principal;

@RestController
@RequestMapping(value = "status-item", produces = { MediaType.APPLICATION_JSON_VALUE })
public class StatusItemController {

	private final StatusItemService statusItemService;

	@Autowired
	public StatusItemController(StatusItemService statusItemService) {
		this.statusItemService = statusItemService;
	}

	@RequestMapping(value = "/{parentTypeId}", method = RequestMethod.GET)
	@ResponseBody
	public Result<StatusItemExt> getStatusItems(@PathVariable(value = "parentTypeId") String parentTypeId) {
		return Exec.exec("status-items get", () -> statusItemService.getStatusItems(parentTypeId));
	}

	@RequestMapping(value = "/code/{statusCode}", method = RequestMethod.GET)
	@ResponseBody
	public Result<StatusItem> getStatusItemByCode(@PathVariable String statusCode) {
		return Exec.exec("status-items get by code", () -> statusItemService.getStatusItembyCode(statusCode));
	}

	@RequestMapping(value = "/status-dropdown-filter", method = RequestMethod.GET)
	@ResponseBody
	public Result<StatusItem> getTimesheetStatusDropdownFilter() {
		return Exec.exec("status-items get", () -> statusItemService.getTimesheetStatusDropdownFilter(principal().getUserLoginId()));
	}

	@GetMapping("/status-type-id/{statusTypeId}")
	@ResponseBody
	public Result<StatusItem> getStatusItemList(@PathVariable String statusTypeId){
		return Exec.exec("get status item list by status type id", () -> this.statusItemService.getStatusItemByStatusTypeId(statusTypeId));
	}

	@GetMapping("/state-to")
	@ResponseBody
	public Result<StatusItemExType> getStatusItemStateTo(){
		return Exec.exec("getStatusItemStateTo", () -> this.statusItemService.getStatusItemStateTo());
	}

	@GetMapping("/state-from/{statusTypeId}")
	@ResponseBody
	public Result<StatusItemExType> getStatusItemStateFrom(@PathVariable String statusTypeId){
		return Exec.exec("getStatusItemStateFrom", () -> this.statusItemService.getStatusItemStateFrom(statusTypeId));
	}

	@PostMapping
	@ResponseBody
	public boolean createStatusItem(@RequestBody StatusItem req){
		return Exec.exec("create status-item", () -> this.statusItemService.createStatusItem(req) );
	}

	@PutMapping
	@ResponseBody
	public boolean updateStatusItem(@RequestBody StatusItem req){
		return Exec.exec("update status-item", () -> this.statusItemService.updateStatusItem(req) );
	}

	@DeleteMapping(value = "/{id}")
	@ResponseBody
	public boolean deleteStatusItem(@PathVariable(value = "id") String id){
		return Exec.exec("delete status-item", () -> this.statusItemService.deleteStatusItem(id) );
	}


}
