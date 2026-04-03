package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountMeasRatSc;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountType;
import it.mapsgroup.gzoom.service.GlAccountMeasRatScService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "gl-account-meas-rat-sc", produces = { MediaType.APPLICATION_JSON_VALUE })
public class GlAccountMeasRatScController {
    private final GlAccountMeasRatScService glAccountMeasRatScService;

    @Autowired
    public GlAccountMeasRatScController(GlAccountMeasRatScService glAccountMeasRatScService) {
        this.glAccountMeasRatScService = glAccountMeasRatScService;
    }

    @GetMapping("warning-value-list/{glAccountId}")
    @ResponseBody
    public boolean showWarningValueList(@PathVariable String glAccountId) {
        return Exec.exec("show-warning-value-list", () -> this.glAccountMeasRatScService.showWarningValueList(glAccountId));
    }

    @GetMapping("{glAccountId}")
    @ResponseBody
    public Result<GlAccountMeasRatSc> getGlAccountMeasRatScByGlAccountId(@PathVariable String glAccountId){
        return Exec.exec("get gl-account-meas-rat-sc by gl-account-id", () -> this.glAccountMeasRatScService.getGlAccountMeasRatScByGlAccountId(glAccountId));
    }

    @PostMapping
    @ResponseBody
    public boolean createGlAccountMeasRatSc(@RequestBody GlAccountMeasRatSc req){
        return Exec.exec("create gl-account-meas-rat-sc", () -> this.glAccountMeasRatScService.createGlAccountMeasRatSc(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateGlAccountMeasRatSc(@RequestBody GlAccountMeasRatSc[] req){
        return Exec.exec("update gl-account-meas-rat-sc", () -> this.glAccountMeasRatScService.updateGlAccountMeasRatSc(req) );
    }

    @PostMapping("delete")
    @ResponseBody
    public boolean deleteGlAccountMeasRatSc(@RequestBody GlAccountMeasRatSc[] req){
        return Exec.exec("delete gl-account-meas-rat-sc", () -> this.glAccountMeasRatScService.deleteGlAccountMeasRatSc(req) );
    }
}
