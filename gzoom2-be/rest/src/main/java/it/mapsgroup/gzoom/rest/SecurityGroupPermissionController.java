package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.SecurityGroupPermission;
import it.mapsgroup.gzoom.service.SecurityGroupPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "security-group-permission", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SecurityGroupPermissionController {
    private final SecurityGroupPermissionService securityGroupPermissionService;

    @Autowired
    public SecurityGroupPermissionController(SecurityGroupPermissionService securityGroupPermissionService) {
        this.securityGroupPermissionService = securityGroupPermissionService;
    }

    @GetMapping("/group-id/{groupId}")
    @ResponseBody
    public Result<SecurityGroupPermission> getSecurityGroupPermissionList(@PathVariable String groupId){
        return Exec.exec("get security-group-permission list", () -> this.securityGroupPermissionService.getSecurityGroupPermissionList(groupId));
    }

    @PostMapping
    @ResponseBody
    public boolean createSecurityGroupPermission(@RequestBody SecurityGroupPermission req){
        return Exec.exec("create security-group-permission", () -> this.securityGroupPermissionService.createSecurityGroupPermission(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateSecurityGroupPermission(@RequestBody SecurityGroupPermission req){
        return Exec.exec("update security-group-permission", () -> this.securityGroupPermissionService.updateSecurityGroupPermission(req) );
    }

    @DeleteMapping(value = "/{groupId}/{permissionId}")
    @ResponseBody
    public boolean deleteSecurityGroupPermission(@PathVariable String groupId, @PathVariable String permissionId){
        return Exec.exec("delete security-group-permission list", () -> this.securityGroupPermissionService.deleteSecurityGroupPermission(groupId, permissionId) );
    }
}
