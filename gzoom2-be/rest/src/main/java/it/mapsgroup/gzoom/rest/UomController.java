package it.mapsgroup.gzoom.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.Uom;
import it.mapsgroup.gzoom.mybatis.dto.UomEx;
import it.mapsgroup.gzoom.service.UomService;

/**
 */
@RestController
@RequestMapping(value = "uom", produces = { MediaType.APPLICATION_JSON_VALUE })
public class UomController {

    private final UomService uomService;

    @Autowired
    public UomController(UomService uomService) {
        this.uomService = uomService;
    }

    @RequestMapping(value = "/value", method = RequestMethod.GET)
    @ResponseBody
    public Result<UomEx> getUoms() {
        return Exec.exec("uom get", () -> uomService.getUoms());
    }


    @GetMapping
    @ResponseBody
    public Result<Uom> getAllUom() {
        return Exec.exec("uom get all", () -> uomService.getAllUom());
    }


    @RequestMapping(value = "/value/{id}", method = RequestMethod.GET)
    @ResponseBody
    public UomEx getUomRatingScale(@PathVariable(value = "id") String id) {
        return Exec.exec("uom get id", () -> uomService.getUom(id));
    }

    @RequestMapping(value = "/isRatingScale/{uomId}", method = RequestMethod.GET)
    @ResponseBody
    public boolean isRatingScale(@PathVariable String uomId) {
        return Exec.exec("verify if uom type id is rating scale", () -> uomService.isRatingScale(uomId));
    }

    @RequestMapping(value = "/isRatingScale/gl-account/{glAccountId}", method = RequestMethod.GET)
    @ResponseBody
    public boolean isRatingScaleByGlAccount(@PathVariable String glAccountId) {
        return Exec.exec("verify if uom type id is rating scale", () -> uomService.isRatingScaleByGlAccount(glAccountId));
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public String createUom(@RequestBody Uom req) {
        return Exec.exec("uom post", () -> uomService.createUom(req));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String updateUom(@PathVariable(value = "id") String id, @RequestBody Uom req) {
        return Exec.exec("uom put", () -> uomService.updateUom(id, req));
    }

    @RequestMapping(value = "/{uoms}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteUom(@PathVariable(value = "uoms") String uoms) {
        return Exec.exec("uom delete", () -> uomService.deleteUoms(uoms));
    }
}
