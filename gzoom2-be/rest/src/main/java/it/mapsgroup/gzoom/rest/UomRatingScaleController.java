package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.UomRatingScale;
import it.mapsgroup.gzoom.mybatis.dto.UomRatingScaleEx;
import it.mapsgroup.gzoom.service.UomRatingScaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 */
@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class UomRatingScaleController {

    private final UomRatingScaleService uomRatingScaleService;

    @Autowired
    public UomRatingScaleController(UomRatingScaleService uomRatingScaleService) {
        this.uomRatingScaleService = uomRatingScaleService;
    }

    @RequestMapping(value = "uom/scale/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Result<UomRatingScaleEx> getUomRatingScales(@PathVariable(value = "id") String id) {
        return Exec.exec("uom/scale get", () -> uomRatingScaleService.getUomRatingScales(id));
    }

    @RequestMapping(value = "uom/scale", method = RequestMethod.GET)
    @ResponseBody
    public Result<UomRatingScale> getAllUomRatingScale() {
        return Exec.exec("uom/uom-rating-value get", () -> uomRatingScaleService.getAllUomRatingScale());
    }

    @RequestMapping(value = "uom/uom-rating-value/{uomId}/{uomRatingValue}", method = RequestMethod.GET)
    @ResponseBody
    public UomRatingScale getUomRatingScaleByPrimaryKey(@PathVariable String uomId, @PathVariable String uomRatingValue) {
        return Exec.exec("uom/uom-rating-value get", () -> uomRatingScaleService.getUomRatingScaleByPrimaryKey(uomId, Double.parseDouble(uomRatingValue)));
    }
    
    @RequestMapping(value = "uom/scale/{id}/{value}", method = RequestMethod.GET)
    @ResponseBody
    public UomRatingScaleEx getUomRatingScale(@PathVariable(value = "id") String id, @PathVariable(value = "value") BigDecimal value) {
        return Exec.exec("uom/scale get", () -> uomRatingScaleService.getUomRatingScale(id, value));
    }

    @GetMapping(value = "uom/scale/uomId/{uomId}")
    @ResponseBody
    public Result<UomRatingScale> getUomRatingScale(@PathVariable(value = "uomId") String uomId){
        return Exec.exec("get UomRatingScale with uom id", () -> this.uomRatingScaleService.getUomRatingScale(uomId));
    }

    @GetMapping(value = "uom/gl-account/{glAccountId}")
    @ResponseBody
    public Result<UomRatingScale> getByGlAccountId(@PathVariable String glAccountId){
        return Exec.exec("get UomRatingScale with glAccountId", () -> this.uomRatingScaleService.getByGlAccountId(glAccountId));
    }

    @GetMapping(value = "uom/gl-account-meas-rat-sc/{glAccountId}")
    @ResponseBody
    public Result<UomRatingScale> getByGlAccountIdOnGlAccountMeasRatSc(@PathVariable String glAccountId){
        return Exec.exec("get UomRatingScale with glAccountId on gl-account-meas-rat-sc", () -> this.uomRatingScaleService.getByGlAccountIdOnGlAccountMeasRatSc(glAccountId));
    }

    @GetMapping(value = "uom/gl-account-meas-rat-sc-excluding/{glAccountId}")
    @ResponseBody
    public Result<UomRatingScale> getUomRatingScalesExcludingGlAccount(@PathVariable String glAccountId){
        return Exec.exec("get UomRatingScale with glAccountId excluding on gl-account-meas-rat-sc", () -> this.uomRatingScaleService.getUomRatingScalesExcludingGlAccount(glAccountId));
    }


    @RequestMapping(value = "uom/scale", method = RequestMethod.POST)
    @ResponseBody
    public UomRatingScale createUomRatingScale(@RequestBody UomRatingScale req) {
        return Exec.exec("uom/scale post", () -> uomRatingScaleService.createUomRatingScale(req));
    }

    @RequestMapping(value = "uom/scale", method = RequestMethod.PUT)
    @ResponseBody
    public String updateUomRatingScale(@RequestBody UomRatingScale req) {
        return Exec.exec("uom/scale put", () -> uomRatingScaleService.updateUomRatingScale(req));
    }

    @RequestMapping(value = "uom/scale/{id}/{value}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteUomRatingScale(@PathVariable(value = "id") String id, @PathVariable(value = "value") Double value) {
        return Exec.exec("uom/scale delete", () -> uomRatingScaleService.deleteUomRatingScale(id, value));
    }
}
