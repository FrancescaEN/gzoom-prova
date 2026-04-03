package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountRole;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountWithWorkEffortPurposeTypeView;
import it.mapsgroup.gzoom.service.GlAccountRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "gl-account-role", produces = { MediaType.APPLICATION_JSON_VALUE })
public class GlAccountRoleController {
    private final GlAccountRoleService glAccountRoleService;

    @Autowired
    public GlAccountRoleController(GlAccountRoleService glAccountRoleService) {
        this.glAccountRoleService = glAccountRoleService;
    }

    @GetMapping( "{glAccountId}")
    @ResponseBody
    public boolean showWarningUODetected(@PathVariable String glAccountId){
        return Exec.exec("show warning UODetected", () -> this.glAccountRoleService.showWarningUODetected(glAccountId));
    }

    @GetMapping(value = "uo-detected/{glAccountId}")
    @ResponseBody
    public Result<GlAccountRole> getUoDetectedByGlAccountId(@PathVariable String glAccountId){
        return Exec.exec("get gl-account-role by gl-account-id", () -> this.glAccountRoleService.getUoDetectedByGlAccountId(glAccountId) );
    }


    @PostMapping()
    @ResponseBody
    public boolean createGlAccountRole(@RequestBody GlAccountRole[] glAccountRoles){
        return Exec.exec("create gl-account-role", () -> this.glAccountRoleService.createGlAccountRole(glAccountRoles) );
    }

    @PutMapping()
    @ResponseBody
    public boolean updateGlAccountRole(@RequestBody GlAccountRole[] glAccountRoles){
        return Exec.exec("update gl-account-role", () -> this.glAccountRoleService.updateGlAccountRole(glAccountRoles) );
    }

    @PostMapping("delete")
    @ResponseBody
    public boolean deleteGlAccountRole(@RequestBody GlAccountRole[] glAccountRoles){
        return Exec.exec("delete gl-account-role", () -> this.glAccountRoleService.deleteGlAccountRole(glAccountRoles) );
    }
}
