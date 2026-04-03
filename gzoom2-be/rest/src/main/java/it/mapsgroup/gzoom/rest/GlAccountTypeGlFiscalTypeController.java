package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountTypeGlFiscalType;
import it.mapsgroup.gzoom.service.GlAccountTypeGlFiscalTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "gl-account-type-gl-fiscal-type", produces = { MediaType.APPLICATION_JSON_VALUE })
public class GlAccountTypeGlFiscalTypeController {


    private final GlAccountTypeGlFiscalTypeService glAccountTypeGlFiscalTypeService;

    @Autowired
    public GlAccountTypeGlFiscalTypeController(GlAccountTypeGlFiscalTypeService glAccountTypeGlFiscalTypeService) {
        this.glAccountTypeGlFiscalTypeService = glAccountTypeGlFiscalTypeService;
    }

    @GetMapping(value = "/{glAccountTypeId}")
    @ResponseBody
    public Result<GlAccountTypeGlFiscalType> getGlAccountTypeGlFiscalTypeList(@PathVariable(value = "glAccountTypeId") String glAccountTypeId) {
        return Exec.exec("gl-account-type-gl-fiscal-type-list/ get", () -> glAccountTypeGlFiscalTypeService.getGlAccountTypeGlFiscalTypeList(glAccountTypeId));
    }

    @PostMapping
    @ResponseBody
    public boolean createGlAccountTypeGlFiscalType(@RequestBody GlAccountTypeGlFiscalType req){
        return Exec.exec("create gl-account-type-gl-fiscal-type", () -> this.glAccountTypeGlFiscalTypeService.createGlAccountTypeGlFiscalType(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateGlAccountTypeGlFiscalType(@RequestBody GlAccountTypeGlFiscalType req){
        return Exec.exec("update gl-account-type-gl-fiscal-type", () -> this.glAccountTypeGlFiscalTypeService.updateGlAccountTypeGlFiscalType(req) );
    }

    @DeleteMapping(value = "/{glAccountTypeId}/{glFiscalTypeId}")
    @ResponseBody
    public boolean deleteGlAccountTypeGlFiscalType(@PathVariable(value = "glAccountTypeId") String glAccountTypeId, @PathVariable(value = "glFiscalTypeId") String glFiscalTypeId){
        return Exec.exec("delete gl-account-type-gl-fiscal-type", () -> this.glAccountTypeGlFiscalTypeService.deleteGlAccountTypeGlFiscalType(glAccountTypeId, glFiscalTypeId) );
    }
}
