package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.TimesheetEx;
import it.mapsgroup.gzoom.mybatis.dto.UserCtxPermissionView;
import it.mapsgroup.gzoom.service.UserCtxPermissionViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static it.mapsgroup.gzoom.security.Principals.principal;

@RestController
@RequestMapping(value = "user-ctx-permission-view", produces = { MediaType.APPLICATION_JSON_VALUE })
public class UserCtxPermissionViewController {
    private final UserCtxPermissionViewService userCtxPermissionViewService;

    @Autowired
    public UserCtxPermissionViewController(UserCtxPermissionViewService userCtxPermissionViewService) {
        this.userCtxPermissionViewService = userCtxPermissionViewService;
    }

    @GetMapping()
    @ResponseBody
    public Result<UserCtxPermissionView> getPermissions() {
        return Exec.exec("user-ctx-permission get", this.userCtxPermissionViewService::getUserCtxPermissions);
    }
}
