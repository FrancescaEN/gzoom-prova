package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.RoleType;
import it.mapsgroup.gzoom.service.RoleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "role-types", produces = { MediaType.APPLICATION_JSON_VALUE })
public class RoleTypeController {
	private final RoleTypeService roleTypeService;

	@Autowired
	public RoleTypeController(RoleTypeService roleTypeService) {
		this.roleTypeService = roleTypeService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public Result<RoleType> getRoleTypes() {
		return Exec.exec("role-types get", roleTypeService::getRoleTypes);
	}

	@RequestMapping(value = "/parent-type/{parentTypeId}", method = RequestMethod.GET)
	@ResponseBody
	public Result<RoleType> getRoleTypeByParentTypeId(@PathVariable String parentTypeId) {
		return Exec.exec("role-types get", () -> this.roleTypeService.getRoleTypeByParentTypeId(parentTypeId));
	}

	@RequestMapping(value = "ou", method = RequestMethod.GET)
	@ResponseBody
	public Result<RoleType> getRoleTypeByOUAndLikeGOAL() {
		return Exec.exec("role-types get ou", this.roleTypeService::getRoleTypeByOUAndLikeGOAL);
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	@ResponseBody
	public boolean updateRoleType(@RequestBody RoleType roleType) {
		return Exec.exec("role-type update", () -> roleTypeService.updateRoleType(roleType));
	}

	@RequestMapping(value = "/{roleTypes}", method = RequestMethod.DELETE)
	@ResponseBody
	public boolean deleteRoleType(@PathVariable(value = "roleTypes") String[] roleTypes) {
		return Exec.exec("role-type delete", () -> roleTypeService.deleteRoleType(roleTypes));

	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public String createRoleType(@RequestBody RoleType roleType) {
		return Exec.exec("role-type post", () -> roleTypeService.createRoleType(roleType));
	}
}
