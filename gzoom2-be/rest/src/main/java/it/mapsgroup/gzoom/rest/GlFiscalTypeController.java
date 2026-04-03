package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.GlFiscalType;
import it.mapsgroup.gzoom.service.GlFiscalTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "gl-fiscal-type", produces = { MediaType.APPLICATION_JSON_VALUE })
public class GlFiscalTypeController {
    private final GlFiscalTypeService glFiscalTypeService;

    @Autowired
    public GlFiscalTypeController(GlFiscalTypeService glFiscalTypeService) {
        this.glFiscalTypeService = glFiscalTypeService;
    }

    @GetMapping
    @ResponseBody
    public Result<GlFiscalType> getGlFiscalType(){
        return Exec.exec("get gl-fiscal-type", this.glFiscalTypeService::getGlFiscalType);
    }

    @GetMapping("is-indicator-used/{isIndicatorUsed}")
    @ResponseBody
    public Result<GlFiscalType> getGlFiscalTypeByIsIndicatorUsed(@PathVariable String isIndicatorUsed){
        return Exec.exec("get gl-fiscal-type by indicator used", () -> this.glFiscalTypeService.getGlFiscalTypeByIsIndicatorUsed(isIndicatorUsed));
    }

    @GetMapping("account-type-enum-id/{accountTypeEnumId}")
    @ResponseBody
    public Result<GlFiscalType> getGlFiscalTypeByAccountTypeEnumId(@PathVariable String accountTypeEnumId){
        return Exec.exec("get gl-fiscal-type by accountTypeEnumId", () -> this.glFiscalTypeService.getGlFiscalTypeByAccountTypeEnumId(accountTypeEnumId));
    }

    @GetMapping("account-type-enum-id")
    @ResponseBody
    public Result<GlFiscalType> getGlFiscalTypeForNewIndicatorMovement(@RequestParam String accountTypeEnumId, @RequestParam String glAccountId, @RequestParam String customTimePeriodId, @RequestParam String roleTypeId, @RequestParam String partyId, @RequestParam String voucherRef){
        return Exec.exec("get gl-fiscal-type by accountTypeEnumId", () -> this.glFiscalTypeService.getGlFiscalTypeForNewIndicatorMovement(accountTypeEnumId, glAccountId, customTimePeriodId, roleTypeId, partyId, voucherRef));
    }

    @PostMapping
    @ResponseBody
    public boolean createGlFiscalType(@RequestBody GlFiscalType req){
        return Exec.exec("create gl-fiscal-type", () -> this.glFiscalTypeService.createGlFiscalType(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateGlFiscalType(@RequestBody GlFiscalType req){
        return Exec.exec("update gl-fiscal-type", () -> this.glFiscalTypeService.updateGlFiscalType(req) );
    }

    @DeleteMapping(value = "/{glFiscalTypeId}")
    @ResponseBody
    public boolean deleteGlFiscalType(@PathVariable(value = "glFiscalTypeId") String id){
        return Exec.exec("delete gl-fiscal-type list", () -> this.glFiscalTypeService.deleteGlFiscalType(id) );
    }

}
