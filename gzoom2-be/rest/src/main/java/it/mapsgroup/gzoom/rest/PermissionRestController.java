package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.mybatis.service.PermissionService;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import it.mapsgroup.gzoom.service.PermissionRestService;
import it.mapsgroup.gzoom.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static it.mapsgroup.gzoom.security.Principals.principal;

@RestController
@RequestMapping(value = "permission", produces = { MediaType.APPLICATION_JSON_VALUE })
public class PermissionRestController {

    private PermissionRestService permissionRestService;
    private PermissionService permissionService;

    @Autowired
    public PermissionRestController(PermissionRestService permissionRestService, PermissionService permissionService) {

        this.permissionRestService = permissionRestService;
        this.permissionService = permissionService;
    }

    @RequestMapping(value = "permission/role/{context}", method = RequestMethod.GET)
    @ResponseBody
    public boolean getPermissionAdmin(@PathVariable(value = "context")  String context) {
        String permission = ContextPermissionPrefixEnum.getPermissionPrefix(context);

        return Exec.exec("permission get", () -> permissionService.isFullAdmin(principal().getUserLoginId(), ContextPermissionPrefixEnum.valueOf(context)));
    }
}
