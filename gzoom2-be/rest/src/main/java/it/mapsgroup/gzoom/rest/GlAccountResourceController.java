package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountResource;
import it.mapsgroup.gzoom.service.GlAccountResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "gl-account-resource", produces = { MediaType.APPLICATION_JSON_VALUE })
public class GlAccountResourceController {


    private final GlAccountResourceService glAccountResourceService;

    @Autowired
    public GlAccountResourceController(GlAccountResourceService glAccountResourceService) {
        this.glAccountResourceService = glAccountResourceService;
    }

    @GetMapping(value = "/{glAccountTypeId}")
    @ResponseBody
    public Result<GlAccountResource> getGlAccountResourceList(@PathVariable(value = "glAccountTypeId") String glAccountTypeId) {
        return Exec.exec("gl-account-resource-list/ get", () -> glAccountResourceService.getGlAccountResourceList(glAccountTypeId));
    }

    @PostMapping
    @ResponseBody
    public boolean createGlAccountResource(@RequestBody GlAccountResource req){
        return Exec.exec("create gl-account-resource", () -> this.glAccountResourceService.createGlAccountResource(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateGlAccountResource(@RequestBody GlAccountResource req){
        return Exec.exec("update gl-account-resource", () -> this.glAccountResourceService.updateGlAccountResource(req) );
    }

    @DeleteMapping(value = "/{glAccountTypeId}/{glResourceTypeId}")
    @ResponseBody
    public boolean deleteGlAccountResource(@PathVariable(value = "glAccountTypeId") String glAccountTypeId, @PathVariable(value = "glResourceTypeId") String glResourceTypeId){
        return Exec.exec("delete gl-account-resource", () -> this.glAccountResourceService.deleteGlAccountResource(glAccountTypeId, glResourceTypeId) );
    }
}
