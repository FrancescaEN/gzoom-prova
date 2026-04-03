package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountInputCalc;
import it.mapsgroup.gzoom.service.GlAccountInputCalcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "gl-account-input-calc", produces = { MediaType.APPLICATION_JSON_VALUE })
public class GlAccountInputCalcController {
    private final GlAccountInputCalcService glAccountInputCalcService;

    @Autowired
    public GlAccountInputCalcController(GlAccountInputCalcService glAccountInputCalcService) {
        this.glAccountInputCalcService = glAccountInputCalcService;
    }

    @GetMapping("warning-calculation-formula/{glAccountId}")
    @ResponseBody
    public boolean showWarningCalculationFormula(@PathVariable String glAccountId) {
        return Exec.exec("show-warning-calculation-formula", () -> this.glAccountInputCalcService.showWarningCalculationFormula(glAccountId));
    }

    @GetMapping("{glAccountId}")
    @ResponseBody
    public Result<GlAccountInputCalc> getGlAccountInputCalcByGlAccountId(@PathVariable String glAccountId) {
        return Exec.exec("get gl-account-input-calc by gl-account-id", () -> this.glAccountInputCalcService.getGlAccountInputCalcByGlAccountId(glAccountId));
    }

    @GetMapping("/ref/{glAccountIdRef}")
    @ResponseBody
    public Result<GlAccountInputCalc> getGlAccountInputCalcByGlAccountIdRef(@PathVariable String glAccountIdRef) {
        return Exec.exec("get gl-account-input-calc by gl-account-id", () -> this.glAccountInputCalcService.getGlAccountInputCalcByGlAccountIdRef(glAccountIdRef));
    }

    @PostMapping()
    @ResponseBody
    public GlAccountInputCalc createGlAccountInputCalc(@RequestBody GlAccountInputCalc glAccountInputCalc) {
        return Exec.exec("create gl-account-input-calc", () -> this.glAccountInputCalcService.createGlAccountInputCalc(glAccountInputCalc));
    }

    @PutMapping("/ref/{glAccountInputCalcId}")
    @ResponseBody
    public boolean updateGlAccountIdRef(@PathVariable String glAccountInputCalcId, @RequestBody(required = false) String glAccountIdRef) {
        return Exec.exec("update gl-account-id-ref in gl-account-input-calc", () -> this.glAccountInputCalcService.updateGlAccountIdRef(glAccountInputCalcId, glAccountIdRef));
    }

    @PutMapping()
    @ResponseBody
    public boolean updateGlAccountInputCalc(@RequestBody GlAccountInputCalc[] glAccountInputCalcs) {
        return Exec.exec("update gl-account-input-calc", () -> this.glAccountInputCalcService.updateGlAccountInputCalc(glAccountInputCalcs));
    }

    @DeleteMapping("{glAccountInputCalcIds}")
    @ResponseBody
    public boolean updateGlAccountInputCalc(@PathVariable String[] glAccountInputCalcIds) {
        return Exec.exec("delete gl-account-input-calc", () -> this.glAccountInputCalcService.deleteGlAccountImputCalc(glAccountInputCalcIds));
    }
}
