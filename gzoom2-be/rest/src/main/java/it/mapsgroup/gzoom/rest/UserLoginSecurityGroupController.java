package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.UserLoginSecurityGroup;
import it.mapsgroup.gzoom.service.UserLoginSecurityGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping(value = "user-login-security-group", produces = { MediaType.APPLICATION_JSON_VALUE })
public class UserLoginSecurityGroupController {
    private final UserLoginSecurityGroupService userLoginSecurityGroupService;

    @Autowired
    public UserLoginSecurityGroupController(UserLoginSecurityGroupService userLoginSecurityGroupService) {
        this.userLoginSecurityGroupService = userLoginSecurityGroupService;
    }

    @GetMapping("/group-id/{groupId}")
    @ResponseBody
    public Result<UserLoginSecurityGroup> getUserLoginSecurityGroupList(@PathVariable String groupId){
        return Exec.exec("get user-login-security-group list", () -> this.userLoginSecurityGroupService.getUserLoginSecurityGroupByGroupId(groupId));
    }

    @PostMapping
    @ResponseBody
    public boolean createUserLoginSecurityGroup(@RequestBody UserLoginSecurityGroup req){
        return Exec.exec("create user-login-security-group", () -> this.userLoginSecurityGroupService.createUserLoginSecurityGroup(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateUserLoginSecurityGroup(@RequestBody UserLoginSecurityGroup req){
        return Exec.exec("update user-login-security-group", () -> this.userLoginSecurityGroupService.updateUserLoginSecurityGroup(req) );
    }

    @DeleteMapping(value = "/{userLoginId}/{groupId}/{fromDate}")
    @ResponseBody
    public boolean deleteUserLoginSecurityGroup(@PathVariable String userLoginId, @PathVariable String groupId, @PathVariable String fromDate){
        Instant fb = Instant.parse(fromDate);
        return Exec.exec("delete user-login-security-group list", () -> this.userLoginSecurityGroupService.deleteUserLoginSecurityGroup(userLoginId, groupId, fb) );
    }
}
