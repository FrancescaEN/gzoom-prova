package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountType;
import it.mapsgroup.gzoom.service.GlAccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "gl-account-type", produces = { MediaType.APPLICATION_JSON_VALUE })
public class GlAccountTypeController {
    private final GlAccountTypeService glAccountTypeService;

    @Autowired
    public GlAccountTypeController(GlAccountTypeService glAccountTypeService) {
        this.glAccountTypeService = glAccountTypeService;
    }

    @GetMapping (value = {"", "/{accountTypeEnumId}"})
    @ResponseBody
    public Result<GlAccountType> getGlAccountTypeList(@PathVariable(required = false) String accountTypeEnumId){
        if (accountTypeEnumId != null) {
            return Exec.exec("get gl-account-type-list-with-accountTypeEnumId", () -> this.glAccountTypeService.getGlAccountTypeList(accountTypeEnumId) );
        }
        return Exec.exec("get gl-account-type-list", this.glAccountTypeService::getGlAccountTypeList);
    }

    @GetMapping ( "/{accountTypeEnumId}/{isReservedAccount}")
    @ResponseBody
    public Result<GlAccountType> getGlAccountTypeList(@PathVariable String accountTypeEnumId, @PathVariable String isReservedAccount){

            return Exec.exec("get gl-account-type-list-with-accountTypeEnumId-and-isReservedAccount", () -> this.glAccountTypeService.getGlAccountTypeList(accountTypeEnumId, isReservedAccount) );

    }

    @GetMapping ( "/glAccountTypeId/{glAccountTypeId}")
    @ResponseBody
    public GlAccountType getGlAccountTypeId(@PathVariable String glAccountTypeId){

        return Exec.exec("get gl-account-type-by-glAccountTypeId", () -> this.glAccountTypeService.getGlAccountTypeId(glAccountTypeId) );

    }

    @PostMapping
    @ResponseBody
    public boolean createGlAccountType(@RequestBody GlAccountType req){
        return Exec.exec("create gl-account-type", () -> this.glAccountTypeService.createGlAccountType(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateGlAccountType(@RequestBody GlAccountType req){
        return Exec.exec("update gl-account-type", () -> this.glAccountTypeService.updateGlAccountType(req) );
    }

    @DeleteMapping(value = "/{glAccountTypeId}")
    @ResponseBody
    public boolean deleteGlAccountType(@PathVariable(value = "glAccountTypeId") String id){
        return Exec.exec("delete gl-account-type", () -> this.glAccountTypeService.deleteGlAccountType(id) );
    }
}
