package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.SecurityPermission;
import it.mapsgroup.gzoom.service.SecurityPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "security-permission", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SecurityPermissionController {
    private final SecurityPermissionService securityPermissionService;

    @Autowired
    public SecurityPermissionController(SecurityPermissionService securityPermissionService) {
        this.securityPermissionService = securityPermissionService;
    }

    @GetMapping("/enabled/{enabled}")
    @ResponseBody
    public Result<SecurityPermission> getByEnabledOrderByPrimaryKey(@PathVariable String enabled){
        return Exec.exec("get security-permission by enabled", () -> this.securityPermissionService.findByEnabledOrderByPrimaryKey(enabled));
    }
}
