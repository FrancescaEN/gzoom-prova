package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.SecurityGroupContent;
import it.mapsgroup.gzoom.service.SecurityGroupContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping(value = "security-group-content", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SecurityGroupContentController {
    private final SecurityGroupContentService securityGroupContentService;

    @Autowired
    public SecurityGroupContentController(SecurityGroupContentService securityGroupContentService) {
        this.securityGroupContentService = securityGroupContentService;
    }

    @GetMapping("/group-id/{groupId}")
    @ResponseBody
    public Result<SecurityGroupContent> getSecurityGroupContentList(@PathVariable String groupId){
        return Exec.exec("get security-group-content list", () -> this.securityGroupContentService.getSecurityGroupContentList(groupId));
    }

    @PostMapping
    @ResponseBody
    public boolean createSecurityGroupContent(@RequestBody SecurityGroupContent req){
        return Exec.exec("create security-group-content", () -> this.securityGroupContentService.createSecurityGroupContent(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateSecurityGroupContent(@RequestBody SecurityGroupContent req){
        return Exec.exec("update security-group-content", () -> this.securityGroupContentService.updateSecurityGroupContent(req) );
    }

    @DeleteMapping(value = "/{groupId}/{contentId}/{fromDate}")
    @ResponseBody
    public boolean deleteSecurityGroupContent(@PathVariable String groupId, @PathVariable String contentId, @PathVariable String fromDate){
        Instant fd = Instant.parse(fromDate);
        return Exec.exec("delete security-group-content list", () -> this.securityGroupContentService.deleteSecurityGroupContent(groupId, contentId, fd) );
    }
}
