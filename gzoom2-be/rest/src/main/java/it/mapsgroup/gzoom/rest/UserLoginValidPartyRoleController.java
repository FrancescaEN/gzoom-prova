package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.UserLoginValidPartyRoleEx;
import it.mapsgroup.gzoom.service.UserLoginValidPartyRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class UserLoginValidPartyRoleController {

    private final UserLoginValidPartyRoleService userLoginValidPartyRoleService;

    @Autowired
    public UserLoginValidPartyRoleController(UserLoginValidPartyRoleService userLoginValidPartyRoleService) {
        this.userLoginValidPartyRoleService = userLoginValidPartyRoleService;
    }

    @RequestMapping(value = "user-login-party-role/{username}", method = RequestMethod.GET)
    @ResponseBody
    public Result<UserLoginValidPartyRoleEx> getUserLoginPartyRoleList(@PathVariable(value = "username") String username) {
        return Exec.exec("userLoginPartyRole get", () -> userLoginValidPartyRoleService.getUserLoginValidPartyRoleList(username));
    }
}
