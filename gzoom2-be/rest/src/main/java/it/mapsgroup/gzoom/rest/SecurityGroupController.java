package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.SecurityGroup;
import it.mapsgroup.gzoom.service.SecurityGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "security-group", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SecurityGroupController {
    private final SecurityGroupService securityGroupService;

    @Autowired
    public SecurityGroupController(SecurityGroupService securityGroupService) {
        this.securityGroupService = securityGroupService;
    }

    @GetMapping(value = "/{groupId}")
    @ResponseBody
    public SecurityGroup getSecurityGroupById(@PathVariable String groupId){
        return Exec.exec("get security group by id", ()-> this.securityGroupService.getSecurityGroupById(groupId));
    }

    @GetMapping
    @ResponseBody
    public Result<SecurityGroup> getSecurityGroupList(){
        return Exec.exec("get security-group list", this.securityGroupService::getSecurityGroupList);
    }

    @PostMapping
    @ResponseBody
    public boolean createSecurityGroup(@RequestBody SecurityGroup req){
        return Exec.exec("create security-group", () -> this.securityGroupService.createSecurityGroup(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateSecurityGroup(@RequestBody SecurityGroup req){
        return Exec.exec("update security-group", () -> this.securityGroupService.updateSecurityGroup(req) );
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public boolean deleteSecurityGroup(@PathVariable String id){
        return Exec.exec("delete security-group list", () -> this.securityGroupService.deleteSecurityGroup(id) );
    }
}
