package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.CustomTimePeriod;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountOrganization;
import it.mapsgroup.gzoom.service.GlAccountOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "gl-account-organization", produces = { MediaType.APPLICATION_JSON_VALUE })
public class GlAccountOrganizationController {
    private final GlAccountOrganizationService glAccountOrganizationService;

    @Autowired
    public GlAccountOrganizationController(GlAccountOrganizationService glAccountOrganizationService) {
        this.glAccountOrganizationService = glAccountOrganizationService;
    }

    @GetMapping("{glAccountId}")
    @ResponseBody
    public Result<GlAccountOrganization> getGlAccountOrganizationByGlAccountId(@PathVariable String glAccountId){
        return Exec.exec("get gl-account-organization by glAccountId", () -> this.glAccountOrganizationService.getGlAccountOrganizationByGlAccountId(glAccountId));
    }

    @PostMapping("{glAccountId}/{organizationPartyId}")
    @ResponseBody
    public boolean createGlAccountOrganization(@PathVariable String glAccountId, @PathVariable String[] organizationPartyId, @RequestBody(required = false) GlAccountOrganization req){
        return Exec.exec("create gl-account-organization", () -> this.glAccountOrganizationService.createGlAccountOrganization(glAccountId, organizationPartyId, req.getFromDate(), req.getThruDate()) );
    }

    @PutMapping()
    @ResponseBody
    public boolean updateGlAccountOrganization(@RequestBody GlAccountOrganization[] req){
        return Exec.exec("update gl-account-organization", () -> this.glAccountOrganizationService.updateGlAccountOrganization(req) );
    }

    @DeleteMapping("{glAccountId}/{organizationId}")
    @ResponseBody
    public boolean deleteGlAccountOrganization(@PathVariable String glAccountId, @PathVariable String[] organizationId){
        return Exec.exec("delete gl-account-organization", () -> this.glAccountOrganizationService.deleteGlAccountOrganization(glAccountId, organizationId));
    }

}
