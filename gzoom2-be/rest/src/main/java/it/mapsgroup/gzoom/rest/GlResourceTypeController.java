package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.GlResourceType;
import it.mapsgroup.gzoom.service.GlResourceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "gl-resource-type", produces = { MediaType.APPLICATION_JSON_VALUE })
public class GlResourceTypeController {
    private final GlResourceTypeService glResourceTypeService;

    @Autowired
    public GlResourceTypeController(GlResourceTypeService glResourceTypeService) {
        this.glResourceTypeService = glResourceTypeService;
    }

    @GetMapping
    @ResponseBody
    public Result<GlResourceType> getGlResourceType(){
        return Exec.exec("get gl-resource-type", this.glResourceTypeService::getGlResourceType);
    }

    @GetMapping("{glAccountTypeId}")
    @ResponseBody
    public Result<GlResourceType> getByGlAccountTypeId(@PathVariable String glAccountTypeId){
        return Exec.exec("get gl-resource-type by gl-account-type-id", () -> this.glResourceTypeService.getByGlAccountTypeId(glAccountTypeId));
    }

    @PostMapping
    @ResponseBody
    public boolean createGlResourceType(@RequestBody GlResourceType req){
        return Exec.exec("create gl-resource-type", () -> this.glResourceTypeService.createGlResourceType(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateGlResourceType(@RequestBody GlResourceType req){
        return Exec.exec("update gl-resource-type", () -> this.glResourceTypeService.updateGlResourceType(req) );
    }

    @DeleteMapping(value = "/{glResourceTypeId}")
    @ResponseBody
    public boolean deleteGlResourceType(@PathVariable(value = "glResourceTypeId") String id){
        return Exec.exec("delete gl-resource-type", () -> this.glResourceTypeService.deleteGlResourceType(id) );
    }

}
